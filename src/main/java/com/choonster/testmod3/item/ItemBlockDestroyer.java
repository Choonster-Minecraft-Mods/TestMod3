package com.choonster.testmod3.item;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class ItemBlockDestroyer extends ItemTestMod3 {
	public ItemBlockDestroyer() {
		super("blockDestroyer");
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
		if (!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			if (state.getBlock() == Blocks.wheat && state.getValue(BlockCrops.AGE) >= 6) {
				playerIn.addChatMessage(new ChatComponentTranslation("message.blockDestroyer.destroy"));
			}
		}

		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
	}
}
