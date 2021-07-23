package choonster.testmod3.network.capability.hiddenblock;

import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import net.minecraft.network.FriendlyByteBuf;

/**
 * Functions used by the {@link IHiddenBlockRevealer} capability update message.
 *
 * @author Choonster
 */
class HiddenBlockRevealerFunctions {
	static boolean convertHiddenBlockRevealerToRevealHiddenBlocksFlag(final IHiddenBlockRevealer hiddenBlockRevealer) {
		return hiddenBlockRevealer.revealHiddenBlocks();
	}

	static boolean decodeRevealHiddenBlocksFlag(final FriendlyByteBuf buffer) {
		return buffer.readBoolean();
	}

	static void encodeRevealHiddenBlocksFlag(final boolean revealHiddenBlocks, final FriendlyByteBuf buffer) {
		buffer.writeBoolean(revealHiddenBlocks);
	}

	static void applyRevealHiddenBlocksFlagToHiddenBlocksRevealer(final IHiddenBlockRevealer hiddenBlockRevealer, final boolean revealHiddenBlocks) {
		hiddenBlockRevealer.setRevealHiddenBlocks(revealHiddenBlocks);
	}
}
