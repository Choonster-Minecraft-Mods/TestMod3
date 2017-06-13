package choonster.testmod3.capability;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

/**
 * Syncs the capability handler instances for items in {@link Container}s.
 *
 * @param <HANDLER> The capability handler type to sync
 * @author Choonster
 */
public abstract class CapabilityContainerListener<HANDLER> implements IContainerListener {
	/**
	 * The player.
	 */
	private final EntityPlayerMP player;

	/**
	 * The {@link Capability} instance to update.
	 */
	private final Capability<HANDLER> capability;

	/**
	 * The {@link EnumFacing} to get the capability handler from.
	 */
	@Nullable
	private final EnumFacing facing;

	public CapabilityContainerListener(final EntityPlayerMP player, final Capability<HANDLER> capability, @Nullable final EnumFacing facing) {
		this.player = player;
		this.capability = capability;
		this.facing = facing;
	}

	@Override
	public final void sendAllContents(final Container containerToSend, final NonNullList<ItemStack> itemsList) {
		// Filter out any items from the list that shouldn't be synced
		final NonNullList<ItemStack> syncableItemsList = NonNullList.withSize(itemsList.size(), ItemStack.EMPTY);
		for (int index = 0; index < syncableItemsList.size(); index++) {
			final ItemStack stack = syncableItemsList.get(index);
			if (shouldSyncItem(stack)) {
				syncableItemsList.set(index, stack);
			} else {
				syncableItemsList.set(index, ItemStack.EMPTY);
			}
		}

		final MessageBulkUpdateContainerCapability<HANDLER, ?> message = createBulkUpdateMessage(containerToSend.windowId, syncableItemsList);
		if (message.hasData()) { // Don't send the message if there's nothing to update
			TestMod3.network.sendTo(message, player);
		}
	}

	@Override
	public final void sendSlotContents(final Container containerToSend, final int slotInd, final ItemStack stack) {
		if (!shouldSyncItem(stack)) return;

		final HANDLER handler = CapabilityUtils.getCapability(stack, capability, facing);
		if (handler == null) return;

		final MessageUpdateContainerCapability<HANDLER, ?> message = createSingleUpdateMessage(containerToSend.windowId, slotInd, handler);
		if (message.hasData()) { // Don't send the message if there's nothing to update
			TestMod3.network.sendTo(message, player);
		}
	}

	@Override
	public final void sendWindowProperty(final Container containerIn, final int varToUpdate, final int newValue) {
		// No-op
	}

	@Override
	public final void sendAllWindowProperties(final Container containerIn, final IInventory inventory) {
		// No-op
	}

	/**
	 * Should the {@link ItemStack}'s capability data be synced?
	 *
	 * @param stack The item
	 * @return Should the capability data be synced?
	 */
	protected boolean shouldSyncItem(final ItemStack stack) {
		return true;
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	protected abstract MessageBulkUpdateContainerCapability<HANDLER, ?> createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items);

	/**
	 * Create an instance of the single update message.
	 *
	 * @param windowID   The window ID of the Container
	 * @param slotNumber The slot's index in the Container
	 * @param handler    The capability handler instance
	 * @return The single update message
	 */
	protected abstract MessageUpdateContainerCapability<HANDLER, ?> createSingleUpdateMessage(final int windowID, final int slotNumber, final HANDLER handler);
}
