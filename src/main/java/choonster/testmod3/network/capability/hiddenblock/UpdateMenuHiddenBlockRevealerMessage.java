package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import choonster.testmod3.network.capability.UpdateMenuCapabilityMessage;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Updates the {@link IHiddenBlockRevealer} for a single slot of an {@link AbstractContainerMenu}.
 *
 * @author Choonster
 */
public class UpdateMenuHiddenBlockRevealerMessage extends UpdateMenuCapabilityMessage<IHiddenBlockRevealer, Boolean> {
	public UpdateMenuHiddenBlockRevealerMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final IHiddenBlockRevealer hiddenBlockRevealer
	) {
		super(
				HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, containerID, stateID, slotNumber, hiddenBlockRevealer,
				HiddenBlockRevealerFunctions::convertHiddenBlockRevealerToRevealHiddenBlocksFlag
		);
	}

	private UpdateMenuHiddenBlockRevealerMessage(
			@Nullable final Direction facing,
			final int containerID,
			final int stateID,
			final int slotNumber,
			final boolean revealHiddenBlocks
	) {
		super(
				HiddenBlockRevealerCapability.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, containerID, stateID, slotNumber, revealHiddenBlocks
		);
	}

	public static UpdateMenuHiddenBlockRevealerMessage decode(final FriendlyByteBuf buffer) {
		return UpdateMenuCapabilityMessage.<IHiddenBlockRevealer, Boolean, UpdateMenuHiddenBlockRevealerMessage>decode(
				buffer,
				HiddenBlockRevealerFunctions::decodeRevealHiddenBlocksFlag,
				UpdateMenuHiddenBlockRevealerMessage::new
		);
	}

	public static void encode(final UpdateMenuHiddenBlockRevealerMessage message, final FriendlyByteBuf buffer) {
		UpdateMenuCapabilityMessage.encode(
				message,
				buffer,
				HiddenBlockRevealerFunctions::encodeRevealHiddenBlocksFlag
		);
	}

	public static void handle(final UpdateMenuHiddenBlockRevealerMessage message, final CustomPayloadEvent.Context ctx) {
		UpdateMenuCapabilityMessage.handle(
				message,
				ctx,
				HiddenBlockRevealerFunctions::applyRevealHiddenBlocksFlagToHiddenBlocksRevealer
		);
	}
}
