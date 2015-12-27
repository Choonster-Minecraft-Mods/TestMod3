package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

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
}
