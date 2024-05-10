package choonster.testmod3.network.capability;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import net.minecraft.core.Direction;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Updates the {@link IPigSpawnerFinite} for a single slot of an {@link AbstractContainerMenu}.
 *
 * @author Choonster
 */
public record UpdateMenuPigSpawnerFiniteMessage(
		UpdateMenuCapabilityData<IPigSpawner, Integer> data
) implements UpdateMenuCapabilityData.UpdateMenuCapabilityDataMessage<IPigSpawner, Integer> {
	public static final StreamCodec<RegistryFriendlyByteBuf, UpdateMenuPigSpawnerFiniteMessage> STREAM_CODEC =
			UpdateMenuCapabilityData.<IPigSpawner, Integer, UpdateMenuPigSpawnerFiniteMessage>streamCodec(
					PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
					ByteBufCodecs.VAR_INT.cast(),
					UpdateMenuPigSpawnerFiniteMessage::new
			);

	public UpdateMenuPigSpawnerFiniteMessage(
			@Nullable final Direction direction,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final IPigSpawner pigSpawner
	) {
		this(new UpdateMenuCapabilityData<>(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				direction, containerID, stateID, slotNumber, pigSpawner,
				UpdateMenuPigSpawnerFiniteMessage::convertFinitePigSpawnerToNumPigs
		));
	}

	public static void handle(final UpdateMenuPigSpawnerFiniteMessage message, final CustomPayloadEvent.Context ctx) {
		message.data.handle(UpdateMenuPigSpawnerFiniteMessage::applyNumPigsToFinitePigSpawner);
	}

	@Nullable
	static Integer convertFinitePigSpawnerToNumPigs(final IPigSpawner pigSpawner) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			return ((IPigSpawnerFinite) pigSpawner).getNumPigs();
		} else {
			return null;
		}
	}

	static void applyNumPigsToFinitePigSpawner(final IPigSpawner pigSpawner, final int numPigs) {
		if (pigSpawner instanceof IPigSpawnerFinite) {
			((IPigSpawnerFinite) pigSpawner).setNumPigs(numPigs);
		}
	}
}
