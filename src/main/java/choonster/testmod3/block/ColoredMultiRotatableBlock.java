package choonster.testmod3.block;

import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.DyeColor;
import net.minecraft.state.EnumProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

/**
 * A block with 16 colours, 6 facings and 4 face rotations.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35055.0.html
 *
 * @author Choonster
 */
public class ColoredMultiRotatableBlock extends ColoredRotatableBlock {
	public static final Property<EnumFaceRotation> FACE_ROTATION = EnumProperty.create("face_rotation", EnumFaceRotation.class);

	public ColoredMultiRotatableBlock(final DyeColor color, final BlockVariantGroup<DyeColor, ? extends ColoredMultiRotatableBlock> variantGroup, final Block.Properties properties) {
		super(color, variantGroup, properties);
	}

	@Override
	protected void createBlockStateDefinition(final StateContainer.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACE_ROTATION);
	}

	@Override
	protected BlockState copyState(final BlockState currentState, final BlockState newState) {
		return super.copyState(currentState, newState).setValue(FACE_ROTATION, currentState.getValue(FACE_ROTATION));
	}

	public void rotateFace(final World world, final BlockPos pos) {
		final EnumFaceRotation faceRotation = world.getBlockState(pos).getValue(FACE_ROTATION);
		final BlockState newState = world.getBlockState(pos).setValue(FACE_ROTATION, faceRotation.rotateClockwise());

		world.setBlock(pos, newState, Constants.BlockFlags.DEFAULT);
	}

	@Override
	public ActionResultType use(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		if (player.isShiftKeyDown()) { // If the player is sneaking, rotate the face
			rotateFace(world, pos);
			return ActionResultType.SUCCESS;
		} else { // Else rotate or recolour the block
			return super.use(state, world, pos, player, hand, rayTraceResult);
		}
	}
}
