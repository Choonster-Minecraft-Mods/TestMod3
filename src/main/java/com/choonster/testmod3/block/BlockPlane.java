package com.choonster.testmod3.block;

import com.choonster.testmod3.util.VectorUtils;
import com.google.common.collect.ImmutableMap;
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
import net.minecraft.item.ItemStack;
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
public class BlockPlane extends BlockTestMod3 {
	/**
	 * The block's rotation around the y-axis.
	 */
	public static final IProperty<EnumFacing> HORIZONTAL_ROTATION = PropertyDirection.create("horizontal_rotation", EnumFacing.Plane.HORIZONTAL);

	/**
	 * The block's rotation around the x-axis.
	 */
	public static final IProperty<EnumVerticalRotation> VERTICAL_ROTATION = PropertyEnum.create("vertical_rotation", EnumVerticalRotation.class);

	public BlockPlane(Material material, MapColor mapColor, String blockName) {
		super(material, mapColor, blockName);
	}

	public BlockPlane(Material materialIn, String blockName) {
		super(materialIn, blockName);
	}

	{
		fullBlock = false;
		setDefaultState(getBlockState().getBaseState().withProperty(HORIZONTAL_ROTATION, EnumFacing.NORTH).withProperty(VERTICAL_ROTATION, EnumVerticalRotation.UP));
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
		for (EnumFacing horizontalRotation : HORIZONTAL_ROTATION.getAllowedValues()) {
			for (EnumVerticalRotation verticalRotation : VERTICAL_ROTATION.getAllowedValues()) {
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

	@Override
	public IBlockState getStateFromMeta(int meta) {
		final EnumFacing horizontalRotation = EnumFacing.getHorizontal(meta & 3);
		final EnumVerticalRotation verticalRotation = EnumVerticalRotation.fromIndex(meta >> 2);

		return getDefaultState().withProperty(HORIZONTAL_ROTATION, horizontalRotation).withProperty(VERTICAL_ROTATION, verticalRotation);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		final int horizontalIndex = state.getValue(HORIZONTAL_ROTATION).getHorizontalIndex();
		final int verticalIndex = state.getValue(VERTICAL_ROTATION).getIndex();

		return (verticalIndex << 2) | horizontalIndex;
	}

	@Override
	public boolean rotateBlock(World world, BlockPos pos, EnumFacing axis) {
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		return rotateBlock(worldIn, pos, side);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		final EnumFacing horizontalRotation = placer.getHorizontalFacing();
		final EnumVerticalRotation verticalRotation = EnumVerticalRotation.fromFacing(facing);

		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
				.withProperty(HORIZONTAL_ROTATION, horizontalRotation)
				.withProperty(VERTICAL_ROTATION, verticalRotation);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@Override
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB mask, List<AxisAlignedBB> list, Entity p_185477_6_) {
		final Pair<EnumFacing, EnumVerticalRotation> key = Pair.of(state.getValue(HORIZONTAL_ROTATION), state.getValue(VERTICAL_ROTATION));
		final Pair<AxisAlignedBB, AxisAlignedBB> boundingBoxes = ROTATED_BOUNDING_BOXES.get(key);

		final AxisAlignedBB baseBoundingBox = boundingBoxes.getLeft();
		addCollisionBoxToList(pos, mask, list, baseBoundingBox);

		final AxisAlignedBB topBoundingBox = boundingBoxes.getRight();
		addCollisionBoxToList(pos, mask, list, topBoundingBox);
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
			for (EnumVerticalRotation verticalRotation : values()) {
				VALUES[verticalRotation.getIndex()] = verticalRotation;
			}
		}

		/**
		 * Get the value with the specified index.
		 *
		 * @param index The index
		 * @return The value
		 */
		public static EnumVerticalRotation fromIndex(int index) {
			return VALUES[MathHelper.abs_int(index % VALUES.length)];
		}

		/**
		 * Get the value corresponding to the specified facing.
		 *
		 * @param facing The facing
		 * @return The value
		 */
		public static EnumVerticalRotation fromFacing(EnumFacing facing) {
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
		EnumVerticalRotation(int index, String name, EnumFacing facing, int numRotations) {
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
