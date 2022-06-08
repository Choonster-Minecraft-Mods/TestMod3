package choonster.testmod3.world.level.block;

import choonster.testmod3.util.VectorUtils;
import com.mojang.math.Quaternion;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import org.jetbrains.annotations.Nullable;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * A diagonal half-cube block, like the Carpenter's Slope from Carpenter's Blocks.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,36713.0.html
 *
 * @author Choonster
 */
public class PlaneBlock extends Block {
	/**
	 * The block's rotation around the y-axis.
	 */
	public static final Property<Direction> HORIZONTAL_ROTATION = DirectionProperty.create("horizontal_rotation", Direction.Plane.HORIZONTAL);

	/**
	 * The block's rotation around the z-axis.
	 */
	public static final Property<VerticalRotation> VERTICAL_ROTATION = EnumProperty.create("vertical_rotation", VerticalRotation.class);

	public PlaneBlock(final Block.Properties properties) {
		super(properties);
		registerDefaultState(getStateDefinition().any().setValue(HORIZONTAL_ROTATION, Direction.NORTH).setValue(VERTICAL_ROTATION, VerticalRotation.UP));
	}

	/**
	 * The VoxelShapes for each possible rotation.
	 */
	private static final VoxelShape[] SHAPES = Util.make(() -> {
		final VoxelShape[] combinedShapes = new VoxelShape[12];

		// The number of bounding boxes to create
		final int numBoundingBoxes = 8;
		final double increment = 1.0 / numBoundingBoxes;

		// Create the bounding boxes for the default rotation pair (horizontal = north, vertical = up)
		final List<AABB> boundingBoxes = IntStream.range(0, numBoundingBoxes)
				.mapToObj(i -> new AABB(0, i * increment, 0, 1, (i + 1) * increment, 1 - (i * increment)))
				.collect(Collectors.toList());

		// For each horizontal and vertical rotation pair,
		for (final Direction horizontalRotation : HORIZONTAL_ROTATION.getPossibleValues()) {
			for (final VerticalRotation verticalRotation : VERTICAL_ROTATION.getPossibleValues()) {
				// Get the horizontal (around the y-axis) rotation angle and quaternion
				// Needs to be negated to perform correct rotation.
				final double horizontalRotationAngle = -VectorUtils.getHorizontalRotation(horizontalRotation);
				final Quaternion horizontalRotationQuaternion = VectorUtils.getRotationQuaternion(Direction.Axis.Y, (float) horizontalRotationAngle);

				// Get the vertical (around the z-axis) rotation angle and quaternion
				// Needs to be negated to perform correct rotation.
				final double verticalRotationAngle = -verticalRotation.getAngle();
				final Quaternion verticalRotationQuaternion = VectorUtils.getRotationQuaternion(Direction.Axis.Z, (float) verticalRotationAngle);

				final Quaternion combinedRotationQuaternion = new Quaternion(horizontalRotationQuaternion);
				combinedRotationQuaternion.mul(verticalRotationQuaternion);

				final VoxelShape combinedShape = boundingBoxes
						.stream()
						.map(aabb -> VectorUtils.rotateAABB(aabb, combinedRotationQuaternion)) // Rotate the AABBs
						.map(VectorUtils::adjustAABBForVoxelShape) // Round/offset them
						.map(Shapes::create) // Convert them to VoxelShapes
						.reduce(Shapes.empty(), Shapes::or); // Combine them into a single VoxelShape

				// Add the combined VoxelShape to the array
				combinedShapes[getShapeIndex(horizontalRotation, verticalRotation)] = combinedShape;
			}
		}

		return combinedShapes;
	});

	@Override
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_ROTATION, VERTICAL_ROTATION);
	}

	@Override
	public BlockState rotate(final BlockState state, final LevelAccessor level, final BlockPos pos, final Rotation direction) {
		return state.setValue(HORIZONTAL_ROTATION, direction.rotate(state.getValue(HORIZONTAL_ROTATION)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.setValue(HORIZONTAL_ROTATION, mirror.mirror(state.getValue(HORIZONTAL_ROTATION)));
	}

	private static InteractionResult rotateBlock(final Level level, final BlockPos pos, final Direction axis) {
		final Direction.Axis axisToRotate = axis.getAxis();

		BlockState state = level.getBlockState(pos);

		switch (axisToRotate) {
			case X, Z -> state = state.cycle(VERTICAL_ROTATION);
			case Y -> {
				final Direction originalRotation = state.getValue(HORIZONTAL_ROTATION);
				final Direction newRotation = axis.getAxisDirection() == Direction.AxisDirection.POSITIVE ? originalRotation.getClockWise() : originalRotation.getCounterClockWise();
				state = state.setValue(HORIZONTAL_ROTATION, newRotation);
			}
		}

		return level.setBlockAndUpdate(pos, state) ? InteractionResult.SUCCESS : InteractionResult.FAIL;
	}

	@SuppressWarnings("deprecation")
	@Override
	public InteractionResult use(final BlockState state, final Level level, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		return rotateBlock(level, pos, rayTraceResult.getDirection());
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockPlaceContext context) {
		final Direction horizontalRotation = context.getHorizontalDirection();
		final VerticalRotation verticalRotation = VerticalRotation.fromDirection(context.getClickedFace());

		return defaultBlockState()
				.setValue(HORIZONTAL_ROTATION, horizontalRotation)
				.setValue(VERTICAL_ROTATION, verticalRotation);
	}


	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
		return SHAPES[getShapeIndex(state.getValue(HORIZONTAL_ROTATION), state.getValue(VERTICAL_ROTATION))];
	}

	private static int getShapeIndex(final Direction horizontalRotation, final VerticalRotation verticalRotation) {
		return verticalRotation.ordinal() * 4 + horizontalRotation.get2DDataValue();
	}

	/**
	 * A rotation around the z axis.
	 */
	public enum VerticalRotation implements StringRepresentable {
		DOWN("down", Direction.DOWN, 2),
		UP("up", Direction.UP, 0),
		SIDE("side", Direction.NORTH, 1);

		/**
		 * Get the value corresponding to the specified direction.
		 *
		 * @param direction The direction
		 * @return The value
		 */
		public static VerticalRotation fromDirection(final Direction direction) {
			return switch (direction) {
				case DOWN -> DOWN;
				case UP -> UP;
				default -> SIDE;
			};
		}

		/**
		 * The name.
		 */
		private final String name;

		/**
		 * The corresponding {@link Direction}.
		 */
		private final Direction direction;

		/**
		 * The angle of this rotation in radians
		 */
		private final double angle;

		/**
		 * Construct a new vertical rotation.
		 *
		 * @param name         The name
		 * @param direction    The corresponding direction
		 * @param numRotations The number of 90-degree rotations relative to {@link #SIDE}
		 */
		VerticalRotation(final String name, final Direction direction, final int numRotations) {
			this.name = name;
			this.direction = direction;
			angle = numRotations * Math.toRadians(90);
		}

		/**
		 * Get the name.
		 *
		 * @return The name
		 */
		@Override
		public String getSerializedName() {
			return name;
		}

		/**
		 * Get the corresponding {@link Direction}.
		 *
		 * @return The corresponding {@link Direction}
		 */
		public Direction getDirection() {
			return direction;
		}

		/**
		 * Get the angle in radians of this vertical rotation relative to {@link #SIDE}.
		 *
		 * @return The angle
		 */
		public double getAngle() {
			return angle;
		}

		/**
		 * Get the angle in degrees of this vertical rotation relative to {@link #SIDE}.
		 *
		 * @return The angle
		 */
		public double getAngleDegrees() {
			return Math.toDegrees(getAngle());
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
