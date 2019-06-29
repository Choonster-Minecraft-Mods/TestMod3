package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import choonster.testmod3.network.capability.UpdateContainerCapabilityMessage;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IHiddenBlockRevealer} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class UpdateContainerHiddenBlockRevealerMessage extends UpdateContainerCapabilityMessage<IHiddenBlockRevealer, Boolean> {
	public UpdateContainerHiddenBlockRevealerMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final IHiddenBlockRevealer hiddenBlockRevealer
	) {
		super(
				HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, slotNumber, hiddenBlockRevealer,
				HiddenBlockRevealerFunctions::convertHiddenBlockRevealerToRevealHiddenBlocksFlag
		);
	}

	private UpdateContainerHiddenBlockRevealerMessage(
			@Nullable final Direction facing,
			final int windowID,
			final int slotNumber,
			final boolean revealHiddenBlocks
	) {
		super(
				HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, slotNumber, revealHiddenBlocks
		);
	}

	public static UpdateContainerHiddenBlockRevealerMessage decode(final PacketBuffer buffer) {
		return UpdateContainerCapabilityMessage.<IHiddenBlockRevealer, Boolean, UpdateContainerHiddenBlockRevealerMessage>decode(
				buffer,
				HiddenBlockRevealerFunctions::decodeRevealHiddenBlocksFlag,
				UpdateContainerHiddenBlockRevealerMessage::new
		);
	}

	public static void encode(final UpdateContainerHiddenBlockRevealerMessage message, final PacketBuffer buffer) {
		UpdateContainerCapabilityMessage.encode(
				message,
				buffer,
				HiddenBlockRevealerFunctions::encodeRevealHiddenBlocksFlag
		);
	}

	public static void handle(final UpdateContainerHiddenBlockRevealerMessage message, final Supplier<NetworkEvent.Context> ctx) {
		UpdateContainerCapabilityMessage.handle(
				message,
				ctx,
				HiddenBlockRevealerFunctions::applyRevealHiddenBlocksFlagToHiddenBlocksRevealer
		);
	}
}
