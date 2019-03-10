package choonster.testmod3.network.capability.pigspawner;

import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerFinite;
import choonster.testmod3.capability.pigspawner.CapabilityPigSpawner;
import choonster.testmod3.network.capability.MessageBulkUpdateContainerCapability;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IPigSpawnerFinite} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerPigSpawnerFinites extends MessageBulkUpdateContainerCapability<IPigSpawner, Integer> {
	public MessageBulkUpdateContainerPigSpawnerFinites(
			@Nullable final EnumFacing facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY,
				facing, windowID, items,
				PigSpawnerFunctions::convertFinitePigSpawnerToNumPigs
		);
	}

	private MessageBulkUpdateContainerPigSpawnerFinites(
			@Nullable final EnumFacing facing,
			final int windowID,
			final Int2ObjectMap<Integer> capabilityData
	) {
		super(
				CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static MessageBulkUpdateContainerPigSpawnerFinites decode(final PacketBuffer buffer) {
		return MessageBulkUpdateContainerCapability.decode(
				buffer,
				PigSpawnerFunctions::decodeNumPigs,
				MessageBulkUpdateContainerPigSpawnerFinites::new
		);
	}

	public static void encode(final MessageBulkUpdateContainerPigSpawnerFinites message, final PacketBuffer buffer) {
		MessageBulkUpdateContainerCapability.encode(
				message,
				buffer,
				PigSpawnerFunctions::encodeNumPigs
		);
	}

	public static void handle(final MessageBulkUpdateContainerPigSpawnerFinites message, final Supplier<NetworkEvent.Context> ctx) {
		MessageBulkUpdateContainerCapability.handle(
				message,
				ctx,
				PigSpawnerFunctions::applyNumPigsToFinitePigSpawner
		);
	}
}
