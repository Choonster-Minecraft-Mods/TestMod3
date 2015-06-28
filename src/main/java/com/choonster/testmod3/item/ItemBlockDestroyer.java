package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.World;

public class ItemBlockDestroyer extends Item {
	public ItemBlockDestroyer() {
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("blockDestroyer");
	}

	@Override
	public boolean onBlockDestroyed(ItemStack stack, World worldIn, Block blockIn, BlockPos pos, EntityLivingBase playerIn) {
		if (!worldIn.isRemote) {
			IBlockState state = worldIn.getBlockState(pos);
			if (state.getBlock() == Blocks.wheat && (int) state.getValue(BlockCrops.AGE) >= 6) {
				playerIn.addChatMessage(new ChatComponentTranslation("message.blockDestroyer.destroy"));
			}
		}

		return super.onBlockDestroyed(stack, worldIn, blockIn, pos, playerIn);
	}
}
