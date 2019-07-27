package choonster.testmod3.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.dimension.DimensionType;
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
			final ServerPlayerEntity playerMP = (ServerPlayerEntity) player;

			final DimensionType dimension;
			if (!world.dimension.canRespawnHere()) {
				dimension = world.dimension.getRespawnDimension(playerMP);
				playerMP.changeDimension(dimension);
			} else {
				dimension = playerMP.dimension;
			}

			final BlockPos bedLocation = playerMP.getBedLocation(dimension);
			final ServerWorld worldServer = world.getServer() != null ? world.getServer().getWorld(dimension) : null;

			if (worldServer == null) {
				return new ActionResult<>(ActionResultType.FAIL, heldItem);
			}

			if (bedLocation == null) {
				playerMP.sendMessage(new TranslationTextComponent("message.testmod3.respawner.no_spawn_location"));
				return new ActionResult<>(ActionResultType.FAIL, heldItem);
			}

			final boolean spawnForced = playerMP.isSpawnForced(dimension);

			return PlayerEntity.func_213822_a(worldServer, bedLocation, spawnForced)
					.map(spawnLocation -> {
						playerMP.setLocationAndAngles(spawnLocation.x + 0.5, spawnLocation.getY() + 0.1, spawnLocation.getZ() + 0.5, 0, 0);
						playerMP.connection.setPlayerLocation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, playerMP.rotationPitch);
						worldServer.getChunkProvider().getChunk((int) playerMP.posX >> 4, (int) playerMP.posZ >> 4, ChunkStatus.FULL, true);

						while (!worldServer.isCollisionBoxesEmpty(playerMP, playerMP.getBoundingBox()) && playerMP.posY < 256.0D) {
							playerMP.setPosition(playerMP.posX, playerMP.posY + 1.0D, playerMP.posZ);
						}

						player.sendMessage(new TranslationTextComponent("message.testmod3.respawner.teleporting", spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), dimension));

						return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
					})
					.orElseGet(() -> new ActionResult<>(ActionResultType.FAIL, heldItem));
		}

		return new ActionResult<>(ActionResultType.SUCCESS, heldItem);
	}
}
