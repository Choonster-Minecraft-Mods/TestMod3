package choonster.testmod3.block.pipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for pipe blocks that connect to adjacent pipes and blocks.
 *
 * @author Choonster
 */
public abstract class BlockPipeBase extends Block {
	public static final float PIPE_MIN_POS = 0.25f;
	public static final float PIPE_MAX_POS = 0.75f;

	public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
			Stream.of(EnumFacing.VALUES)
					.map(facing -> PropertyBool.create(facing.getName()))
					.collect(Collectors.toList())
	);

	public static final ImmutableList<AxisAlignedBB> CONNECTED_BOUNDING_BOXES = ImmutableList.copyOf(
			Stream.of(EnumFacing.VALUES)
					.map(facing -> {
						Vec3i directionVec = facing.getDirectionVec();
						return new AxisAlignedBB(
								getMinBound(directionVec.getX()), getMinBound(directionVec.getY()), getMinBound(directionVec.getZ()),
								getMaxBound(directionVec.getX()), getMaxBound(directionVec.getY()), getMaxBound(directionVec.getZ())
						);
					})
					.collect(Collectors.toList())
	);

	private static float getMinBound(final int dir) {
		return dir == -1 ? 0 : PIPE_MIN_POS;
	}

	private static float getMaxBound(final int dir) {
		return dir == 1 ? 1 : PIPE_MAX_POS;
	}

	public BlockPipeBase(final Material material) {
		super(material);
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

	/**
	 * Is the neighbouring block a valid connection for this pipe?
	 *
	 * @param ownState           This pipe's state
	 * @param neighbourState     The neighbouring block's state
	 * @param world              The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring block
	 * @return Is the neighbouring block a valid connection?
	 */
	protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IBlockAccess world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
		return neighbourState.getBlock() instanceof BlockPipeBase;
	}

	/**
	 * Can this pipe connect to the neighbouring block?
	 *
	 * @param ownState           This pipe's state
	 * @param worldIn            The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring block
	 * @return Can this pipe connect?
	 */
	private boolean canConnectTo(final IBlockState ownState, final IBlockAccess worldIn, final BlockPos ownPos, final EnumFacing neighbourDirection) {
		final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		final IBlockState neighbourState = worldIn.getBlockState(neighbourPos);
		final Block neighbourBlock = neighbourState.getBlock();

		final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, worldIn, ownPos, neighbourDirection);
		final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockPipeBase) || ((BlockPipeBase) neighbourBlock).isValidConnection(neighbourState, ownState, worldIn, neighbourPos, neighbourDirection.getOpposite());

		return neighbourIsValidForThis && thisIsValidForNeighbour;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getActualState(IBlockState state, final IBlockAccess world, final BlockPos pos) {
		for (final EnumFacing facing : EnumFacing.VALUES) {
			state = state.withProperty(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, pos, facing));
		}

		return state;
	}

	public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
		return state.getValue(CONNECTED_PROPERTIES.get(facing.getIndex()));
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
		final AxisAlignedBB bb = new AxisAlignedBB(PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MIN_POS, PIPE_MAX_POS, PIPE_MAX_POS, PIPE_MAX_POS);
		addCollisionBoxToList(pos, entityBox, collidingBoxes, bb);

		if (!p_185477_7_) {
			state = state.getActualState(worldIn, pos);
		}

		for (final EnumFacing facing : EnumFacing.VALUES) {
			if (isConnected(state, facing)) {
				final AxisAlignedBB axisAlignedBB = CONNECTED_BOUNDING_BOXES.get(facing.getIndex());
				addCollisionBoxToList(pos, entityBox, collidingBoxes, axisAlignedBB);
			}
		}
	}
}
