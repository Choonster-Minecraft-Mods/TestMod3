package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

/**
 * An item that teleports the player to their spawn position when right clicked.
 *
 * @author Choonster
 */
public class ItemRespawner extends ItemTestMod3 {
	public ItemRespawner() {
		super("respawner");
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand) {
		if (!worldIn.isRemote) {
			final EntityPlayerMP playerMP = (EntityPlayerMP) playerIn;

			final int dimension;
			if (!worldIn.provider.canRespawnHere()) {
				dimension = worldIn.provider.getRespawnDimension(playerMP);
				playerMP.changeDimension(dimension);
			} else {
				dimension = playerMP.dimension;
			}

			final BlockPos bedLocation = playerMP.getBedLocation(dimension);
			final WorldServer worldServer = worldIn.getMinecraftServer() != null ? worldIn.getMinecraftServer().worldServerForDimension(dimension) : null;

			if (worldServer == null) {
				return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
			}

			if (bedLocation == null) {
				playerMP.sendMessage(new TextComponentTranslation("message.testmod3:respawner.no_spawn_location"));
				return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
			}

			final boolean spawnForced = playerMP.isSpawnForced(dimension);
			final BlockPos spawnLocation = EntityPlayer.getBedSpawnLocation(worldServer, bedLocation, spawnForced);

			if (spawnLocation == null) {
				return new ActionResult<>(EnumActionResult.FAIL, itemStackIn);
			}

			playerMP.setLocationAndAngles(spawnLocation.getX() + 0.5, spawnLocation.getY() + 0.1, spawnLocation.getZ() + 0.5, 0, 0);
			playerMP.connection.setPlayerLocation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, playerMP.rotationPitch);
			worldServer.getChunkProvider().loadChunk((int) playerMP.posX >> 4, (int) playerMP.posZ >> 4);

			while (!worldServer.getCollisionBoxes(playerMP, playerMP.getEntityBoundingBox()).isEmpty() && playerMP.posY < 256.0D) {
				playerMP.setPosition(playerMP.posX, playerMP.posY + 1.0D, playerMP.posZ);
			}

			playerIn.sendMessage(new TextComponentTranslation("message.testmod3:respawner.teleporting", spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), dimension));

			return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
		}

		return new ActionResult<>(EnumActionResult.SUCCESS, itemStackIn);
	}
}
