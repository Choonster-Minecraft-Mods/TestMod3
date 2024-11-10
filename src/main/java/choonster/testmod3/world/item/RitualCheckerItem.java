package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.Optional;

/**
 * An item that checks if the player is standing on obsidian and surrounded by the following pattern of blocks:
 * <p>
 * A A A A A
 * A R R R A
 * A R P R A
 * A R R R A
 * A A A A A
 * <p>
 * Where A is air, R is redstone and P is the player.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/41088-1102-checking-blocks-around-player/
 *
 * @author Choonster
 */
public class RitualCheckerItem extends Item {
	public RitualCheckerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if (!level.isClientSide && player instanceof final ServerPlayer serverPlayer) {
			final Component textComponent;

			final var invalidPosition = checkRitual(serverPlayer);
			if (invalidPosition.isPresent()) {
				final var pos = invalidPosition.get();
				textComponent = Component.translatable(TestMod3Lang.MESSAGE_RITUAL_CHECKER_FAILURE.getTranslationKey(), pos.getX(), pos.getY(), pos.getZ());
				textComponent.getStyle().withColor(ChatFormatting.RED);
			} else {
				textComponent = Component.translatable(TestMod3Lang.MESSAGE_RITUAL_CHECKER_SUCCESS.getTranslationKey());
				textComponent.getStyle().withColor(ChatFormatting.GREEN);
			}

			serverPlayer.sendSystemMessage(textComponent);
		}

		return InteractionResult.SUCCESS;
	}

	/**
	 * Is the player surrounded by the correct pattern of blocks?
	 *
	 * @param player The command player
	 * @return The first invalid position, if any.
	 */
	private Optional<BlockPos> checkRitual(final Player player) {
		final var world = player.getCommandSenderWorld();
		final var playerPos = player.blockPosition();

		// The block under the player must be obsidian
		if (!(world.getBlockState(playerPos.below()).getBlock() == Blocks.OBSIDIAN)) {
			return Optional.of(playerPos.below());
		}

		// Iterate from -2,0,-2 to +2,0,+2
		for (var x = -2; x <= 2; x++) {
			for (var z = -2; z <= 2; z++) {
				// If this is the player's position, skip it
				if (x == 0 && z == 0) {
					continue;
				}

				final var pos = playerPos.offset(x, 0, z);
				final var block = world.getBlockState(pos).getBlock();

				if (Math.abs(x) == 2 || Math.abs(z) == 2) { // If this is the outer layer, the block must be air
					if (block != Blocks.AIR) {
						return Optional.of(pos);
					}

				} else { // If this is the inner layer, the block must be redstone
					if (block != Blocks.REDSTONE_WIRE) {
						return Optional.of(pos);
					}

				}
			}
		}

		// All blocks are correct, the ritual is valid
		return Optional.empty();
	}
}
