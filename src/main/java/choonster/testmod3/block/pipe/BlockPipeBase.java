package choonster.testmod3.block.pipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReaderBase;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for pipe blocks that connect to adjacent pipes and blocks.
 *
 * @author Choonster
 */
public class BlockPipeBase extends Block {
	public static final float PIPE_MIN_POS = 0.25f;
	public static final float PIPE_MAX_POS = 0.75f;

	public static final ImmutableList<IProperty<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
			Stream.of(EnumFacing.values())
					.map(facing -> BooleanProperty.create(facing.getName()))
					.collect(Collectors.toList())
	);

	// TODO: Convert this to VoxelShapes
	public static final ImmutableList<AxisAlignedBB> CONNECTED_BOUNDING_BOXES = ImmutableList.copyOf(
			Stream.of(EnumFacing.values())
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

	public BlockPipeBase(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		CONNECTED_PROPERTIES.forEach(builder::add);
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
	protected boolean isValidConnection(final IBlockState ownState, final IBlockState neighbourState, final IWorldReaderBase world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
		return neighbourState.getBlock() instanceof BlockPipeBase;
	}

	/**
	 * Can this pipe connect to the neighbouring block?
	 *
	 * @param ownState           This pipe's state
	 * @param world              The world
	 * @param ownPos             This pipe's position
	 * @param neighbourDirection The direction of the neighbouring block
	 * @return Can this pipe connect?
	 */
	private boolean canConnectTo(final IBlockState ownState, final IWorldReaderBase world, final BlockPos ownPos, final EnumFacing neighbourDirection) {
		final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		final IBlockState neighbourState = world.getBlockState(neighbourPos);
		final Block neighbourBlock = neighbourState.getBlock();

		final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection);
		final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BlockPipeBase) || ((BlockPipeBase) neighbourBlock).isValidConnection(neighbourState, ownState, world, neighbourPos, neighbourDirection.getOpposite());

		return neighbourIsValidForThis && thisIsValidForNeighbour;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState updatePostPlacement(IBlockState state, final EnumFacing facing, final IBlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		for (final EnumFacing neightbourFacing : EnumFacing.values()) {
			state = state.with(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, currentPos, neightbourFacing));
		}

		return state;
	}

	public final boolean isConnected(final IBlockState state, final EnumFacing facing) {
		return state.get(CONNECTED_PROPERTIES.get(facing.getIndex()));
	}

	/*
	// TODO: Convert this to VoxelShapes
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
	}*/
}
