package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

/**
 * An item that teleports the player to their spawn position when right-clicked.
 *
 * @author Choonster
 */
public class RespawnerItem extends Item {
	public RespawnerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(final Level level, final Player player, final InteractionHand hand) {
		if (level.isClientSide() || !(level instanceof final ServerLevel serverLevel)) {
			return InteractionResult.SUCCESS;
		}

		final var serverPlayer = (ServerPlayer) player;
		final var server = serverLevel.getServer();

		final var respawnConfig = serverPlayer.getRespawnConfig();
		final var respawnPosition = respawnConfig != null ? respawnConfig.respawnData().pos() : null;

		final var respawnLevel = respawnConfig != null
				? server.getLevel(respawnConfig.respawnData().dimension())
				: null;

		if (respawnPosition == null || respawnLevel == null) {
			serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_RESPAWNER_NO_SPAWN_LOCATION.getTranslationKey()));
			return InteractionResult.FAIL;
		}

		final var respawnTransition = serverPlayer.findRespawnPositionAndUseSpawnBlock(
				/* wonGame */ false,
				entity -> {
					if (entity instanceof final ServerPlayer newServerPlayer) {
						newServerPlayer.sendSystemMessage(
								Component.translatable(
										TestMod3Lang.MESSAGE_RESPAWNER_TELEPORTING.getTranslationKey(),
										entity.getX(),
										entity.getY(),
										entity.getZ(),
										entity.level().dimension()
								)
						);
					}
				}
		);

		serverPlayer.teleport(respawnTransition);

		return InteractionResult.CONSUME;
	}
}
