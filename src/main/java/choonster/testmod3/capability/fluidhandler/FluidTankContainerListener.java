package choonster.testmod3.capability.fluidhandler;

import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.network.capability.fluidhandler.UpdateMenuFluidTankMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Syncs the {@link FluidTank} capability for items in {@link AbstractContainerMenu}s.
 *
 * @author Choonster
 */
public class FluidTankContainerListener extends CapabilityContainerListener<IFluidHandlerItem> {

	public FluidTankContainerListener(final ServerPlayer player) {
		super(player, ForgeCapabilities.FLUID_HANDLER_ITEM, null);
	}

	@Override
	protected boolean shouldSyncItem(final ItemStack stack) {
		return stack.getItem() == ModBlocks.FLUID_TANK.get().asItem();
	}

	@Override
	protected UpdateMenuFluidTankMessage createUpdateMessage(final int containerID, final int stateID, final int slotNumber, final IFluidHandlerItem fluidHandlerItem) {
		return new UpdateMenuFluidTankMessage(null, containerID, stateID, slotNumber, fluidHandlerItem);
	}
}
