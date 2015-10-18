package com.choonster.testmod3.block;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * A Block that prints the current state of the player's held ItemStack on the client and server when right clicked.
 */
public class BlockItemDebugger extends Block {
	public BlockItemDebugger() {
		super(Material.iron);
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("itemDebugger");
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack stack = playerIn.getHeldItem();
		if (stack != null) {
			Logger.info("Item: %s", stack);
			if (stack.hasTagCompound()) {
				Logger.info("NBT data: %s", stack.getTagCompound());
			}
		}

		return true;
	}
}
