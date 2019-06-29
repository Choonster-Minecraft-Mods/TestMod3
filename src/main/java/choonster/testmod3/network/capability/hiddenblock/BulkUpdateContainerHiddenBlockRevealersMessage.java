package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
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
 * Updates the {@link IHiddenBlockRevealer} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class BulkUpdateContainerHiddenBlockRevealersMessage extends BulkUpdateContainerCapabilityMessage<IHiddenBlockRevealer, Boolean> {
	public BulkUpdateContainerHiddenBlockRevealersMessage(
			@Nullable final Direction facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, items,
				HiddenBlockRevealerFunctions::convertHiddenBlockRevealerToRevealHiddenBlocksFlag
		);
	}

	private BulkUpdateContainerHiddenBlockRevealersMessage(
			@Nullable final Direction facing,
			final int windowID,
			final Int2ObjectMap<Boolean> capabilityData
	) {
		super(
				HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static BulkUpdateContainerHiddenBlockRevealersMessage decode(final PacketBuffer buffer) {
		return BulkUpdateContainerCapabilityMessage.<IHiddenBlockRevealer, Boolean, BulkUpdateContainerHiddenBlockRevealersMessage>decode(
				buffer,
				HiddenBlockRevealerFunctions::decodeRevealHiddenBlocksFlag,
				BulkUpdateContainerHiddenBlockRevealersMessage::new
		);
	}

	public static void encode(final BulkUpdateContainerHiddenBlockRevealersMessage message, final PacketBuffer buffer) {
		BulkUpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				HiddenBlockRevealerFunctions::encodeRevealHiddenBlocksFlag
		);
	}

	public static void handle(final BulkUpdateContainerHiddenBlockRevealersMessage message, final Supplier<NetworkEvent.Context> ctx) {
		BulkUpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				HiddenBlockRevealerFunctions::applyRevealHiddenBlocksFlagToHiddenBlocksRevealer
		);
	}
}
