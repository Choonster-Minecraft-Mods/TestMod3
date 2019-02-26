package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * A block with 16 colours, 6 facings and 4 face rotations.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35055.0.html
 *
 * @author Choonster
 */
public class BlockColoredMultiRotatable extends BlockColoredRotatable {
	public static final IProperty<EnumFaceRotation> FACE_ROTATION = EnumProperty.create("face_rotation", EnumFaceRotation.class);

	public BlockColoredMultiRotatable(final Block.Properties properties, final EnumDyeColor color, final BlockVariantGroup<EnumDyeColor, ? extends BlockColoredMultiRotatable> variantGroup) {
		super(properties, color, variantGroup);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, IBlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACE_ROTATION);
	}

	@Override
	protected IBlockState copyState(final IBlockState currentState, final IBlockState newState) {
		return super.copyState(currentState, newState).with(FACE_ROTATION, currentState.get(FACE_ROTATION));
	}

	public void rotateFace(final World world, final BlockPos pos) {
		final EnumFaceRotation faceRotation = world.getBlockState(pos).get(FACE_ROTATION);
		final IBlockState newState = world.getBlockState(pos).with(FACE_ROTATION, faceRotation.rotateClockwise());

		world.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT_FLAGS);
	}

	@Override
	public boolean onBlockActivated(final IBlockState state, final World world, final BlockPos pos, final EntityPlayer player, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		if (player.isSneaking()) { // If the player is sneaking, rotate the face
			rotateFace(world, pos);
			return true;
		} else { // Else rotate or recolour the block
			return super.onBlockActivated(state, world, pos, player, hand, side, hitX, hitY, hitZ);
		}
	}

}
