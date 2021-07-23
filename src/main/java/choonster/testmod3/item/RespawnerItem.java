package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

/**
 * An item that teleports the player to their spawn position when right clicked.
 *
 * @author Choonster
 */
public class RespawnerItem extends Item {
	public RespawnerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> use(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getItemInHand(hand);

		if (!world.isClientSide) {
			final ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

			final BlockPos respawnPosition = serverPlayer.getRespawnPosition();
			final ServerWorld respawnWorld = serverPlayer.server.getLevel(serverPlayer.getRespawnDimension());
			final boolean respawnForced = serverPlayer.isRespawnForced();
			final float respawnAngle = serverPlayer.getRespawnAngle();

			if (respawnPosition == null || respawnWorld == null) {
				serverPlayer.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_RESPAWNER_NO_SPAWN_LOCATION.getTranslationKey()), Util.NIL_UUID);
				return new ActionResult<>(ActionResultType.FAIL, heldItem);
			}

			if (respawnWorld != serverPlayer.getLevel()) {
				player.changeDimension(respawnWorld);
			}

			return PlayerEntity.findRespawnPositionAndUseSpawnBlock(respawnWorld, respawnPosition, respawnAngle, respawnForced, /* endConquered */false)
					.map(spawnLocation -> {
						serverPlayer.moveTo(spawnLocation.x + 0.5, spawnLocation.y() + 0.1, spawnLocation.z() + 0.5, 0, 0);
						serverPlayer.connection.teleport(serverPlayer.getX(), serverPlayer.getY(), serverPlayer.getZ(), serverPlayer.yRot, serverPlayer.xRot);

						while (!respawnWorld.noCollision(serverPlayer, serverPlayer.getBoundingBox()) && serverPlayer.getY() < 256) {
							serverPlayer.setPos(serverPlayer.getX(), serverPlayer.getY() + 1, serverPlayer.getZ());
						}

						player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_RESPAWNER_TELEPORTING.getTranslationKey(), spawnLocation.x(), spawnLocation.y(), spawnLocation.z(), respawnWorld.dimension()), Util.NIL_UUID);

						return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
					})
					.orElseGet(() -> new ActionResult<>(ActionResultType.FAIL, heldItem));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
