package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
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
 * Updates the {@link IHiddenBlockRevealer} for each slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageBulkUpdateContainerHiddenBlockRevealers extends MessageBulkUpdateContainerCapability<IHiddenBlockRevealer, Boolean> {
	public MessageBulkUpdateContainerHiddenBlockRevealers(
			@Nullable final EnumFacing facing,
			final int windowID,
			final NonNullList<ItemStack> items
	) {
		super(
				CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, items,
				HiddenBlockRevealerFunctions::convertHiddenBlockRevealerToRevealHiddenBlocksFlag
		);
	}

	private MessageBulkUpdateContainerHiddenBlockRevealers(
			@Nullable final EnumFacing facing,
			final int windowID,
			final Int2ObjectMap<Boolean> capabilityData
	) {
		super(
				CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, capabilityData
		);
	}

	public static MessageBulkUpdateContainerHiddenBlockRevealers decode(final PacketBuffer buffer) {
		return MessageBulkUpdateContainerCapability.decode(
				buffer,
				HiddenBlockRevealerFunctions::decodeRevealHiddenBlocksFlag,
				MessageBulkUpdateContainerHiddenBlockRevealers::new
		);
	}

	public static void encode(final MessageBulkUpdateContainerHiddenBlockRevealers message, final PacketBuffer buffer) {
		MessageBulkUpdateContainerCapability.encode(
				message,
				buffer,
				HiddenBlockRevealerFunctions::encodeRevealHiddenBlocksFlag
		);
	}

	public static void handle(final MessageBulkUpdateContainerHiddenBlockRevealers message, final Supplier<NetworkEvent.Context> ctx) {
		MessageBulkUpdateContainerCapability.handle(
				message,
				ctx,
				HiddenBlockRevealerFunctions::applyRevealHiddenBlocksFlagToHiddenBlocksRevealer
		);
	}
}
