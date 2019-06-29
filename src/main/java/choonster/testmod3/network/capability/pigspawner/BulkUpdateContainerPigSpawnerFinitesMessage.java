package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.PigSpawnerCapability;
import choonster.testmod3.network.capability.BulkUpdateContainerCapabilityMessage;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IPigSpawnerFinite} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class BulkUpdateContainerPigSpawnerFinitesMessage extends BulkUpdateContainerCapabilityMessage<IPigSpawner, Integer> {
	public BulkUpdateContainerPigSpawnerFinitesMessage(
			@Nullable final Direction facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				facing, windowID, items,
				PigSpawnerFunctions::convertFinitePigSpawnerToNumPigs
		);
	}

	private BulkUpdateContainerPigSpawnerFinitesMessage(
			@Nullable final Direction facing,
			final int windowID,
			final Int2ObjectMap<Integer> capabilityData
	) {
		super(
				PigSpawnerCapability.PIG_SPAWNER_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static BulkUpdateContainerPigSpawnerFinitesMessage decode(final PacketBuffer buffer) {
		return BulkUpdateContainerCapabilityMessage.<IPigSpawner, Integer, BulkUpdateContainerPigSpawnerFinitesMessage>decode(
				buffer,
				PigSpawnerFunctions::decodeNumPigs,
				BulkUpdateContainerPigSpawnerFinitesMessage::new
		);
	}

	public static void encode(final BulkUpdateContainerPigSpawnerFinitesMessage message, final PacketBuffer buffer) {
		BulkUpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				PigSpawnerFunctions::encodeNumPigs
		);
	}

	public static void handle(final BulkUpdateContainerPigSpawnerFinitesMessage message, final Supplier<NetworkEvent.Context> ctx) {
		BulkUpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				PigSpawnerFunctions::applyNumPigsToFinitePigSpawner
		);
	}
}
