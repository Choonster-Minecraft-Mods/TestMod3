package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.CapabilityContainerListener;
import choonster.testmod3.network.capability.UpdateMenuCapabilityMessage;
import choonster.testmod3.network.capability.pigspawner.UpdateMenuPigSpawnerFiniteMessage;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * Syncs the {@link IPigSpawnerFinite} capability for items in {@link AbstractContainerMenu}s.
 *
 * @author Choonster
 */
public class FinitePigSpawnerContainerListener extends CapabilityContainerListener<IPigSpawner> {

	public FinitePigSpawnerContainerListener(final ServerPlayer player) {
		super(player, PigSpawnerCapability.PIG_SPAWNER_CAPABILITY, PigSpawnerCapability.DEFAULT_FACING);
	}

	@Override
	protected UpdateMenuCapabilityMessage<IPigSpawner, ?> createSingleUpdateMessage(final int containerID, final int slotNumber, final IPigSpawner pigSpawner) {
		return new UpdateMenuPigSpawnerFiniteMessage(PigSpawnerCapability.DEFAULT_FACING, containerID, slotNumber, pigSpawner);
	}
}
