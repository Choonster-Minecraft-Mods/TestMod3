package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;

import java.util.List;

/**
 * A Block with a 3x3x3 bounding box.
 * <p>
 * Currently only the selection bounding box works.
 * Entity collision still treats the bounding box as 1x1x1 and glitches out if you try to enter this bounding box.
 */
public class BlockLargeCollisionTest extends Block {
	public BlockLargeCollisionTest() {
		super(Material.cloth);
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("largeCollisionTest");
		setBlockBounds(-1, -1, -1, 2, 2, 2);
	}

	@Override
	public void addCollisionBoxesToList(World worldIn, BlockPos pos, IBlockState state, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity collidingEntity) {
		super.addCollisionBoxesToList(worldIn, pos, state, mask, list, collidingEntity);
	}
}
