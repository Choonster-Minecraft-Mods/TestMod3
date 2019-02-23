package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A block with a {@link TileEntity}.
 *
 * @author Choonster
 */
public abstract class BlockTileEntity<TE extends TileEntity> extends Block {
	/**
	 * Should the {@link TileEntity} be preserved until after {@link #getDrops} has been called?
	 */
	private final boolean preserveTileEntity;

	public BlockTileEntity(final Material material, final MapColor mapColor, final boolean preserveTileEntity) {
		super(material, mapColor);
		this.preserveTileEntity = preserveTileEntity;
	}

	public BlockTileEntity(final Material materialIn, final boolean preserveTileEntity) {
		this(materialIn, materialIn.getMaterialMapColor(), preserveTileEntity);
	}

	@Override
	public boolean hasTileEntity(final IBlockState state) {
		return true;
	}

	@Override
	public abstract TileEntity createTileEntity(World world, IBlockState state);

	/**
	 * Get the {@link TileEntity} at the specified position.
	 *
	 * @param world The World
	 * @param pos   The position
	 * @return The TileEntity
	 */
	@SuppressWarnings("unchecked")
	@Nullable
	protected TE getTileEntity(final IBlockAccess world, final BlockPos pos) {
		return (TE) world.getTileEntity(pos);
	}

	@Override
	public boolean removedByPlayer(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final boolean willHarvest) {
		// If it will harvest, delay deletion of the block until after getDrops
		return preserveTileEntity && willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public void harvestBlock(final World world, final EntityPlayer player, final BlockPos pos, final IBlockState state, @Nullable final TileEntity te, final ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);

		if (preserveTileEntity) {
			world.setBlockToAir(pos);
		}
	}
}
