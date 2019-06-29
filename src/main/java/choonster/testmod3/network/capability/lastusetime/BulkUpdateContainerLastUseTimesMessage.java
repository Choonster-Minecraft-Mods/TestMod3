package choonster.testmod3.network.capability.lastusetime;

import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.lastusetime.LastUseTimeCapability;
import choonster.testmod3.network.capability.BulkUpdateContainerCapabilityMessage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link ILastUseTime} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class BulkUpdateContainerLastUseTimesMessage extends BulkUpdateContainerCapabilityMessage<ILastUseTime, Long> {
	public BulkUpdateContainerLastUseTimesMessage(
			@Nullable final Direction facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				LastUseTimeCapability.LAST_USE_TIME_CAPABILITY,
				facing, windowID, items,
				LastUseTimeFunctions::convertLastUseTimeToLastUseTimeValue
		);
	}

	private BulkUpdateContainerLastUseTimesMessage(
			@Nullable final Direction facing,
			final int windowID,
			final Int2ObjectMap<Long> capabilityData
	) {
		super(
				LastUseTimeCapability.LAST_USE_TIME_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static BulkUpdateContainerLastUseTimesMessage decode(final PacketBuffer buffer) {
		return BulkUpdateContainerCapabilityMessage.<ILastUseTime, Long, BulkUpdateContainerLastUseTimesMessage>decode(
				buffer,
				LastUseTimeFunctions::decodeLastUseTimeValue,
				BulkUpdateContainerLastUseTimesMessage::new
		);
	}

	public static void encode(final BulkUpdateContainerLastUseTimesMessage message, final PacketBuffer buffer) {
		BulkUpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				LastUseTimeFunctions::encodeLastUseTimeValue
		);
	}

	public static void handle(final BulkUpdateContainerLastUseTimesMessage message, final Supplier<NetworkEvent.Context> ctx) {
		BulkUpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				LastUseTimeFunctions::applyLastUseTimeValueToLastUseTime
		);
	}
}
