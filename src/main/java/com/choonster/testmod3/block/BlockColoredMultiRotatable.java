package com.choonster.testmod3.block;

import com.choonster.testmod3.tileentity.TileEntityColoredMultiRotatable;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * A block with 16 colours (stored in the metadata), 6 facings and 4 face rotations (stored in the TileEntity).
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35055.0.html
 *
 * @author Choonster
 */
public class BlockColoredMultiRotatable extends BlockColoredRotatable {
	public static final IProperty<EnumFaceRotation> FACE_ROTATION = PropertyEnum.create("face_rotation", EnumFaceRotation.class);

	public BlockColoredMultiRotatable(Material materialIn, String blockName) {
		super(materialIn, blockName);
	}

	@Override
	protected BlockState createBlockState() {
		return new BlockState(this, COLOR, FACING, FACE_ROTATION);
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityColoredMultiRotatable();
	}

	@Override
	public TileEntityColoredMultiRotatable getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TileEntityColoredMultiRotatable) super.getTileEntity(world, pos);
	}

	public EnumFaceRotation getFaceRotation(IBlockAccess world, BlockPos pos) {
		return getTileEntity(world, pos).getFaceRotation();
	}

	public void setFaceRotation(IBlockAccess world, BlockPos pos, EnumFaceRotation faceRotation) {
		getTileEntity(world, pos).setFaceRotation(faceRotation);
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos).withProperty(FACE_ROTATION, getFaceRotation(worldIn, pos));
	}

	public boolean rotateFace(World world, BlockPos pos) {
		EnumFaceRotation faceRotation = getFaceRotation(world, pos);
		setFaceRotation(world, pos, faceRotation.rotateClockwise());
		world.markBlockForUpdate(pos);

		return true;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		ItemStack heldItem = playerIn.getHeldItem();
		if (heldItem != null && heldItem.getItem() == Items.dye) { // If the player is holding dye, change the colour
			EnumDyeColor color = EnumDyeColor.byDyeDamage(heldItem.getMetadata());
			return worldIn.setBlockState(pos, state.withProperty(COLOR, color));
		} else if (playerIn.isSneaking()) { // If the player is sneaking, rotate the face
			return rotateFace(worldIn, pos);
		} else { // Else rotate the block
			return rotateBlock(worldIn, pos, side);
		}
	}

	public enum EnumFaceRotation implements IStringSerializable {
		UP("up"),
		RIGHT("right"),
		DOWN("down"),
		LEFT("left");

		private static final EnumFaceRotation[] VALUES = values();

		private final String name;

		EnumFaceRotation(String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public String toString() {
			return name;
		}

		public EnumFaceRotation rotateClockwise() {
			return VALUES[(ordinal() + 1) % VALUES.length];
		}

		public EnumFaceRotation rotateCounterClockwise() {
			return VALUES[(ordinal() - 1) % VALUES.length];
		}
	}
}
