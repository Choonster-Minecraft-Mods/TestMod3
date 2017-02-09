package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.hiddenblock.MessageBulkUpdateContainerHiddenBlockRevealers;
import choonster.testmod3.network.capability.hiddenblock.MessageUpdateContainerHiddenBlockRevealer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Syncs the {@link IHiddenBlockRevealer} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class ContainerListenerHiddenBlockRevealer extends CapabilityContainerListener<IHiddenBlockRevealer> {

	public ContainerListenerHiddenBlockRevealer(final EntityPlayerMP player) {
		super(player, CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY, CapabilityHiddenBlockRevealer.DEFAULT_FACING);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected MessageBulkUpdateContainerHiddenBlockRevealers createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new MessageBulkUpdateContainerHiddenBlockRevealers(windowID, items);
	}

	/**
	 * Create an instance of the single update message.
	 *
	 * @param windowID            The window ID of the Container
	 * @param slotNumber          The slot's index in the Container
	 * @param hiddenBlockRevealer The capability handler instance
	 * @return The single update message
	 */
	@Override
	protected MessageUpdateContainerHiddenBlockRevealer createSingleUpdateMessage(final int windowID, final int slotNumber, final IHiddenBlockRevealer hiddenBlockRevealer) {
		return new MessageUpdateContainerHiddenBlockRevealer(windowID, slotNumber, hiddenBlockRevealer);
	}
}
