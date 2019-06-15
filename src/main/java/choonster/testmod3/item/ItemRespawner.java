package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.dimension.DimensionType;

/**
 * An item that teleports the player to their spawn position when right clicked.
 *
 * @author Choonster
 */
public class ItemRespawner extends Item {
	public ItemRespawner(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(final World world, final EntityPlayer player, final EnumHand hand) {
		final ItemStack heldItem = player.getHeldItem(hand);

		if (!world.isRemote) {
			final EntityPlayerMP playerMP = (EntityPlayerMP) player;

			final DimensionType dimension;
			if (!world.dimension.canRespawnHere()) {
				dimension = world.dimension.getRespawnDimension(playerMP);
				playerMP.changeDimension(dimension, new Teleporter((WorldServer) world));
			} else {
				dimension = playerMP.dimension;
			}

			final BlockPos bedLocation = playerMP.getBedLocation(dimension);
			final WorldServer worldServer = world.getServer() != null ? world.getServer().getWorld(dimension) : null;

			if (worldServer == null) {
				return new ActionResult<>(EnumActionResult.FAIL, heldItem);
			}

			if (bedLocation == null) {
				playerMP.sendMessage(new TextComponentTranslation("message.testmod3.respawner.no_spawn_location"));
				return new ActionResult<>(EnumActionResult.FAIL, heldItem);
			}

			final boolean spawnForced = playerMP.isSpawnForced(dimension);
			final BlockPos spawnLocation = EntityPlayer.getBedSpawnLocation(worldServer, bedLocation, spawnForced);

			if (spawnLocation == null) {
				return new ActionResult<>(EnumActionResult.FAIL, heldItem);
			}

			playerMP.setLocationAndAngles(spawnLocation.getX() + 0.5, spawnLocation.getY() + 0.1, spawnLocation.getZ() + 0.5, 0, 0);
			playerMP.connection.setPlayerLocation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, playerMP.rotationPitch);
			worldServer.getChunkProvider().getChunk((int) playerMP.posX >> 4, (int) playerMP.posZ >> 4, true, true);

			while (!worldServer.isCollisionBoxesEmpty(playerMP, playerMP.getBoundingBox()) && playerMP.posY < 256.0D) {
				playerMP.setPosition(playerMP.posX, playerMP.posY + 1.0D, playerMP.posZ);
			}

			player.sendMessage(new TextComponentTranslation("message.testmod3.respawner.teleporting", spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), dimension));

			return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, heldItem);
	}
}
