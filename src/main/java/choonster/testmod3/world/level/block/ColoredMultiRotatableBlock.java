package choonster.testmod3.world.level.block;

import choonster.testmod3.util.EnumFaceRotation;
import choonster.testmod3.world.level.block.variantgroup.IBlockVariantGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;

import java.util.function.Supplier;

/**
 * A block with 16 colours, 6 facings and 4 face rotations.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,35055.0.html
 *
 * @author Choonster
 */
public class ColoredMultiRotatableBlock extends BaseColoredRotatableBlock<ColoredMultiRotatableBlock> {
	public static final EnumProperty<EnumFaceRotation> FACE_ROTATION = EnumProperty.create("face_rotation", EnumFaceRotation.class);

	public static Codec<ColoredMultiRotatableBlock> codec(
			final Supplier<IBlockVariantGroup<DyeColor, ColoredMultiRotatableBlock>> variantGroupSupplier,
			final MapCodec<IBlockVariantGroup<DyeColor, ColoredMultiRotatableBlock>> variantGroupMapCodec
	) {
		return RecordCodecBuilder.create(instance ->
				instance.group(
						DyeColor.CODEC
								.fieldOf("color")
								.forGetter(BaseColoredRotatableBlock::getColor),

						MapCodec.unitCodec(() -> variantGroupSupplier)
								.fieldOf("variantGroup")
								.forGetter(block -> block.variantGroup),

						MapCodec.unitCodec(() -> variantGroupMapCodec)
								.fieldOf("variantGroupMapCodec")
								.forGetter(block -> block.variantGroupMapCodec),

						propertiesCodec()
				).apply(instance, ColoredMultiRotatableBlock::new)
		);
	}

	private final MapCodec<ColoredMultiRotatableBlock> codec;

	public ColoredMultiRotatableBlock(
			final DyeColor color,
			final Supplier<IBlockVariantGroup<DyeColor, ColoredMultiRotatableBlock>> variantGroup,
			final MapCodec<IBlockVariantGroup<DyeColor, ColoredMultiRotatableBlock>> variantGroupMapCodec,
			final Block.Properties properties
	) {
		super(color, variantGroup, variantGroupMapCodec, properties);

		codec = IBlockVariantGroup.blockMapCodec(
				variantGroupMapCodec,
				BaseColoredRotatableBlock::getVariantGroup,
				color
		);
	}

	@Override
	protected MapCodec<? extends Block> codec() {
		return codec;
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

		world.setBlock(pos, newState, Block.UPDATE_ALL);
	}

	@Override
	protected InteractionResult useItemOn(
			final ItemStack heldItem,
			final BlockState state,
			final Level level,
			final BlockPos pos,
			final Player player,
			final InteractionHand hand,
			final BlockHitResult blockHitResult
	) {
		if (player.isShiftKeyDown()) { // If the player is sneaking, rotate the face
			rotateFace(level, pos);
			return InteractionResult.SUCCESS;
		} else { // Else rotate or recolour the block
			return super.useItemOn(heldItem, state, level, pos, player, hand, blockHitResult);
		}
	}
}
