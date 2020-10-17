package choonster.testmod3.block.pipe;

import com.google.common.collect.ImmutableList;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3i;
import net.minecraft.world.IWorld;
import net.minecraft.world.IWorldReader;

import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Base class for pipe blocks that connect to adjacent pipes and blocks.
 *
 * @author Choonster
 */
public class BasePipeBlock extends Block {
	public static final float PIPE_MIN_POS = 0.25f;
	public static final float PIPE_MAX_POS = 0.75f;

	public static final ImmutableList<Property<Boolean>> CONNECTED_PROPERTIES = ImmutableList.copyOf(
			Stream.of(Direction.values())
					.map(facing -> BooleanProperty.create(facing.getString()))
					.collect(Collectors.toList())
	);

	// TODO: Convert this to VoxelShapes
	public static final ImmutableList<AxisAlignedBB> CONNECTED_BOUNDING_BOXES = ImmutableList.copyOf(
			Stream.of(Direction.values())
					.map(facing -> {
						Vector3i directionVec = facing.getDirectionVec();
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

	/**
	 * Gets the connected property for the specified direction.
	 *
	 * @param direction The direction
	 * @return The property
	 */
	public static Property<Boolean> getConnectedProperty(final Direction direction) {
		return CONNECTED_PROPERTIES.get(direction.getIndex());
	}

	public BasePipeBlock(final Block.Properties properties) {
		super(properties);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		CONNECTED_PROPERTIES.forEach(builder::add);
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
	protected boolean isValidConnection(final BlockState ownState, final BlockState neighbourState, final IWorldReader world, final BlockPos ownPos, final Direction neighbourDirection) {
		return neighbourState.getBlock() instanceof BasePipeBlock;
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
	private boolean canConnectTo(final BlockState ownState, final IWorldReader world, final BlockPos ownPos, final Direction neighbourDirection) {
		final BlockPos neighbourPos = ownPos.offset(neighbourDirection);
		final BlockState neighbourState = world.getBlockState(neighbourPos);
		final Block neighbourBlock = neighbourState.getBlock();

		final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection);
		final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BasePipeBlock) || ((BasePipeBlock) neighbourBlock).isValidConnection(neighbourState, ownState, world, neighbourPos, neighbourDirection.getOpposite());

		return neighbourIsValidForThis && thisIsValidForNeighbour;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updatePostPlacement(BlockState state, final Direction facing, final BlockState facingState, final IWorld world, final BlockPos currentPos, final BlockPos facingPos) {
		for (final Direction neighbourFacing : Direction.values()) {
			state = state.with(CONNECTED_PROPERTIES.get(facing.getIndex()), canConnectTo(state, world, currentPos, neighbourFacing));
		}

		return state;
	}

	public final boolean isConnected(final BlockState state, final Direction facing) {
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

		for (final Direction facing : Direction.VALUES) {
			if (isConnected(state, facing)) {
				final AxisAlignedBB axisAlignedBB = CONNECTED_BOUNDING_BOXES.get(facing.getIndex());
				addCollisionBoxToList(pos, entityBox, collidingBoxes, axisAlignedBB);
			}
		}
	}*/
}
