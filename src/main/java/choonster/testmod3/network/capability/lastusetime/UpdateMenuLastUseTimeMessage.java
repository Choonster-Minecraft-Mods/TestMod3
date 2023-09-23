package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import choonster.testmod3.network.capability.UpdateMenuCapabilityMessage;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Updates the {@link ILastUseTime} for a single slot of an {@link AbstractContainerMenu}.
 *
 * @author Choonster
 */
public class UpdateMenuLastUseTimeMessage extends UpdateMenuCapabilityMessage<ILastUseTime, Long> {
	public UpdateMenuLastUseTimeMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final ILastUseTime lastUseTime
	) {
		super(
				LastUseTimeCapability.LAST_USE_TIME_CAPABILITY,
				facing, containerID, stateID, slotNumber, lastUseTime,
				LastUseTimeFunctions::convertLastUseTimeToLastUseTimeValue
		);
	}

	private UpdateMenuLastUseTimeMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int stateID,
			final int slotNumber,
			final long lastUseTime
	) {
		super(
				LastUseTimeCapability.LAST_USE_TIME_CAPABILITY,
				facing, windowID, stateID, slotNumber, lastUseTime
		);
	}

	public static UpdateMenuLastUseTimeMessage decode(final FriendlyByteBuf buffer) {
		return UpdateMenuCapabilityMessage.<ILastUseTime, Long, UpdateMenuLastUseTimeMessage>decode(
				buffer,
				LastUseTimeFunctions::decodeLastUseTimeValue,
				UpdateMenuLastUseTimeMessage::new
		);
	}

	public static void encode(final UpdateMenuLastUseTimeMessage message, final FriendlyByteBuf buffer) {
		UpdateMenuCapabilityMessage.encode(
				message,
				buffer,
				LastUseTimeFunctions::encodeLastUseTimeValue
		);
	}

	public static void handle(final UpdateMenuLastUseTimeMessage message, final CustomPayloadEvent.Context ctx) {
		UpdateMenuCapabilityMessage.handle(
				message,
				ctx,
				LastUseTimeFunctions::applyLastUseTimeValueToLastUseTime
		);
	}
}
