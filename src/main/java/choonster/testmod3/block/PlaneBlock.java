package choonster.testmod3.block;

import choonster.testmod3.util.VectorUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix3d;
import java.util.Map;

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
	public static final IProperty<Direction> HORIZONTAL_ROTATION = DirectionProperty.create("horizontal_rotation", Direction.Plane.HORIZONTAL);

	/**
	 * The block's rotation around the x-axis.
	 */
	public static final IProperty<VerticalRotation> VERTICAL_ROTATION = EnumProperty.create("vertical_rotation", VerticalRotation.class);

	public PlaneBlock(final Block.Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(HORIZONTAL_ROTATION, Direction.NORTH).with(VERTICAL_ROTATION, VerticalRotation.UP));
	}

	/**
	 * The bounding boxes for each rotation.
	 * <ul>
	 * <li>Key: Left = Horizontal Rotation, Right = Vertical Rotation</li>
	 * <li>Value: Left = Base Bounding Box, Right = Top Bounding Box</li>
	 * </ul>
	 */
	// TODO: Convert this to VoxelShapes
	private static final Map<Pair<Direction, VerticalRotation>, Pair<AxisAlignedBB, AxisAlignedBB>> ROTATED_BOUNDING_BOXES = Util.make(() -> {
		final ImmutableMap.Builder<Pair<Direction, VerticalRotation>, Pair<AxisAlignedBB, AxisAlignedBB>> builder = ImmutableMap.builder();

		// The base and top AABBs for the default rotation pair
		final AxisAlignedBB baseBoundingBox = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
		final AxisAlignedBB topBoundingBox = new AxisAlignedBB(0, 0.5, 0, 1, 1, 0.5);

		// For each horizontal and vertical rotation pair,
		for (final Direction horizontalRotation : HORIZONTAL_ROTATION.getAllowedValues()) {
			for (final VerticalRotation verticalRotation : VERTICAL_ROTATION.getAllowedValues()) {
				// Get the horizontal (around the y axis) rotation angle and matrix
				final double horizontalRotationAngle = VectorUtils.getHorizontalRotation(horizontalRotation);
				final Matrix3d horizontalRotationMatrix = VectorUtils.getRotationMatrix(Direction.Axis.Y, verticalRotation == VerticalRotation.DOWN ? horizontalRotationAngle + Math.PI : horizontalRotationAngle);

				// Get the vertical (around the x axis) rotation angle and matrix
				final double verticalRotationAngle = verticalRotation.getAngle();
				final Matrix3d verticalRotationMatrix = VectorUtils.getRotationMatrix(Direction.Axis.X, verticalRotationAngle);

				// Multiply the rotation matrices to combine them
				final Matrix3d combinedRotationMatrix = new Matrix3d();
				combinedRotationMatrix.mul(verticalRotationMatrix, horizontalRotationMatrix);

				// Rotate the AABBs
				final AxisAlignedBB rotatedBaseBoundingBox = VectorUtils.rotateAABB(baseBoundingBox, combinedRotationMatrix, true);
				final AxisAlignedBB rotatedTopBoundingBox = VectorUtils.rotateAABB(topBoundingBox, combinedRotationMatrix, true);

				// Add them to the map
				builder.put(Pair.of(horizontalRotation, verticalRotation), Pair.of(rotatedBaseBoundingBox, rotatedTopBoundingBox));
			}
		}

		return builder.build();
	});


	/**
	 * The default bounding box.
	 */
	private static final AxisAlignedBB DEFAULT_BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

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

	private static boolean rotateBlock(final World world, final BlockPos pos, final Direction axis) {
		final Direction.Axis axisToRotate = axis.getAxis();

		BlockState state = world.getBlockState(pos);

		switch (axisToRotate) {
			case X:
			case Z:
				state = state.cycle(VERTICAL_ROTATION);
				break;
			case Y:
				final Direction originalRotation = state.get(HORIZONTAL_ROTATION);
				final Direction newRotation = axis.getAxisDirection() == Direction.AxisDirection.POSITIVE ? originalRotation.rotateY() : originalRotation.rotateYCCW();
				state = state.with(HORIZONTAL_ROTATION, newRotation);
				break;
		}

		return world.setBlockState(pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
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

	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/*
	// TODO: Convert this to VoxelShapes
	@SuppressWarnings("deprecation")
	public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
		final Pair<Direction, EnumVerticalRotation> key = Pair.of(state.getValue(HORIZONTAL_ROTATION), state.getValue(VERTICAL_ROTATION));
		final Pair<AxisAlignedBB, AxisAlignedBB> boundingBoxes = ROTATED_BOUNDING_BOXES.get(key);

		final AxisAlignedBB baseBoundingBox = boundingBoxes.getLeft();
		addCollisionBoxToList(pos, entityBox, collidingBoxes, baseBoundingBox);

		final AxisAlignedBB topBoundingBox = boundingBoxes.getRight();
		addCollisionBoxToList(pos, entityBox, collidingBoxes, topBoundingBox);
	}
	*/

	/**
	 * A rotation around the x-axis.
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
		public String getName() {
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
