package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.hiddenblock.BulkUpdateContainerHiddenBlockRevealersMessage;
import choonster.testmod3.network.capability.hiddenblock.UpdateContainerHiddenBlockRevealerMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Syncs the {@link IHiddenBlockRevealer} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class HiddenBlockRevealerContainerListener extends CapabilityContainerListener<IHiddenBlockRevealer> {

	public HiddenBlockRevealerContainerListener(final ServerPlayerEntity player) {
		super(player, HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY, HiddenBlockRevealerCapability.DEFAULT_FACING);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected BulkUpdateContainerHiddenBlockRevealersMessage createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new BulkUpdateContainerHiddenBlockRevealersMessage(HiddenBlockRevealerCapability.DEFAULT_FACING, windowID, items);
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
	protected UpdateContainerHiddenBlockRevealerMessage createSingleUpdateMessage(final int windowID, final int slotNumber, final IHiddenBlockRevealer hiddenBlockRevealer) {
		return new UpdateContainerHiddenBlockRevealerMessage(HiddenBlockRevealerCapability.DEFAULT_FACING, windowID, slotNumber, hiddenBlockRevealer);
	}
}
