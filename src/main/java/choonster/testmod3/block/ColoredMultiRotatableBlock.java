package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.IProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

/**
 * A block with 16 colours, 6 facings and 4 face rotations.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35055.0.html
 *
 * @author Choonster
 */
public class ColoredMultiRotatableBlock extends ColoredRotatableBlock {
	public static final IProperty<EnumFaceRotation> FACE_ROTATION = EnumProperty.create("face_rotation", EnumFaceRotation.class);

	public ColoredMultiRotatableBlock(final DyeColor color, final BlockVariantGroup<DyeColor, ? extends ColoredMultiRotatableBlock> variantGroup, final Block.Properties properties) {
		super(color, variantGroup, properties);
	}

	@Override
	protected void fillStateContainer(final StateContainer.Builder<Block, BlockState> builder) {
		super.fillStateContainer(builder);
		builder.add(FACE_ROTATION);
	}

	@Override
	protected BlockState copyState(final BlockState currentState, final BlockState newState) {
		return super.copyState(currentState, newState).with(FACE_ROTATION, currentState.get(FACE_ROTATION));
	}

	public void rotateFace(final World world, final BlockPos pos) {
		final EnumFaceRotation faceRotation = world.getBlockState(pos).get(FACE_ROTATION);
		final BlockState newState = world.getBlockState(pos).with(FACE_ROTATION, faceRotation.rotateClockwise());

		world.setBlockState(pos, newState, Constants.BlockFlags.DEFAULT_FLAGS);
	}

	@Override
	public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		if (player.isSneaking()) { // If the player is sneaking, rotate the face
			rotateFace(world, pos);
			return true;
		} else { // Else rotate or recolour the block
			return super.onBlockActivated(state, world, pos, player, hand, rayTraceResult);
		}
	}

}
