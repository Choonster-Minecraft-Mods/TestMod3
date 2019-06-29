package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.IFluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A block with a {@link TileEntity}.
 *
 * @author Choonster
 */
public abstract class TileEntityBlock<TE extends TileEntity> extends Block {
	/**
	 * Should the {@link TileEntity} be preserved until after {@link #getDrops} has been called?
	 */
	// TODO: Is this still needed in 1.13?
	private final boolean preserveTileEntity;

	public TileEntityBlock(final boolean preserveTileEntity, final Block.Properties properties) {
		super(properties);
		this.preserveTileEntity = preserveTileEntity;
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

	@Override
	public boolean removedByPlayer(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final boolean willHarvest, final IFluidState fluid) {
		// If it will harvest, delay deletion of the block until after getDrops
		return preserveTileEntity && willHarvest || super.removedByPlayer(state, world, pos, player, willHarvest, fluid);
	}

	@Override
	public void harvestBlock(final World world, final PlayerEntity player, final BlockPos pos, final BlockState state, @Nullable final TileEntity te, final ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);

		if (preserveTileEntity) {
			world.setBlockState(pos, Blocks.AIR.getDefaultState());
		}
	}
}
