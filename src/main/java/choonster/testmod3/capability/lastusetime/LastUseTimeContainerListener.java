package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.lastusetime.BulkUpdateContainerLastUseTimesMessage;
import choonster.testmod3.network.capability.lastusetime.UpdateContainerLastUseTimeMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Syncs the {@link IHiddenBlockRevealer} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class LastUseTimeContainerListener extends CapabilityContainerListener<ILastUseTime> {

	public LastUseTimeContainerListener(final ServerPlayerEntity player) {
		super(player, LastUseTimeCapability.LAST_USE_TIME_CAPABILITY, LastUseTimeCapability.DEFAULT_FACING);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected BulkUpdateContainerLastUseTimesMessage createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new BulkUpdateContainerLastUseTimesMessage(LastUseTimeCapability.DEFAULT_FACING, windowID, items);
	}

	/**
	 * Create an instance of the single update message.
	 *
	 * @param windowID    The window ID of the Container
	 * @param slotNumber  The slot's index in the Container
	 * @param lastUseTime The capability handler instance
	 * @return The single update message
	 */
	@Override
	protected UpdateContainerLastUseTimeMessage createSingleUpdateMessage(final int windowID, final int slotNumber, final ILastUseTime lastUseTime) {
		return new UpdateContainerLastUseTimeMessage(LastUseTimeCapability.DEFAULT_FACING, windowID, slotNumber, lastUseTime);
	}
}
