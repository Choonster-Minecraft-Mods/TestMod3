package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.CapabilityHiddenBlockRevealer;
import choonster.testmod3.network.capability.MessageUpdateContainerCapability;
import net.minecraft.inventory.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.network.NetworkEvent;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Updates the {@link IHiddenBlockRevealer} for a single slot of a {@link Container}.
 *
 * @author Choonster
 */
public class MessageUpdateContainerHiddenBlockRevealer extends MessageUpdateContainerCapability<IHiddenBlockRevealer, Boolean> {
	public MessageUpdateContainerHiddenBlockRevealer(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final IHiddenBlockRevealer hiddenBlockRevealer
	) {
		super(
				CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, slotNumber, hiddenBlockRevealer,
				HiddenBlockRevealerFunctions::convertHiddenBlockRevealerToRevealHiddenBlocksFlag
		);
	}

	private MessageUpdateContainerHiddenBlockRevealer(
			@Nullable final EnumFacing facing,
			final int windowID,
			final int slotNumber,
			final boolean revealHiddenBlocks
	) {
		super(
				CapabilityHiddenBlockRevealer.HIDDEN_BLOCK_REVEALER_CAPABILITY,
				facing, windowID, slotNumber, revealHiddenBlocks
		);
	}

	public static MessageUpdateContainerHiddenBlockRevealer decode(final PacketBuffer buffer) {
		return MessageUpdateContainerCapability.decode(
				buffer,
				HiddenBlockRevealerFunctions::decodeRevealHiddenBlocksFlag,
				MessageUpdateContainerHiddenBlockRevealer::new
		);
	}

	public static void encode(final MessageUpdateContainerHiddenBlockRevealer message, final PacketBuffer buffer) {
		MessageUpdateContainerCapability.encode(
				message,
				buffer,
				HiddenBlockRevealerFunctions::encodeRevealHiddenBlocksFlag
		);
	}

	public static void handle(final MessageUpdateContainerHiddenBlockRevealer message, final Supplier<NetworkEvent.Context> ctx) {
		MessageUpdateContainerCapability.handle(
				message,
				ctx,
				HiddenBlockRevealerFunctions::applyRevealHiddenBlocksFlagToHiddenBlocksRevealer
		);
	}
}
