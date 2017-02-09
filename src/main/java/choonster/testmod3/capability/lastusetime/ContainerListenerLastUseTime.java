package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.lastusetime.MessageBulkUpdateContainerLastUseTimes;
import choonster.testmod3.network.capability.lastusetime.MessageUpdateContainerLastUseTime;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Syncs the {@link IHiddenBlockRevealer} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class ContainerListenerLastUseTime extends CapabilityContainerListener<ILastUseTime> {

	public ContainerListenerLastUseTime(final EntityPlayerMP player) {
		super(player, CapabilityLastUseTime.LAST_USE_TIME_CAPABILITY, CapabilityLastUseTime.DEFAULT_FACING);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected MessageBulkUpdateContainerLastUseTimes createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new MessageBulkUpdateContainerLastUseTimes(windowID, items);
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
	protected MessageUpdateContainerLastUseTime createSingleUpdateMessage(final int windowID, final int slotNumber, final ILastUseTime lastUseTime) {
		return new MessageUpdateContainerLastUseTime(windowID, slotNumber, lastUseTime);
	}
}
