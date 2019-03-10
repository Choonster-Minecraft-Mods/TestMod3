package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IPigSpawnerFinite} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageUpdateContainerPigSpawnerFinite extends MessageUpdateContainerCapability<IPigSpawner, Integer> {
	public MessageUpdateContainerPigSpawnerFinite(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final IPigSpawner pigSpawner
	) {
		super(
				CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY,
				facing, windowID, slotNumber, pigSpawner,
				PigSpawnerFunctions::convertFinitePigSpawnerToNumPigs
		);
	}

	private MessageUpdateContainerPigSpawnerFinite(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final int numPigs
	) {
		super(
				CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY,
				facing, windowID, slotNumber, numPigs
		);
	}

	public static MessageUpdateContainerPigSpawnerFinite decode(final PacketBuffer buffer) {
		return MessageUpdateContainerCapability.decode(
				buffer,
				PigSpawnerFunctions::decodeNumPigs,
				MessageUpdateContainerPigSpawnerFinite::new
		);
	}

	public static void encode(final MessageUpdateContainerPigSpawnerFinite message, final PacketBuffer buffer) {
		MessageUpdateContainerCapability.encode(
				message,
				buffer,
				PigSpawnerFunctions::encodeNumPigs
		);
	}

	public static void handle(final MessageUpdateContainerPigSpawnerFinite message, final Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateContainerCapability.handle(
				message,
				ctx,
				PigSpawnerFunctions::applyNumPigsToFinitePigSpawner
		);
	}
}
