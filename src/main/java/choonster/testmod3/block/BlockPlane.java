package choonster.testmod3.block;

import choonster.testmod3.util.VectorUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.*;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
public class BlockPlane extends Block {
	/**
	 * The block's rotation around the y-axis.
	 */
	public static final IProperty<EnumFacing> HORIZONTAL_ROTATION = DirectionProperty.create("horizontal_rotation", EnumFacing.Plane.HORIZONTAL);

	/**
	 * The block's rotation around the x-axis.
	 */
	public static final IProperty<EnumVerticalRotation> VERTICAL_ROTATION = EnumProperty.create("vertical_rotation", EnumVerticalRotation.class);

	public BlockPlane(final Properties properties) {
		super(properties);
		setDefaultState(getStateContainer().getBaseState().with(HORIZONTAL_ROTATION, EnumFacing.NORTH).with(VERTICAL_ROTATION, EnumVerticalRotation.UP));
	}

	/**
	 * The bounding boxes for each rotation.
	 * <ul>
	 * <li>Key: Left = Horizontal Rotation, Right = Vertical Rotation</li>
	 * <li>Value: Left = Base Bounding Box, Right = Top Bounding Box</li>
	 * </ul>
	 */
	// TODO: Convert this to VoxelShapes
	private static final Map<Pair<EnumFacing, EnumVerticalRotation>, Pair<AxisAlignedBB, AxisAlignedBB>> ROTATED_BOUNDING_BOXES = Util.make(() -> {
		final ImmutableMap.Builder<Pair<EnumFacing, EnumVerticalRotation>, Pair<AxisAlignedBB, AxisAlignedBB>> builder = ImmutableMap.builder();

		// The base and top AABBs for the default rotation pair
		final AxisAlignedBB baseBoundingBox = new AxisAlignedBB(0, 0, 0, 1, 0.5, 1);
		final AxisAlignedBB topBoundingBox = new AxisAlignedBB(0, 0.5, 0, 1, 1, 0.5);

		// For each horizontal and vertical rotation pair,
		for (final EnumFacing horizontalRotation : HORIZONTAL_ROTATION.getAllowedValues()) {
			for (final EnumVerticalRotation verticalRotation : VERTICAL_ROTATION.getAllowedValues()) {
				// Get the horizontal (around the y axis) rotation angle and matrix
				final double horizontalRotationAngle = VectorUtils.getHorizontalRotation(horizontalRotation);
				final Matrix3d horizontalRotationMatrix = VectorUtils.getRotationMatrix(EnumFacing.Axis.Y, verticalRotation == EnumVerticalRotation.DOWN ? horizontalRotationAngle + Math.PI : horizontalRotationAngle);

				// Get the vertical (around the x axis) rotation angle and matrix
				final double verticalRotationAngle = verticalRotation.getAngle();
				final Matrix3d verticalRotationMatrix = VectorUtils.getRotationMatrix(EnumFacing.Axis.X, verticalRotationAngle);

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
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		builder.add(HORIZONTAL_ROTATION, VERTICAL_ROTATION);
	}

	@Override
	public IBlockState rotate(final IBlockState state, final IWorld world, final BlockPos pos, final Rotation direction) {
		return state.with(HORIZONTAL_ROTATION, direction.rotate(state.get(HORIZONTAL_ROTATION)));
	}

	@Override
	public IBlockState mirror(final IBlockState state, final Mirror mirror) {
		return state.with(HORIZONTAL_ROTATION, mirror.mirror(state.get(HORIZONTAL_ROTATION)));
	}

	private static boolean rotateBlock(final World world, final BlockPos pos, final EnumFacing axis) {
		final EnumFacing.Axis axisToRotate = axis.getAxis();

		IBlockState state = world.getBlockState(pos);

		switch (axisToRotate) {
			case X:
			case Z:
				state = state.cycle(VERTICAL_ROTATION);
				break;
			case Y:
				final EnumFacing originalRotation = state.get(HORIZONTAL_ROTATION);
				final EnumFacing newRotation = axis.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? originalRotation.rotateY() : originalRotation.rotateYCCW();
				state = state.with(HORIZONTAL_ROTATION, newRotation);
				break;
		}

		return world.setBlockState(pos, state);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		return rotateBlock(world, pos, side);
	}

	@Nullable
	@Override
	public IBlockState getStateForPlacement(final BlockItemUseContext context) {
		final EnumFacing horizontalRotation = context.getPlacementHorizontalFacing();
		final EnumVerticalRotation verticalRotation = EnumVerticalRotation.fromFacing(context.getFace());

		return getDefaultState()
				.with(HORIZONTAL_ROTATION, horizontalRotation)
				.with(VERTICAL_ROTATION, verticalRotation);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	/*
	// TODO: Convert this to VoxelShapes
	@SuppressWarnings("deprecation")
	public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
		final Pair<EnumFacing, EnumVerticalRotation> key = Pair.of(state.getValue(HORIZONTAL_ROTATION), state.getValue(VERTICAL_ROTATION));
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
	public enum EnumVerticalRotation implements IStringSerializable {
		DOWN("down", EnumFacing.DOWN, 2),
		UP("up", EnumFacing.UP, 0),
		SIDE("side", EnumFacing.NORTH, 1);

		/**
		 * Get the value corresponding to the specified facing.
		 *
		 * @param facing The facing
		 * @return The value
		 */
		public static EnumVerticalRotation fromFacing(final EnumFacing facing) {
			switch (facing) {
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
		 * The corresponding {@link EnumFacing}.
		 */
		private final EnumFacing facing;

		/**
		 * The angle of this rotation in radians
		 */
		private final double angle;

		/**
		 * Construct a new vertical rotation.
		 *
		 * @param name         The name
		 * @param facing       The corresponding facing
		 * @param numRotations The number of 90-degree rotations relative to {@link #SIDE}
		 */
		EnumVerticalRotation(final String name, final EnumFacing facing, final int numRotations) {
			this.name = name;
			this.facing = facing;
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
		 * Get the corresponding {@link EnumFacing}.
		 *
		 * @return The corresponding {@link EnumFacing}
		 */
		public EnumFacing getFacing() {
			return facing;
		}

		/**
		 * Get the angle of this vertical rotation relative to {@link #SIDE}.
		 *
		 * @return The angle
		 */
		public double getAngle() {
			return angle;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
