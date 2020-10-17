package choonster.testmod3.item;

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
	public ActionResult<ItemStack> onItemRightClick(final World world, final PlayerEntity player, final Hand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!world.isRemote) {
			final ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

			final BlockPos spawnPosition = serverPlayer.func_241140_K_();
			final ServerWorld spawnWorld = serverPlayer.server.getWorld(serverPlayer.func_241141_L_());
			final boolean spawnForced = serverPlayer.func_241142_M_();
			final float spawnAngle = serverPlayer.func_242109_L();

			if (spawnPosition == null || spawnWorld == null) {
				serverPlayer.sendMessage(new TranslationTextComponent("message.testmod3.respawner.no_spawn_location"), Util.DUMMY_UUID);
				return new ActionResult<>(ActionResultType.FAIL, heldItem);
			}

			if (spawnWorld != serverPlayer.getServerWorld()) {
				player.changeDimension(spawnWorld);
			}

			return PlayerEntity./* getSpawnLocation */func_242374_a(spawnWorld, spawnPosition, spawnAngle, spawnForced, /* endConquered */false)
					.map(spawnLocation -> {
						serverPlayer.setLocationAndAngles(spawnLocation.x + 0.5, spawnLocation.getY() + 0.1, spawnLocation.getZ() + 0.5, 0, 0);
						serverPlayer.connection.setPlayerLocation(serverPlayer.getPosX(), serverPlayer.getPosY(), serverPlayer.getPosZ(), serverPlayer.rotationYaw, serverPlayer.rotationPitch);

						while (!spawnWorld.hasNoCollisions(serverPlayer, serverPlayer.getBoundingBox()) && serverPlayer.getPosY() < 256) {
							serverPlayer.setPosition(serverPlayer.getPosX(), serverPlayer.getPosY() + 1, serverPlayer.getPosZ());
						}

						player.sendMessage(new TranslationTextComponent("message.testmod3.respawner.teleporting", spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), spawnWorld.getDimensionKey()), Util.DUMMY_UUID);

						return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
					})
					.orElseGet(() -> new ActionResult<>(ActionResultType.FAIL, heldItem));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
