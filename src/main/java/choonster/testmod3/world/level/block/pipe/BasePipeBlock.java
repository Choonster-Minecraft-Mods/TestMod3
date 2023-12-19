package choonster.testmod3.world.level.block.pipe;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.BiFunction;

/**
 * Base class for pipe blocks that connect to adjacent pipes and blocks.
 *
 * @author Choonster
 */
public abstract class BasePipeBlock extends PipeBlock {
	private static final Direction[] FACING_VALUES = Direction.values();

	protected final ImmutableList<VoxelShape> shapes;

	/**
	 * Gets the connected property for the specified direction.
	 *
	 * @param direction The direction
	 * @return The property
	 */
	public static Property<Boolean> getConnectedProperty(final Direction direction) {
		return PROPERTY_BY_DIRECTION.get(direction);
	}

	public BasePipeBlock(final BlockBehaviour.Properties properties) {
		this(4, 4, properties);
	}

	public BasePipeBlock(final float coreSize, final float extensionWidth, final BlockBehaviour.Properties properties) {
		super(0, properties);

		shapes = makeShapes(coreSize, extensionWidth);
	}

	@Override
	protected abstract MapCodec<? extends PipeBlock> codec();

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		PROPERTY_BY_DIRECTION.values().forEach(builder::add);
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
	protected boolean isValidConnection(final BlockState ownState, final BlockState neighbourState, final LevelReader world, final BlockPos ownPos, final Direction neighbourDirection) {
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
	private boolean canConnectTo(final BlockState ownState, final LevelReader world, final BlockPos ownPos, final Direction neighbourDirection) {
		final BlockPos neighbourPos = ownPos.relative(neighbourDirection);
		final BlockState neighbourState = world.getBlockState(neighbourPos);
		final Block neighbourBlock = neighbourState.getBlock();

		final boolean neighbourIsValidForThis = isValidConnection(ownState, neighbourState, world, ownPos, neighbourDirection);
		final boolean thisIsValidForNeighbour = !(neighbourBlock instanceof BasePipeBlock) || ((BasePipeBlock) neighbourBlock).isValidConnection(neighbourState, ownState, world, neighbourPos, neighbourDirection.getOpposite());

		return neighbourIsValidForThis && thisIsValidForNeighbour;
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState updateShape(BlockState state, final Direction facing, final BlockState facingState, final LevelAccessor world, final BlockPos currentPos, final BlockPos facingPos) {
		// TODO: This may be incorrect
		for (final Direction neighbourFacing : Direction.values()) {
			state = state.setValue(PROPERTY_BY_DIRECTION.get(facing), canConnectTo(state, world, currentPos, neighbourFacing));
		}

		return state;
	}

	public final boolean isConnected(final BlockState state, final Direction facing) {
		return state.getValue(PROPERTY_BY_DIRECTION.get(facing));
	}

	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
		return shapes.get(getAABBIndex(state));
	}

	/**
	 * Creates a list containing a VoxelShape for every possible combination of facing properties.
	 * <p>
	 * Adapted from {@link PipeBlock}#makeShapes.
	 *
	 * @param coreSize       Half the core cube's width, height and length
	 * @param extensionWidth Half the extension's face width and height
	 * @return The list of VoxelShapes
	 */
	private ImmutableList<VoxelShape> makeShapes(final float coreSize, final float extensionWidth) {
		final float coreMin = 8 - coreSize;
		final float coreMax = 8 + coreSize;

		final float extensionFaceMin = 8 - extensionWidth;
		final float extensionFaceMax = 8 + extensionWidth;

		final VoxelShape coreShape = box(coreMin, coreMin, coreMin, coreMax, coreMax, coreMax);

		final BiFunction<Direction.Axis, Direction, Float> getMinCoordinate = (axis, direction) -> {
			if (axis == direction.getAxis()) {
				return direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? 0 : coreMax;
			}

			return extensionFaceMin;
		};

		final BiFunction<Direction.Axis, Direction, Float> getMaxCoordinate = (axis, direction) -> {
			if (axis == direction.getAxis()) {
				return direction.getAxisDirection() == Direction.AxisDirection.NEGATIVE ? coreMin : 16;
			}

			return extensionFaceMax;
		};

		final VoxelShape[] extensionShapes = new VoxelShape[FACING_VALUES.length];

		for (int facingIndex = 0; facingIndex < FACING_VALUES.length; ++facingIndex) {
			final Direction direction = FACING_VALUES[facingIndex];

			extensionShapes[facingIndex] = box(
					getMinCoordinate.apply(Direction.Axis.X, direction),
					getMinCoordinate.apply(Direction.Axis.Y, direction),
					getMinCoordinate.apply(Direction.Axis.Z, direction),
					getMaxCoordinate.apply(Direction.Axis.X, direction),
					getMaxCoordinate.apply(Direction.Axis.Y, direction),
					getMaxCoordinate.apply(Direction.Axis.Z, direction)
			);
		}

		final VoxelShape[] combinedShapes = new VoxelShape[64];

		for (int shapeIndex = 0; shapeIndex < 64; ++shapeIndex) {
			VoxelShape shape = coreShape;

			for (int facingIndex = 0; facingIndex < FACING_VALUES.length; ++facingIndex) {
				if ((shapeIndex & 1 << facingIndex) != 0) {
					shape = Shapes.or(shape, extensionShapes[facingIndex]);
				}
			}

			combinedShapes[shapeIndex] = shape;
		}

		return ImmutableList.copyOf(combinedShapes);
	}
}
