package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import choonster.testmod3.network.capability.UpdateContainerCapabilityMessage;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IPigSpawnerFinite} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class UpdateContainerPigSpawnerFiniteMessage extends UpdateContainerCapabilityMessage<IPigSpawner, Integer> {
	public UpdateContainerPigSpawnerFiniteMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final IPigSpawner pigSpawner
	) {
		super(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				facing, windowID, slotNumber, pigSpawner,
				PigSpawnerFunctions::convertFinitePigSpawnerToNumPigs
		);
	}

	private UpdateContainerPigSpawnerFiniteMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final int numPigs
	) {
		super(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				facing, windowID, slotNumber, numPigs
		);
	}

	public static UpdateContainerPigSpawnerFiniteMessage decode(final PacketBuffer buffer) {
		return UpdateContainerCapabilityMessage.<IPigSpawner, Integer, UpdateContainerPigSpawnerFiniteMessage>decode(
				buffer,
				PigSpawnerFunctions::decodeNumPigs,
				UpdateContainerPigSpawnerFiniteMessage::new
		);
	}

	public static void encode(final UpdateContainerPigSpawnerFiniteMessage message, final PacketBuffer buffer) {
		UpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				PigSpawnerFunctions::encodeNumPigs
		);
	}

	public static void handle(final UpdateContainerPigSpawnerFiniteMessage message, final Supplier<NetworkEvent.Context> ctx) {
		UpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				PigSpawnerFunctions::applyNumPigsToFinitePigSpawner
		);
	}
}
