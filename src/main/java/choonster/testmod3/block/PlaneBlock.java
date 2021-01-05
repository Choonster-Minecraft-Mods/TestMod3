package choonster.testmod3.block;

import choonster.testmod3.util.VectorUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;

import javax.annotation.Nullable;
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
	 * The block's rotation around the y axis.
	 */
	public static final Property<Direction> HORIZONTAL_ROTATION = DirectionProperty.create("horizontal_rotation", Direction.Plane.HORIZONTAL);

	/**
	 * The block's rotation around the z axis.
	 */
	public static final Property<VerticalRotation> VERTICAL_ROTATION = EnumProperty.create("vertical_rotation", VerticalRotation.class);

	public PlaneBlock(final Block.Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(HORIZONTAL_ROTATION, Direction.NORTH).with(VERTICAL_ROTATION, VerticalRotation.UP));
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
		final List<AxisAlignedBB> boundingBoxes = IntStream.range(0, numBoundingBoxes)
				.mapToObj(i -> new AxisAlignedBB(0, i * increment, 0, 1, (i + 1) * increment, 1 - (i * increment)))
				.collect(Collectors.toList());

		// For each horizontal and vertical rotation pair,
		for (final Direction horizontalRotation : HORIZONTAL_ROTATION.getAllowedValues()) {
			for (final VerticalRotation verticalRotation : VERTICAL_ROTATION.getAllowedValues()) {
				// Get the horizontal (around the y axis) rotation angle and quaternion
				// Needs to be negated to perform correct rotation.
				final double horizontalRotationAngle = -VectorUtils.getHorizontalRotation(horizontalRotation);
				final Quaternion horizontalRotationQuaternion = VectorUtils.getRotationQuaternion(Direction.Axis.Y, (float) horizontalRotationAngle);

				// Get the vertical (around the z axis) rotation angle and quaternion
				// Needs to be negated to perform correct rotation.
				final double verticalRotationAngle = -verticalRotation.getAngle();
				final Quaternion verticalRotationQuaternion = VectorUtils.getRotationQuaternion(Direction.Axis.Z, (float) verticalRotationAngle);

				final Quaternion combinedRotationQuaternion = new Quaternion(horizontalRotationQuaternion);
				combinedRotationQuaternion.multiply(verticalRotationQuaternion);

				final VoxelShape combinedShape = boundingBoxes
						.stream()
						.map(aabb -> VectorUtils.rotateAABB(aabb, combinedRotationQuaternion)) // Rotate the AABBs
						.map(VectorUtils::adjustAABBForVoxelShape) // Round/offset them
						.map(VoxelShapes::create) // Convert them to VoxelShapes
						.reduce(VoxelShapes.empty(), VoxelShapes::or); // Combine them into a single VoxelShape

				// Add the combined VoxelShape to the array
				combinedShapes[getShapeIndex(horizontalRotation, verticalRotation)] = combinedShape;
			}
		}

		return combinedShapes;
	});

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_ROTATION, VERTICAL_ROTATION);
	}

	@Override
	public BlockState rotate(final BlockState state, final IWorld world, final BlockPos pos, final Rotation direction) {
		return state.with(HORIZONTAL_ROTATION, direction.rotate(state.get(HORIZONTAL_ROTATION)));
	}

	@SuppressWarnings("deprecation")
	@Override
	public BlockState mirror(final BlockState state, final Mirror mirror) {
		return state.with(HORIZONTAL_ROTATION, mirror.mirror(state.get(HORIZONTAL_ROTATION)));
	}

	private static ActionResultType rotateBlock(final World world, final BlockPos pos, final Direction axis) {
		final Direction.Axis axisToRotate = axis.getAxis();

		BlockState state = world.getBlockState(pos);

		switch (axisToRotate) {
			case X:
			case Z:
				state = state./* cycle */func_235896_a_(VERTICAL_ROTATION);
				break;
			case Y:
				final Direction originalRotation = state.get(HORIZONTAL_ROTATION);
				final Direction newRotation = axis.getAxisDirection() == Direction.AxisDirection.POSITIVE ? originalRotation.rotateY() : originalRotation.rotateYCCW();
				state = state.with(HORIZONTAL_ROTATION, newRotation);
				break;
		}

		return world.setBlockState(pos, state) ? ActionResultType.SUCCESS : ActionResultType.FAIL;
	}

	@SuppressWarnings("deprecation")
	@Override
	public ActionResultType onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		return rotateBlock(world, pos, rayTraceResult.getFace());
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(final BlockItemUseContext context) {
		final Direction horizontalRotation = context.getPlacementHorizontalFacing();
		final VerticalRotation verticalRotation = VerticalRotation.fromDirection(context.getFace());

		return getDefaultState()
				.with(HORIZONTAL_ROTATION, horizontalRotation)
				.with(VERTICAL_ROTATION, verticalRotation);
	}


	@SuppressWarnings("deprecation")
	@Override
	public VoxelShape getShape(final BlockState state, final IBlockReader worldIn, final BlockPos pos, final ISelectionContext context) {
		return SHAPES[getShapeIndex(state.get(HORIZONTAL_ROTATION), state.get(VERTICAL_ROTATION))];
	}

	private static int getShapeIndex(final Direction horizontalRotation, final VerticalRotation verticalRotation) {
		return verticalRotation.ordinal() * 4 + horizontalRotation.getHorizontalIndex();
	}

	/**
	 * A rotation around the z axis.
	 */
	public enum VerticalRotation implements IStringSerializable {
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
			switch (direction) {
				case DOWN:
					return DOWN;
				case UP:
					return UP;
				default:
					return SIDE;
			}
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
		public String getString() {
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
