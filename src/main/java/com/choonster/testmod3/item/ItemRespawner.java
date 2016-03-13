package com.choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
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
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			final EntityPlayerMP playerMP = (EntityPlayerMP) playerIn;

			final int dimension;
			if (!worldIn.provider.canRespawnHere()) {
				dimension = worldIn.provider.getRespawnDimension(playerMP);
				playerMP.travelToDimension(dimension);
			} else {
				dimension = playerMP.dimension;
			}

			final BlockPos bedLocation = playerMP.getBedLocation(dimension);
			final WorldServer worldServer = MinecraftServer.getServer().worldServerForDimension(dimension);

			if (bedLocation == null) {
				playerMP.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:respawner.noSpawnLocation"));
			} else {
				final boolean spawnForced = playerMP.isSpawnForced(dimension);
				final BlockPos spawnLocation = EntityPlayer.getBedSpawnLocation(worldServer, bedLocation, spawnForced);
				playerMP.setLocationAndAngles(spawnLocation.getX() + 0.5, spawnLocation.getY() + 0.1, spawnLocation.getZ() + 0.5, 0, 0);
				playerMP.playerNetServerHandler.setPlayerLocation(playerMP.posX, playerMP.posY, playerMP.posZ, playerMP.rotationYaw, playerMP.rotationPitch);
				worldServer.theChunkProviderServer.loadChunk((int) playerMP.posX >> 4, (int) playerMP.posZ >> 4);

				while (!worldServer.getCollidingBoundingBoxes(playerMP, playerMP.getEntityBoundingBox()).isEmpty() && playerMP.posY < 256.0D) {
					playerMP.setPosition(playerMP.posX, playerMP.posY + 1.0D, playerMP.posZ);
				}

				playerIn.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:respawner.teleporting", spawnLocation.getX(), spawnLocation.getY(), spawnLocation.getZ(), dimension));
			}


		}

		return itemStackIn;
	}
}
