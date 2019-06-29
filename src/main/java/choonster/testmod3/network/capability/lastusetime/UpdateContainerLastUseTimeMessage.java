package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import choonster.testmod3.network.capability.UpdateContainerCapabilityMessage;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link ILastUseTime} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class UpdateContainerLastUseTimeMessage extends UpdateContainerCapabilityMessage<ILastUseTime, Long> {
	public UpdateContainerLastUseTimeMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final ILastUseTime lastUseTime
	) {
		super(
				LastUseTimeCapability.LAST_USE_TIME_CAPABILITY,
				facing, windowID, slotNumber, lastUseTime,
				LastUseTimeFunctions::convertLastUseTimeToLastUseTimeValue
		);
	}

	private UpdateContainerLastUseTimeMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final long lastUseTime
	) {
		super(
				LastUseTimeCapability.LAST_USE_TIME_CAPABILITY,
				facing, windowID, slotNumber, lastUseTime
		);
	}

	public static UpdateContainerLastUseTimeMessage decode(final PacketBuffer buffer) {
		return UpdateContainerCapabilityMessage.<ILastUseTime, Long, UpdateContainerLastUseTimeMessage>decode(
				buffer,
				LastUseTimeFunctions::decodeLastUseTimeValue,
				UpdateContainerLastUseTimeMessage::new
		);
	}

	public static void encode(final UpdateContainerLastUseTimeMessage message, final PacketBuffer buffer) {
		UpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				LastUseTimeFunctions::encodeLastUseTimeValue
		);
	}

	public static void handle(final UpdateContainerLastUseTimeMessage message, final Supplier<NetworkEvent.Context> ctx) {
		UpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				LastUseTimeFunctions::applyLastUseTimeValueToLastUseTime
		);
	}
}
