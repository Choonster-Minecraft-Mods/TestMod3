package choonster.testmod3.world.level.block.slab;

import choonster.testmod3.world.level.block.variantgroup.IBlockVariantGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A slab that uses vanilla's dye colours.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2597500-how-do-you-create-a-halfslab
 *
 * @author Choonster
 */
public class ColouredSlabBlock extends TestMod3SlabBlock<DyeColor, ColouredSlabBlock> {
	public static Codec<ColouredSlabBlock> codec(
			final Supplier<IBlockVariantGroup<DyeColor, ColouredSlabBlock>> variantGroupSupplier,
			final MapCodec<IBlockVariantGroup<DyeColor, ColouredSlabBlock>> variantGroupMapCodec
	) {
		return RecordCodecBuilder.create(instance ->
				instance.group(
						DyeColor.CODEC
								.fieldOf("variant")
								.forGetter(TestMod3SlabBlock::getVariant),

						Codec.unit(() -> variantGroupSupplier)
								.fieldOf("variantGroup")
								.forGetter(block -> block.variantGroup),

						Codec.unit(() -> variantGroupMapCodec)
								.fieldOf("variantGroupMapCodec")
								.forGetter(block -> block.variantGroupMapCodec),

						propertiesCodec()
				).apply(instance, ColouredSlabBlock::new)
		);
	}

	private final MapCodec<ColouredSlabBlock> codec;

	/**
	 * Create a coloured slab block.
	 *
	 * @param variant      The variant of this slab
	 * @param variantGroup The group this slab belongs to
	 * @param properties   The block properties of this slab
	 */
	public ColouredSlabBlock(
			final DyeColor variant,
			final Supplier<IBlockVariantGroup<DyeColor, ColouredSlabBlock>> variantGroup,
			final MapCodec<IBlockVariantGroup<DyeColor, ColouredSlabBlock>> variantGroupMapCodec,
			final Properties properties
	) {
		super(variant, variantGroup, variantGroupMapCodec, properties);

		codec = IBlockVariantGroup.blockMapCodec(
				variantGroupMapCodec,
				block -> block.variantGroup.get(),
				variant
		);
	}

	@Override
	public MapCodec<ColouredSlabBlock> codec() {
		return codec;
	}

	private boolean recolorBlock(final BlockState state, final LevelAccessor world, final BlockPos pos, final Direction facing, final DyeColor colour) {
		final var newBlock = Objects.requireNonNull(variantGroup.get().getBlock(colour));
		final var newState = newBlock.get().defaultBlockState()
				.setValue(TYPE, state.getValue(TYPE));

		return world.setBlock(pos, newState, 3);
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
		if (!heldItem.isEmpty()) {
			final var dyeColour = DyeColor.getColor(heldItem);
			if (dyeColour != null) {
				final var success = recolorBlock(state, level, pos, blockHitResult.getDirection(), dyeColour);
				if (success) {
					heldItem.shrink(1);
					return InteractionResult.SUCCESS;
				}
			}
		}

		return InteractionResult.FAIL;
	}
}
