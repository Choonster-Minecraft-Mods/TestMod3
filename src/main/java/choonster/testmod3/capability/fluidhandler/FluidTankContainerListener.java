package choonster.testmod3.capability.fluidhandler;

import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.network.capability.fluidhandler.BulkUpdateContainerFluidTanksMessage;
import choonster.testmod3.network.capability.fluidhandler.UpdateContainerFluidTankMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Syncs the {@link FluidTank} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class FluidTankContainerListener extends CapabilityContainerListener<IFluidHandlerItem> {

	public FluidTankContainerListener(final ServerPlayerEntity player) {
		super(player, CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null);
	}

	/**
	 * Should the {@link ItemStack}'s capability data be synced?
	 *
	 * @param stack The item
	 * @return Should the capability data be synced?
	 */
	@Override
	protected boolean shouldSyncItem(final ItemStack stack) {
		return stack.getItem() == ModBlocks.FLUID_TANK.get().asItem();
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected BulkUpdateContainerFluidTanksMessage createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new BulkUpdateContainerFluidTanksMessage(null, windowID, items);
	}

	/**
	 * Create an instance of the single update message.
	 *
	 * @param windowID         The window ID of the Container
	 * @param slotNumber       The slot's index in the Container
	 * @param fluidHandlerItem The capability handler instance
	 * @return The single update message
	 */
	@Override
	protected UpdateContainerFluidTankMessage createSingleUpdateMessage(final int windowID, final int slotNumber, final IFluidHandlerItem fluidHandlerItem) {
		return new UpdateContainerFluidTankMessage(null, windowID, slotNumber, fluidHandlerItem);
	}
}
