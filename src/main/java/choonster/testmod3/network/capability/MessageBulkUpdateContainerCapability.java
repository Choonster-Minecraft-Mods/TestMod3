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
	final EnumFacing facing;

	/**
	 * The window ID of the {@link Container}.
	 */
	final int windowID;

	/**
	 * The capability data instances for each slot, indexed by their index in the original {@link NonNullList<ItemStack>}.
	 */
	final Int2ObjectMap<DATA> capabilityData;

	public MessageBulkUpdateContainerCapability(
			final Capability<HANDLER> capability,
			@Nullable final EnumFacing facing,
			final int windowID,
			final NonNullList<ItemStack> items,
			final CapabilityContainerUpdateMessageUtils.CapabilityDataConverter<HANDLER, DATA> capabilityDataConverter
	) {
		this.capability = capability;
		this.facing = facing;
		this.windowID = windowID;

		capabilityData = new Int2ObjectOpenHashMap<>();
		IntStream.range(0, items.size()).forEach(slotNumber -> {
			final ItemStack stack = items.get(slotNumber);

			CapabilityUtils.getCapability(stack, capability, facing).ifPresent((handler) -> {
				final DATA data = capabilityDataConverter.convert(handler);

				if (data != null) {
					capabilityData.put(slotNumber, data);
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
			final CapabilityContainerUpdateMessageUtils.CapabilityDataDecoder<DATA> capabilityDataDecoder,
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
			final int slotNumber = buffer.readInt();
			final DATA data = capabilityDataDecoder.decode(buffer);
			capabilityData.put(slotNumber, data);
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
			final CapabilityContainerUpdateMessageUtils.CapabilityDataEncoder<DATA> capabilityDataEncoder
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
	 * @param capabilityDataApplier A function that applies the capability data from a data instance to a capability handler instance
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
			final CapabilityContainerUpdateMessageUtils.CapabilityDataApplier<HANDLER, DATA> capabilityDataApplier
	) {
		if (!message.hasData()) { // Don't do anything if no data was sent
			ctx.get().setPacketHandled(true);
			return;
		}

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
				final int slotNumber = entry.getIntKey();
				final DATA data = entry.getValue();

				CapabilityContainerUpdateMessageUtils.applyCapabilityDataToContainerSlot(
						container,
						slotNumber,
						message.capability,
						message.facing,
						data,
						capabilityDataApplier
				);
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
}
