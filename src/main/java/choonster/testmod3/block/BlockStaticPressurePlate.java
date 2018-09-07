package choonster.testmod3.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFence;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * A block that uses pressure plate model, placement and piston movement behaviour; but doesn't depress when stood on.
 *
 * @author Choonster
 */
public abstract class BlockStaticPressurePlate extends BlockTestMod3 {
	protected final AxisAlignedBB BB = new AxisAlignedBB(0.0625D, 0.0D, 0.0625D, 0.9375D, 0.03125D, 0.9375D);

	public BlockStaticPressurePlate(final Material materialIn, final String blockName) {
		super(materialIn, blockName);
	}

	@SuppressWarnings("deprecation")
	@Override
	public AxisAlignedBB getBoundingBox(final IBlockState state, final IBlockAccess source, final BlockPos pos) {
		return BB;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullCube(final IBlockState state) {
		return false;
	}

	@Override
	public boolean isPassable(final IBlockAccess worldIn, final BlockPos pos) {
		return true;
	}

	@Override
	public boolean canPlaceBlockAt(final World worldIn, final BlockPos pos) {
		return canBePlacedOn(worldIn, pos.down());
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(final IBlockState state, final World worldIn, final BlockPos thisPos, final Block blockIn, final BlockPos neighbourPos) {
		if (!this.canBePlacedOn(worldIn, thisPos.down())) {
			this.dropBlockAsItem(worldIn, thisPos, state, 0);
			worldIn.setBlockToAir(thisPos);
		}
	}

	private boolean canBePlacedOn(final World world, final BlockPos pos) {
		return world.getBlockState(pos).isSideSolid(world, pos, EnumFacing.UP) || world.getBlockState(pos).getBlock() instanceof BlockFence;
	}

	@SuppressWarnings("deprecation")
	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(final IBlockState blockState, final IBlockAccess worldIn, final BlockPos pos) {
		return NULL_AABB;
	}

	@SuppressWarnings("deprecation")
	@Override
	public EnumPushReaction getPushReaction(final IBlockState state) {
		return EnumPushReaction.DESTROY;
	}
}
