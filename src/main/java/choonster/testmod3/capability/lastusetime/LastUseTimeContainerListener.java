package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.lastusetime.UpdateMenuLastUseTimeMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Syncs the {@link IHiddenBlockRevealer} capability for items in {@link AbstractContainerMenu}s.
 *
 * @author Choonster
 */
public class LastUseTimeContainerListener extends CapabilityContainerListener<ILastUseTime> {

	public LastUseTimeContainerListener(final ServerPlayer player) {
		super(player, LastUseTimeCapability.LAST_USE_TIME_CAPABILITY, LastUseTimeCapability.DEFAULT_FACING);
	}

	@Override
	protected UpdateMenuLastUseTimeMessage createSingleUpdateMessage(final int containerID, final int slotNumber, final ILastUseTime lastUseTime) {
		return new UpdateMenuLastUseTimeMessage(LastUseTimeCapability.DEFAULT_FACING, containerID, slotNumber, lastUseTime);
	}
}
