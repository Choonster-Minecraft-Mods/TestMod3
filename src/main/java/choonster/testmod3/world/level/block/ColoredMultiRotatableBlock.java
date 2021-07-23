package choonster.testmod3.world.level.block;

import choonster.testmod3.util.EnumFaceRotation;
import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.BlockHitResult;
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
	protected void createBlockStateDefinition(final StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(FACE_ROTATION);
	}

	@Override
	protected BlockState copyState(final BlockState currentState, final BlockState newState) {
		return super.copyState(currentState, newState).setValue(FACE_ROTATION, currentState.getValue(FACE_ROTATION));
	}

	public void rotateFace(final Level world, final BlockPos pos) {
		final EnumFaceRotation faceRotation = world.getBlockState(pos).getValue(FACE_ROTATION);
		final BlockState newState = world.getBlockState(pos).setValue(FACE_ROTATION, faceRotation.rotateClockwise());

		world.setBlock(pos, newState, Constants.BlockFlags.DEFAULT);
	}

	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult rayTraceResult) {
		if (player.isShiftKeyDown()) { // If the player is sneaking, rotate the face
			rotateFace(world, pos);
			return InteractionResult.SUCCESS;
		} else { // Else rotate or recolour the block
			return super.use(state, world, pos, player, hand, rayTraceResult);
		}
	}
}
