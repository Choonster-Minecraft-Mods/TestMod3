package choonster.testmod3.block;

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
public abstract class BlockTileEntity<TE extends TileEntity> extends BlockTestMod3 {
	/**
	 * Should the {@link TileEntity} be preserved until after {@link #getDrops} has been called?
	 */
	private final boolean preserveTileEntity;

	public BlockTileEntity(Material material, MapColor mapColor, String blockName, boolean preserveTileEntity) {
		super(material, mapColor, blockName);
		this.preserveTileEntity = preserveTileEntity;
	}

	public BlockTileEntity(Material materialIn, String blockName, boolean preserveTileEntity) {
		super(materialIn, blockName);
		this.preserveTileEntity = preserveTileEntity;
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
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
	protected TE getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TE) world.getTileEntity(pos);
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		// If it will harvest, delay deletion of the block until after getDrops
		return preserveTileEntity && willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);

		if (preserveTileEntity) {
			world.setBlockToAir(pos);
		}
	}
}
