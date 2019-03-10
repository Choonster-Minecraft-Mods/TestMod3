package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.CapabilityLastUseTime;
import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link ILastUseTime} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerLastUseTimes extends MessageBulkUpdateContainerCapability<ILastUseTime, Long> {
	public MessageBulkUpdateContainerLastUseTimes(
			@Nullable final EnumFacing facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY,
				facing, windowID, items,
				LastUseTimeFunctions::convertLastUseTimeToLastUseTimeValue
		);
	}

	private MessageBulkUpdateContainerLastUseTimes(
			@Nullable final EnumFacing facing,
			final int windowID,
			final Int2ObjectMap<Long> capabilityData
	) {
		super(
				CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static MessageBulkUpdateContainerLastUseTimes decode(final PacketBuffer buffer) {
		return MessageBulkUpdateContainerCapability.decode(
				buffer,
				LastUseTimeFunctions::decodeLastUseTimeValue,
				MessageBulkUpdateContainerLastUseTimes::new
		);
	}

	public static void encode(final MessageBulkUpdateContainerLastUseTimes message, final PacketBuffer buffer) {
		MessageBulkUpdateContainerCapability.encode(
				message,
				buffer,
				LastUseTimeFunctions::encodeLastUseTimeValue
		);
	}

	public static void handle(final MessageBulkUpdateContainerLastUseTimes message, final Supplier<NetworkEvent.Context> ctx) {
		MessageBulkUpdateContainerCapability.handle(
				message,
				ctx,
				LastUseTimeFunctions::applyLastUseTimeValueToLastUseTime
		);
	}
}
