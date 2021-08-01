package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.level.ServerLevel;
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

	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final ServerPlayer serverPlayer = (ServerPlayer) player;

			final BlockPos respawnPosition = serverPlayer.getRespawnPosition();
			final ServerLevel respawnLevel = serverPlayer.server.getLevel(serverPlayer.getRespawnDimension());
			final boolean respawnForced = serverPlayer.isRespawnForced();
			final float respawnAngle = serverPlayer.getRespawnAngle();

			if (respawnPosition == null || respawnLevel == null) {
				serverPlayer.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_RESPAWNER_NO_SPAWN_LOCATION.getTranslationKey()), Util.NIL_UUID);
				return new InteractionResultHolder<>(InteractionResult.FAIL, heldItem);
			}

			if (respawnLevel != serverPlayer.getLevel()) {
				player.changeDimension(respawnLevel);
			}

			return Player.findRespawnPositionAndUseSpawnBlock(respawnLevel, respawnPosition, respawnAngle, respawnForced, /* endConquered */false)
					.map(spawnLocation -> {
						serverPlayer.moveTo(spawnLocation.x + 0.5, spawnLocation.y() + 0.1, spawnLocation.z() + 0.5, 0, 0);
						serverPlayer.connection.teleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.getYRot(), serverPlayer.getXRot());

						while (!respawnLevel.noCollision(serverPlayer, serverPlayer.getBoundingBox()) && serverPlayer.getY() < 256) {
							serverPlayer.setPos(serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ());
						}

						player.sendMessage(new TranslatableComponent(TestMod3Lang.MESSAGE_RESPAWNER_TELEPORTING.getTranslationKey(), spawnLocation.x(), spawnLocation.y(), spawnLocation.z(), respawnLevel.dimension()), Util.NIL_UUID);

						return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
					})
					.orElseGet(() -> new InteractionResultHolder<>(InteractionResult.FAIL, heldItem));
		}

		return new InteractionResultHolder<>(InteractionResult.SUCCESS, heldItem);
	}
}
