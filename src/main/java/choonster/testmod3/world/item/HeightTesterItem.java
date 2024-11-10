package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;

/**
 * When right-clicked, prints the value of {@link Level#getHeightmapPos(Heightmap.Types, BlockPos)} at the player's current position.
 *
 * @author Choonster
 */
public class HeightTesterItem extends Item {
	public HeightTesterItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level world, final Player player, final InteractionHand hand) {
		if (!world.isClientSide && player instanceof final ServerPlayer serverPlayer) {
			final var pos = serverPlayer.blockPosition();

			serverPlayer.sendSystemMessage(
					Component.translatable(
							TestMod3Lang.MESSAGE_HEIGHT_TESTER_HEIGHT.getTranslationKey(),
							pos.getX(),
							pos.getZ(),
							world.getHeightmapPos(Heightmap.Types.WORLD_SURFACE, pos).getY()
					)
			);
		}

		return InteractionResult.SUCCESS;
	}
}
