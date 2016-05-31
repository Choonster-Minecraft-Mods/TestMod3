package com.choonster.testmod3.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

import javax.annotation.Nullable;

/**
 * A block with a {@link TileEntity}.
 *
 * @author Choonster
 */
public abstract class BlockTileEntity<TE extends TileEntity> extends BlockTestMod3 {

	public BlockTileEntity(Material material, MapColor mapColor, String blockName) {
		super(material, mapColor, blockName);
	}

	public BlockTileEntity(Material materialIn, String blockName) {
		super(materialIn, blockName);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	/**
	 * Get the {@link TileEntity} at the specified position.
	 *
	 * @param world The World
	 * @param pos   The position
	 * @return The TileEntity
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	public TE getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TE) world.getTileEntity(pos);
	}
}
