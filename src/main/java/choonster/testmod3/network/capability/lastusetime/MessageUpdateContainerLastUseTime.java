package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link ILastUseTime} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageUpdateContainerLastUseTime extends MessageUpdateContainerCapability<ILastUseTime, Long> {
	public MessageUpdateContainerLastUseTime(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final ILastUseTime lastUseTime
	) {
		super(
				CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY,
				facing, windowID, slotNumber, lastUseTime,
				LastUseTimeFunctions::convertLastUseTimeToLastUseTimeValue
		);
	}

	private MessageUpdateContainerLastUseTime(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final long lastUseTime
	) {
		super(
				CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY,
				facing, windowID, slotNumber, lastUseTime
		);
	}

	public static MessageUpdateContainerLastUseTime decode(final PacketBuffer buffer) {
		return MessageUpdateContainerCapability.decode(
				buffer,
				LastUseTimeFunctions::decodeLastUseTimeValue,
				MessageUpdateContainerLastUseTime::new
		);
	}

	public static void encode(final MessageUpdateContainerLastUseTime message, final PacketBuffer buffer) {
		MessageUpdateContainerCapability.encode(
				message,
				buffer,
				LastUseTimeFunctions::encodeLastUseTimeValue
		);
	}

	public static void handle(final MessageUpdateContainerLastUseTime message, final Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateContainerCapability.handle(
				message,
				ctx,
				LastUseTimeFunctions::applyLastUseTimeValueToLastUseTime
		);
	}
}
