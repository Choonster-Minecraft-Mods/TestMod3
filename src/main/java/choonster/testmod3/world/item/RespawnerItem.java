package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
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

	@SuppressWarnings("resource")
	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final var serverPlayer = (ServerPlayer) player;

			final var respawnPosition = serverPlayer.getRespawnPosition();
			final var respawnLevel = serverPlayer.server.getLevel(serverPlayer.getRespawnDimension());
			final var respawnForced = serverPlayer.isRespawnForced();
			final var respawnAngle = serverPlayer.getRespawnAngle();

			if (respawnPosition == null || respawnLevel == null) {
				serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_RESPAWNER_NO_SPAWN_LOCATION.getTranslationKey()));
				return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
			}

			if (respawnLevel != serverPlayer.level()) {
				player.changeDimension(respawnLevel);
			}

			return Player.findRespawnPositionAndUseSpawnBlock(respawnLevel, respawnPosition, respawnAngle, respawnForced, /* endConquered */false)
					.map(spawnLocation -> {
						serverPlayer.moveTo(spawnLocation.x + 0.5, spawnLocation.y() + 0.1, spawnLocation.z() + 0.5, 0, 0);
						serverPlayer.connection.teleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());

						while (!respawnLevel.noCollision(serverPlayer, serverPlayer.getBoundingBox()) && serverPlayer.getY() < 256) {
							serverPlayer.setPos(serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ());
						}

						player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_RESPAWNER_TELEPORTING.getTranslationKey(), spawnLocation.x(), spawnLocation.y(), spawnLocation.z(), respawnLevel.dimension()));

						return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
					})
					.orElseGet(() -> new InteractionResultHolder<>(InteractionResult.FAIL, heldItem));
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}
}
