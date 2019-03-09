package choonster.testmod3.network.capability;

import choonster.testmod3.util.CapabilityUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.IntStream;

/**
 * Base class for messages that update the capability data for each slot of a {@link Container}.
 * <p>
 * The {@link HANDLER} type must override {@link Object#equals(Object)} to perform a value comparison and
 * {@link Object#hashCode()} to generate a hash code based on the values used in
 * {@link Object#equals(Object)}.
 *
 * @param <HANDLER> The capability handler type
 * @param <DATA>    The data type written to and read from the buffer
 * @author Choonster
 */
public abstract class MessageBulkUpdateContainerCapability<HANDLER, DATA> {
	/**
	 * The {@link Capability} instance to update.
	 */
	final Capability<HANDLER> capability;

	/**
	 * The {@link EnumFacing} to get the capability handler from.
	 */
	@Nullable
	EnumFacing facing;

	/**
	 * The window ID of the {@link Container}.
	 */
	int windowID;

	/**
	 * The capability data instances for each slot, indexed by their index in the original {@link List<ItemStack>}.
	 */
	final Int2ObjectMap<DATA> capabilityData;

	public MessageBulkUpdateContainerCapability(
			final Capability<HANDLER> capability,
			@Nullable final EnumFacing facing,
			final int windowID,
			final NonNullList<ItemStack> items,
			final CapabilityDataConverter<HANDLER, DATA> capabilityDataConverter
	) {
		this.capability = capability;
		this.facing = facing;
		this.windowID = windowID;

		capabilityData = new Int2ObjectOpenHashMap<>();
		IntStream.range(0, items.size()).forEach(index -> {
			final ItemStack stack = items.get(index);

			CapabilityUtils.getCapability(stack, capability, facing).ifPresent((handler) -> {
				final DATA data = capabilityDataConverter.convert(handler);

				if (data != null) {
					capabilityData.put(index, data);
				}
			});
		});
	}

	protected MessageBulkUpdateContainerCapability(
			final Capability<HANDLER> capability,
			@Nullable final EnumFacing facing,
			final int windowID,
			final Int2ObjectMap<DATA> capabilityData
	) {
		this.capability = capability;
		this.facing = facing;
		this.windowID = windowID;
		this.capabilityData = capabilityData;
	}

	/**
	 * Is there any capability data to sync?
	 *
	 * @return Is there any capability data to sync?
	 */
	public final boolean hasData() {
		return !capabilityData.isEmpty();
	}

	/**
	 * Decodes a bulk update message from the network.
	 *
	 * @param buffer                The packet buffer
	 * @param capabilityDataDecoder A function that decodes a data instance from the buffer
	 * @param messageFactory        A function to create the message instance
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 * @param <MESSAGE>             The message type
	 * @return The decoded message
	 */
	protected static <
			HANDLER,
			DATA,
			MESSAGE extends MessageBulkUpdateContainerCapability<HANDLER, DATA>
			>
	MESSAGE decode(
			final PacketBuffer buffer,
			final CapabilityDataDecoder<DATA> capabilityDataDecoder,
			final MessageFactory<HANDLER, DATA, MESSAGE> messageFactory
	) {
		final boolean hasFacing = buffer.readBoolean();

		final EnumFacing facing;
		if (hasFacing) {
			facing = buffer.readEnumValue(EnumFacing.class);
		} else {
			facing = null;
		}

		final int windowID = buffer.readInt();

		final Int2ObjectMap<DATA> capabilityData = new Int2ObjectOpenHashMap<>();

		final int numEntries = buffer.readInt();
		for (int i = 0; i < numEntries; i++) {
			final int index = buffer.readInt();
			final DATA data = capabilityDataDecoder.decode(buffer);
			capabilityData.put(index, data);
		}

		return messageFactory.createMessage(facing, windowID, capabilityData);
	}

	/**
	 * Encodes a bulk update message to be sent over the network.
	 *
	 * @param message               The message to encode
	 * @param buffer                The packet buffer
	 * @param capabilityDataEncoder A function that encodes a data instance to the buffer
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 * @param <MESSAGE>             The message type
	 */
	protected static <
			HANDLER,
			DATA,
			MESSAGE extends MessageBulkUpdateContainerCapability<HANDLER, DATA>
			>
	void encode(
			final MESSAGE message,
			final PacketBuffer buffer,
			final CapabilityDataEncoder<DATA> capabilityDataEncoder
	) {
		final boolean hasFacing = message.facing != null;
		buffer.writeBoolean(hasFacing);

		if (hasFacing) {
			buffer.writeEnumValue(message.facing);
		}

		buffer.writeInt(message.windowID);

		buffer.writeInt(message.capabilityData.size());
		Int2ObjectMaps.fastForEach(message.capabilityData, (entry) -> {
			buffer.writeInt(entry.getIntKey());
			capabilityDataEncoder.encode(entry.getValue(), buffer);
		});
	}

	/**
	 * Handles a bulk update message.
	 *
	 * @param message               The message to handle
	 * @param ctx                   The network context
	 * @param capabilityDataApplier A function that applies the capability data from a data instance to a capability handler instance.
	 * @param <HANDLER>             The capability handler type
	 * @param <DATA>                The data type written to and read from the buffer
	 * @param <MESSAGE>             The message type
	 */
	protected static <
			HANDLER,
			DATA,
			MESSAGE extends MessageBulkUpdateContainerCapability<HANDLER, DATA>
			>
	void handle(
			final MESSAGE message,
			final Supplier<NetworkEvent.Context> ctx,
			final CapabilityDataApplier<HANDLER, DATA> capabilityDataApplier
	) {
		if (!message.hasData()) return; // Don't do anything if no data was sent

		ctx.get().enqueueWork(() -> DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final EntityPlayer player = Minecraft.getInstance().player;

			final Container container;
			if (message.windowID == 0) {
				container = player.inventoryContainer;
			} else if (message.windowID == player.openContainer.windowId) {
				container = player.openContainer;
			} else {
				return;
			}

			Int2ObjectMaps.fastForEach(message.capabilityData, (entry) -> {
				final int index = entry.getIntKey();
				final DATA data = entry.getValue();

				final ItemStack originalStack = container.getSlot(index).getStack();

				CapabilityUtils.getCapability(originalStack, message.capability, message.facing).ifPresent(originalHandler -> {
					final ItemStack newStack = originalStack.copy();

					CapabilityUtils.getCapability(newStack, message.capability, message.facing).ifPresent(newHandler -> {
						capabilityDataApplier.apply(newHandler, data);

						if (!originalHandler.equals(newHandler)) {
							container.putStackInSlot(index, newStack);
						}
					});
				});
			});
		}));

		ctx.get().setPacketHandled(true);
	}

	/**
	 * A function that creates bulk update message instances from network data.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 * @param <MESSAGE> The message type
	 */
	@FunctionalInterface
	public interface MessageFactory<HANDLER, DATA, MESSAGE extends MessageBulkUpdateContainerCapability<HANDLER, DATA>> {
		MESSAGE createMessage(
				@Nullable EnumFacing facing,
				int windowID,
				Int2ObjectMap<DATA> capabilityData
		);
	}

	/**
	 * Converts a capability handler instance to a data instance.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataConverter<HANDLER, DATA> {
		@Nullable
		DATA convert(HANDLER handler);
	}

	/**
	 * A function that decodes a data instance from the buffer
	 *
	 * @param <DATA> The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataDecoder<DATA> {
		DATA decode(PacketBuffer buffer);
	}

	/**
	 * A function that encodes a data instance to the buffer
	 *
	 * @param <DATA> The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataEncoder<DATA> {
		void encode(DATA data, PacketBuffer buffer);
	}

	/**
	 * A function that applies the capability data from a data instance to a capability handler instance.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	@FunctionalInterface
	public interface CapabilityDataApplier<HANDLER, DATA> {
		void apply(HANDLER handler, DATA data);
	}
}
