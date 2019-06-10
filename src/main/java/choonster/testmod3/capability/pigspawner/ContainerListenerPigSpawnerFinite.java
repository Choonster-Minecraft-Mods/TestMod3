package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import choonster.testmod3.network.capability.pigspawner.MessageBulkUpdateContainerPigSpawnerFinites;
import choonster.testmod3.network.capability.pigspawner.MessageUpdateContainerPigSpawnerFinite;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;

/**
 * Syncs the {@link IPigSpawnerFinite} capability for items in {@link Container}s.
 *
 * @author Choonster
 */
public class ContainerListenerPigSpawnerFinite extends CapabilityContainerListener<IPigSpawner> {

	public ContainerListenerPigSpawnerFinite(final EntityPlayerMP player) {
		super(player, CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY, CapabilityPigSpawner.DEFAULT_FACING);
	}

	/**
	 * Create an instance of the bulk update message.
	 *
	 * @param windowID The window ID of the Container
	 * @param items    The items list
	 * @return The bulk update message
	 */
	@Override
	protected MessageBulkUpdateContainerCapability<IPigSpawner, ?> createBulkUpdateMessage(final int windowID, final NonNullList<ItemStack> items) {
		return new MessageBulkUpdateContainerPigSpawnerFinites(CapabilityPigSpawner.DEFAULT_FACING, windowID, items);
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
	protected MessageUpdateContainerCapability<IPigSpawner, ?> createSingleUpdateMessage(final int windowID, final int slotNumber, final IPigSpawner pigSpawner) {
		return new MessageUpdateContainerPigSpawnerFinite(CapabilityPigSpawner.DEFAULT_FACING, windowID, slotNumber, pigSpawner);
	}
}
