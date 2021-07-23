package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import choonster.testmod3.network.capability.UpdateMenuCapabilityMessage;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.fmllegacy.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IPigSpawnerFinite} for a single slot of an {@link AbstractContainerMenu}.
 *
 * @author Choonster
 */
public class UpdateMenuPigSpawnerFiniteMessage extends UpdateMenuCapabilityMessage<IPigSpawner, Integer> {
	public UpdateMenuPigSpawnerFiniteMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int slotNumber,
			final IPigSpawner pigSpawner
	) {
		super(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				facing, containerID, slotNumber, pigSpawner,
				PigSpawnerFunctions::convertFinitePigSpawnerToNumPigs
		);
	}

	private UpdateMenuPigSpawnerFiniteMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int slotNumber,
			final int numPigs
	) {
		super(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				facing, containerID, slotNumber, numPigs
		);
	}

	public static UpdateMenuPigSpawnerFiniteMessage decode(final FriendlyByteBuf buffer) {
		return UpdateMenuCapabilityMessage.<IPigSpawner, Integer, UpdateMenuPigSpawnerFiniteMessage>decode(
				buffer,
				PigSpawnerFunctions::decodeNumPigs,
				UpdateMenuPigSpawnerFiniteMessage::new
		);
	}

	public static void encode(final UpdateMenuPigSpawnerFiniteMessage message, final FriendlyByteBuf buffer) {
		UpdateMenuCapabilityMessage.encode(
				message,
				buffer,
				PigSpawnerFunctions::encodeNumPigs
		);
	}

	public static void handle(final UpdateMenuPigSpawnerFiniteMessage message, final Supplier<NetworkEvent.Context> ctx) {
		UpdateMenuCapabilityMessage.handle(
				message,
				ctx,
				PigSpawnerFunctions::applyNumPigsToFinitePigSpawner
		);
	}
}
