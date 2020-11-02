package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;

import javax.annotation.Nullable;

/**
 * A block with a {@link TileEntity}.
 *
 * @author Choonster
 */
public abstract class TileEntityBlock<TE extends TileEntity> extends Block {
	public TileEntityBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	public boolean hasTileEntity(final BlockState state) {
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(final BlockState state, final IBlockReader world);

	/**
	 * Get the {@link TileEntity} at the specified position.
	 *
	 * @param world The World
	 * @param pos   The position
	 * @return The TileEntity
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	protected TE getTileEntity(final IBlockReader world, final BlockPos pos) {
		return (TE) world.getTileEntity(pos);
	}
}
