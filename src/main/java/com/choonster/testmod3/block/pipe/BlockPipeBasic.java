package com.choonster.testmod3.block.pipe;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

/**
 * A basic pipe that only connects to other pipes.
 *
 * @author Choonster
 */
public class BlockPipeBasic extends BlockPipeBase {
	public BlockPipeBasic(String blockName) {
		super(Material.iron, blockName);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
	}

	@Override
	public IBlockState getStateFromMeta(int meta) {
		return getDefaultState();
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}
}
