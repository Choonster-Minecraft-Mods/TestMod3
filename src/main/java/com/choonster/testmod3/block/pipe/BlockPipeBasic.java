package com.choonster.testmod3.block.pipe;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;

public class BlockPipeBasic extends BlockPipeBase {
	public BlockPipeBasic(String blockName) {
		super(Material.iron, blockName);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, CONNECTED_PROPERTIES.toArray(new IProperty[CONNECTED_PROPERTIES.size()]));
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
