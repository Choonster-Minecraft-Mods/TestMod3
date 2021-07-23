package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.hiddenblock.UpdateMenuHiddenBlockRevealerMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Syncs the {@link IHiddenBlockRevealer} capability for items in {@link AbstractContainerMenu}s.
 *
 * @author Choonster
 */
public class HiddenBlockRevealerContainerListener extends CapabilityContainerListener<IHiddenBlockRevealer> {

	public HiddenBlockRevealerContainerListener(final ServerPlayer player) {
		super(player, HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY, HiddenBlockRevealerCapability.DEFAULT_FACING);
	}

	@Override
	protected UpdateMenuHiddenBlockRevealerMessage createSingleUpdateMessage(final int containerID, final int slotNumber, final IHiddenBlockRevealer hiddenBlockRevealer) {
		return new UpdateMenuHiddenBlockRevealerMessage(HiddenBlockRevealerCapability.DEFAULT_FACING, containerID, slotNumber, hiddenBlockRevealer);
	}
}
