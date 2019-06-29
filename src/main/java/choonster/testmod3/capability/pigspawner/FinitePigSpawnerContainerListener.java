package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.BulkUpdateContainerCapabilityMessage;
import choonster.testmod3.network.capability.UpdateContainerCapabilityMessage;
import choonster.testmod3.network.capability.pigspawner.BulkUpdateContainerPigSpawnerFinitesMessage;
import choonster.testmod3.network.capability.pigspawner.UpdateContainerPigSpawnerFiniteMessage;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Syncs the {@link IPigSpawnerFinite} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class FinitePigSpawnerContainerListener extends CapabilityContainerListener<IPigSpawner> {

	public FinitePigSpawnerContainerListener(final ServerPlayerEntity player) {
		super(player, PigSpawnerCapability.PIG_SPAWNER_CAPABILITY, PigSpawnerCapability.DEFAULT_FACING);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected BulkUpdateContainerCapabilityMessage<IPigSpawner, ?> createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new BulkUpdateContainerPigSpawnerFinitesMessage(PigSpawnerCapability.DEFAULT_FACING, windowID, items);
	}

	/**
	 * Create an instance of the single update message.
	 *
	 * @param windowID   The window ID of the Container
	 * @param slotNumber The slot's index in the Container
	 * @param pigSpawner The capability handler instance
	 * @return The single update message
	 */
	@Override
	protected UpdateContainerCapabilityMessage<IPigSpawner, ?> createSingleUpdateMessage(final int windowID, final int slotNumber, final IPigSpawner pigSpawner) {
		return new UpdateContainerPigSpawnerFiniteMessage(PigSpawnerCapability.DEFAULT_FACING, windowID, slotNumber, pigSpawner);
	}
}
