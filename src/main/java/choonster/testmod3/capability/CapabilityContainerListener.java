package choonster.testmod3.capability;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.capability.UpdateMenuCapabilityMessage;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fmllegacy.network.PacketDistributor;

import javax.annotation.Nullable;

/**
 * Syncs the capability handler instances for items in {@link AbstractContainerMenu}s.
 *
 * @param <HANDLER> The capability handler type to sync
 * @author Choonster
 */
public abstract class CapabilityContainerListener<HANDLER> implements ContainerListener {
	/**
	 * The player.
	 */
	private final ServerPlayer player;

	/**
	 * The {@link Capability} instance to update.
	 */
	private final Capability<HANDLER> capability;

	/**
	 * The {@link Direction} to get the capability handler from.
	 */
	@Nullable
	private final Direction facing;

	public CapabilityContainerListener(final ServerPlayer player, final Capability<HANDLER> capability, @Nullable final Direction facing) {
		this.player = player;
		this.capability = capability;
		this.facing = facing;
	}

	@Override
	public final void slotChanged(final AbstractContainerMenu menu, final int slotNumber, final ItemStack stack) {
		if (!shouldSyncItem(stack)) return;

		stack.getCapability(capability, facing).ifPresent(handler -> {
			final UpdateMenuCapabilityMessage<HANDLER, ?> message = createSingleUpdateMessage(menu.containerId, slotNumber, handler);
			if (message.hasData()) { // Don't send the message if there's nothing to update
				TestMod3.network.send(PacketDistributor.PLAYER.with(() -> player), message);
			}
		});
	}

	@Override
	public void dataChanged(final AbstractContainerMenu menu, final int variable, final int newValue) {
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
	 * Create an instance of the single update message.
	 *
	 * @param containerID The ID of the menu
	 * @param slotNumber  The slot's index in the menu
	 * @param handler     The capability handler instance
	 * @return The single update message
	 */
	protected abstract UpdateMenuCapabilityMessage<HANDLER, ?> createSingleUpdateMessage(final int containerID, final int slotNumber, final HANDLER handler);
}
