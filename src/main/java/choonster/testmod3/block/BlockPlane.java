package choonster.testmod3.block;

import choonster.testmod3.util.VectorUtils;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix3d;
import java.util.List;
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
	public static final IProperty<EnumFacing> HORIZONTAL_ROTATION = PropertyDirection.create("horizontal_rotation", EnumFacing.Plane.HORIZONTAL);

	/**
	 * The block's rotation around the x-axis.
	 */
	public static final IProperty<EnumVerticalRotation> VERTICAL_ROTATION = PropertyEnum.create("vertical_rotation", EnumVerticalRotation.class);

	public BlockPlane(final Material material, final MapColor mapColor) {
		super(material, mapColor);
		setDefaultState(getBlockState().getBaseState().withProperty(HORIZONTAL_ROTATION, EnumFacing.NORTH).withProperty(VERTICAL_ROTATION, EnumVerticalRotation.UP));
	}

	public BlockPlane(final Material materialIn) {
		this(materialIn, materialIn.getMaterialMapColor());
	}

	/**
	 * The bounding boxes for each rotation.
	 * <ul>
	 * <li>Key: Left = Horizontal Rotation, Right = Vertical Rotation</li>
	 * <li>Value: Left = Base Bounding Box, Right = Top Bounding Box</li>
	 * </ul>
	 */
	private static final Map<Pair<EnumFacing, EnumVerticalRotation>, Pair<AxisAlignedBB, AxisAlignedBB>> ROTATED_BOUNDING_BOXES;

	static {
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

		ROTATED_BOUNDING_BOXES = builder.build();
	}

	/**
	 * The default bounding box.
	 */
	private static final AxisAlignedBB DEFAULT_BOUNDING_BOX = new AxisAlignedBB(0, 0, 0, 1, 1, 1);

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HORIZONTAL_ROTATION, VERTICAL_ROTATION);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(final int meta) {
		final EnumFacing horizontalRotation = EnumFacing.byHorizontalIndex(meta & 3);
		final EnumVerticalRotation verticalRotation = EnumVerticalRotation.fromIndex(meta >> 2);

		return getDefaultState().withProperty(HORIZONTAL_ROTATION, horizontalRotation).withProperty(VERTICAL_ROTATION, verticalRotation);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {
		final int horizontalIndex = state.getValue(HORIZONTAL_ROTATION).getHorizontalIndex();
		final int verticalIndex = state.getValue(VERTICAL_ROTATION).getIndex();

		return (verticalIndex << 2) | horizontalIndex;
	}

	@Override
	public boolean rotateBlock(final World world, final BlockPos pos, final EnumFacing axis) {
		final EnumFacing.Axis axisToRotate = axis.getAxis();

		IBlockState state = world.getBlockState(pos);

		switch (axisToRotate) {
			case X:
			case Z:
				state = state.cycleProperty(VERTICAL_ROTATION);
				break;
			case Y:
				final EnumFacing originalRotation = state.getValue(HORIZONTAL_ROTATION);
				final EnumFacing newRotation = axis.getAxisDirection() == EnumFacing.AxisDirection.POSITIVE ? originalRotation.rotateY() : originalRotation.rotateYCCW();
				state = state.withProperty(HORIZONTAL_ROTATION, newRotation);
				break;
		}

		return world.setBlockState(pos, state);
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		return rotateBlock(worldIn, pos, side);
	}

	@Override
	public IBlockState getStateForPlacement(final World world, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer, final EnumHand hand) {
		final EnumFacing horizontalRotation = placer.getHorizontalFacing();
		final EnumVerticalRotation verticalRotation = EnumVerticalRotation.fromFacing(facing);

		return super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
				.withProperty(HORIZONTAL_ROTATION, horizontalRotation)
				.withProperty(VERTICAL_ROTATION, verticalRotation);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getRenderLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(final IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(final IBlockState blockState, final IBlockAccess blockAccess, final BlockPos pos, final EnumFacing side) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void addCollisionBoxToList(final IBlockState state, final World worldIn, final BlockPos pos, final AxisAlignedBB entityBox, final List<AxisAlignedBB> collidingBoxes, @Nullable final Entity entityIn, final boolean p_185477_7_) {
		final Pair<EnumFacing, EnumVerticalRotation> key = Pair.of(state.getValue(HORIZONTAL_ROTATION), state.getValue(VERTICAL_ROTATION));
		final Pair<AxisAlignedBB, AxisAlignedBB> boundingBoxes = ROTATED_BOUNDING_BOXES.get(key);

		final AxisAlignedBB baseBoundingBox = boundingBoxes.getLeft();
		addCollisionBoxToList(pos, entityBox, collidingBoxes, baseBoundingBox);

		final AxisAlignedBB topBoundingBox = boundingBoxes.getRight();
		addCollisionBoxToList(pos, entityBox, collidingBoxes, topBoundingBox);
	}

	/**
	 * A rotation around the x-axis.
	 */
	public enum EnumVerticalRotation implements IStringSerializable {
		DOWN(0, "down", EnumFacing.DOWN, 2),
		UP(1, "up", EnumFacing.UP, 0),
		SIDE(2, "side", EnumFacing.NORTH, 1);

		/**
		 * The values ordered by their index.
		 */
		private static final EnumVerticalRotation[] VALUES = new EnumVerticalRotation[values().length];

		static {
			for (final EnumVerticalRotation verticalRotation : values()) {
				VALUES[verticalRotation.getIndex()] = verticalRotation;
			}
		}

		/**
		 * Get the value with the specified index.
		 *
		 * @param index The index
		 * @return The value
		 */
		public static EnumVerticalRotation fromIndex(final int index) {
			return VALUES[MathHelper.abs(index % VALUES.length)];
		}

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
		 * The index.
		 */
		private final int index;

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
		 * @param index        The index
		 * @param name         The name
		 * @param facing       The corresponding facing
		 * @param numRotations The number of 90-degree rotations relative to {@link #SIDE}
		 */
		EnumVerticalRotation(final int index, final String name, final EnumFacing facing, final int numRotations) {
			this.index = index;
			this.name = name;
			this.facing = facing;
			this.angle = numRotations * Math.toRadians(90);
		}

		/**
		 * Get the index.
		 *
		 * @return The index
		 */
		public int getIndex() {
			return index;
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
