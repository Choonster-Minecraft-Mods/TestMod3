package choonster.testmod3.block;

import choonster.testmod3.tileentity.TileEntityColoredMultiRotatable;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
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
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, COLOR, FACING, FACE_ROTATION);
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
		final TileEntityColoredMultiRotatable tileEntity = getTileEntity(world, pos);
		return tileEntity != null ? tileEntity.getFaceRotation() : EnumFaceRotation.UP;
	}

	public void setFaceRotation(IBlockAccess world, BlockPos pos, EnumFaceRotation faceRotation) {
		final TileEntityColoredMultiRotatable tileEntity = getTileEntity(world, pos);
		if (tileEntity != null) {
			tileEntity.setFaceRotation(faceRotation);
		}
	}

	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
		return super.getActualState(state, worldIn, pos).withProperty(FACE_ROTATION, getFaceRotation(worldIn, pos));
	}

	public void rotateFace(World world, BlockPos pos) {
		final EnumFaceRotation faceRotation = getFaceRotation(world, pos);
		setFaceRotation(world, pos, faceRotation.rotateClockwise());
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing side, float hitX, float hitY, float hitZ) {
		if (playerIn.isSneaking()) { // If the player is sneaking, rotate the face
			rotateFace(worldIn, pos);
			return true;
		} else { // Else rotate or recolour the block
			return super.onBlockActivated(worldIn, pos, state, playerIn, hand, side, hitX, hitY, hitZ);
		}
	}

}
