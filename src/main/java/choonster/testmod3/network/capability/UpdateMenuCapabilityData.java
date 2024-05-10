package choonster.testmod3.network.capability;

import choonster.testmod3.client.util.ClientUtil;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.capabilities.Capability;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Base class for messages that update capability data for a single slot of an {@link AbstractContainerMenu}.
 *
 * @param <HANDLER>      The capability handler type
 * @param <DATA>         The data type written to and read from the buffer
 * @param capability     The {@link Capability} instance to update.
 * @param direction      The {@link Direction} to get the capability handler from.
 * @param containerID    The ID of the {@link AbstractContainerMenu}.
 * @param stateID        The state ID from the {@link AbstractContainerMenu}.
 * @param slotNumber     The slot's index in the {@link AbstractContainerMenu}.
 * @param capabilityData The capability data instance.
 * @author Choonster
 */
public record UpdateMenuCapabilityData<HANDLER, DATA>(
		Capability<HANDLER> capability,
		Optional<Direction> direction,
		int containerID,
		int stateID,
		int slotNumber,
		Optional<DATA> capabilityData
) {
	public UpdateMenuCapabilityData(
			final Capability<HANDLER> capability,
			@Nullable final Direction facing,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final HANDLER handler,
			final CapabilityDataConverter<HANDLER, DATA> capabilityDataConverter
	) {
		this(capability, Optional.ofNullable(facing), containerID, stateID, slotNumber, Optional.ofNullable(capabilityDataConverter.convert(handler)));
	}

	public static <
			HANDLER,
			DATA,
			MESSAGE extends UpdateMenuCapabilityDataMessage<HANDLER, DATA>
			>
	StreamCodec<RegistryFriendlyByteBuf, MESSAGE> streamCodec(
			final Capability<HANDLER> capability,
			final StreamCodec<RegistryFriendlyByteBuf, DATA> dataCodec,
			final MessageFactory<HANDLER, DATA, MESSAGE> messageFactory
	) {
		final StreamCodec<RegistryFriendlyByteBuf, UpdateMenuCapabilityData<HANDLER, DATA>> baseCodec = StreamCodec.composite(
				ByteBufCodecs.optional(Direction.STREAM_CODEC),
				UpdateMenuCapabilityData::direction,
				ByteBufCodecs.VAR_INT,
				UpdateMenuCapabilityData::containerID,
				ByteBufCodecs.VAR_INT,
				UpdateMenuCapabilityData::stateID,
				ByteBufCodecs.VAR_INT,
				UpdateMenuCapabilityData::slotNumber,
				ByteBufCodecs.optional(dataCodec),
				UpdateMenuCapabilityData::capabilityData,
				(direction, containerID, stateID, slotNumber, capabilityData) -> new UpdateMenuCapabilityData<>(
						capability,
						direction,
						containerID,
						stateID,
						slotNumber,
						capabilityData
				)
		);

		return baseCodec.map(messageFactory::createMessage, MESSAGE::data);
	}


	/**
	 * Is there any capability data to sync?
	 *
	 * @return Is there any capability data to sync?
	 */
	public boolean hasData() {
		return capabilityData.isPresent();
	}

	/**
	 * Handles an update message.
	 *
	 * @param capabilityDataApplier A function that applies the capability data from a data instance to a capability handler instance
	 */
	public void handle(
			final CapabilityDataApplier<HANDLER, DATA> capabilityDataApplier
	) {
		final var player = ClientUtil.getClientPlayer();

		if (player == null) {
			return;
		}

		final AbstractContainerMenu menu;
		if (containerID == 0) {
			menu = player.inventoryMenu;
		} else if (containerID == player.containerMenu.containerId) {
			menu = player.containerMenu;
		} else {
			return;
		}

		final var direction = this.direction.orElse(null);
		final var originalStack = menu.getSlot(slotNumber).getItem();

		originalStack.getCapability(capability, direction1).ifPresent(originalHandler -> {
			final var newStack = originalStack.copy();

			newStack.getCapability(capability, direction1).ifPresent(newHandler -> {
				capabilityDataApplier.apply(newHandler, capabilityData.orElseThrow());

				if (!originalHandler.equals(newHandler)) {
					menu.setItem(stateID, slotNumber, newStack);
				}
			});
		});
	}

	/**
	 * Represents an update message that wraps {@link UpdateMenuCapabilityData}.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 */
	public interface UpdateMenuCapabilityDataMessage<HANDLER, DATA> {
		UpdateMenuCapabilityData<HANDLER, DATA> data();
	}

	/**
	 * A function that creates update message instances from network data.
	 *
	 * @param <HANDLER> The capability handler type
	 * @param <DATA>    The data type written to and read from the buffer
	 * @param <MESSAGE> The message type
	 */
	@FunctionalInterface
	public interface MessageFactory<HANDLER, DATA, MESSAGE extends UpdateMenuCapabilityDataMessage<HANDLER, DATA>> {
		MESSAGE createMessage(UpdateMenuCapabilityData<HANDLER, DATA> data);
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
