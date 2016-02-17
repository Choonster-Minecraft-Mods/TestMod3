package com.choonster.testmod3.block;

import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
		setDefaultState(getBlockState().getBaseState().withProperty(HORIZONTAL_ROTATION, EnumFacing.NORTH).withProperty(VERTICAL_ROTATION, EnumVerticalRotation.UP));
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, HORIZONTAL_ROTATION, VERTICAL_ROTATION);
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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		return rotateBlock(worldIn, pos, side);
	}

	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		final EnumFacing horizontalRotation = placer.getHorizontalFacing().getOpposite();
		final EnumVerticalRotation verticalRotation = EnumVerticalRotation.fromFacing(facing);

		return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer)
				.withProperty(HORIZONTAL_ROTATION, horizontalRotation)
				.withProperty(VERTICAL_ROTATION, verticalRotation);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

	/**
	 * A rotation around the x-axis.
	 */
	public enum EnumVerticalRotation implements IStringSerializable {
		DOWN(0, "down", EnumFacing.DOWN),
		UP(1, "up", EnumFacing.UP),
		SIDE(2, "side", EnumFacing.NORTH);

		/**
		 * The values ordered by their index
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

		EnumVerticalRotation(int index, String name, EnumFacing facing) {
			this.index = index;
			this.name = name;
			this.facing = facing;
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
		 * Get the corresponding {@link EnumFacing}.
		 *
		 * @return The corresponding {@link EnumFacing}
		 */
		public EnumFacing getFacing() {
			return facing;
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

		@Override
		public String toString() {
			return name;
		}
	}
}
