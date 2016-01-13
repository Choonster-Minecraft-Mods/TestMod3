package com.choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

/**
 * When right clicked, prints the value of {@link World#getHeight(BlockPos)} at the player's current position.
 * When sneak-right clicked, regenerates the skylight map for the player's current chunk (updating the height map used by {@link World#getHeight(BlockPos)}.
 *
 * @author Choonster
 */
public class ItemHeightTester extends ItemTestMod3 {
	public ItemHeightTester() {
		super("heightTester");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if (!worldIn.isRemote) {
			BlockPos pos = playerIn.getPosition();

			if (playerIn.isSneaking()) {
				Chunk chunk = worldIn.getChunkFromBlockCoords(pos);
				chunk.generateSkylightMap();
				playerIn.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:heightTester.generate", chunk.xPosition, chunk.zPosition, pos.getX(), pos.getY(), pos.getZ()));
			} else {
				playerIn.addChatComponentMessage(new ChatComponentTranslation("message.testmod3:heightTester.height", pos.getX(), pos.getZ(), worldIn.getHeight(pos).getY()));
			}
		}

		return itemStackIn;
	}
}
