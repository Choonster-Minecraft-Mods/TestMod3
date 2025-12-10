package choonster.testmod3.world.level.block;

import choonster.testmod3.world.level.block.variantgroup.IBlockVariantGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * A block with 16 colours and 6 facings.
 *
 * @author Choonster
 */
public class ColoredRotatableBlock extends BaseColoredRotatableBlock<ColoredRotatableBlock> {
	public static Codec<ColoredRotatableBlock> codec(
			final Supplier<IBlockVariantGroup<DyeColor, ColoredRotatableBlock>> variantGroupSupplier,
			final MapCodec<IBlockVariantGroup<DyeColor, ColoredRotatableBlock>> variantGroupMapCodec
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
				).apply(instance, ColoredRotatableBlock::new)
		);
	}

	private final MapCodec<ColoredRotatableBlock> codec;

	public ColoredRotatableBlock(
			final DyeColor color,
			final Supplier<IBlockVariantGroup<DyeColor, ColoredRotatableBlock>> variantGroup,
			final MapCodec<IBlockVariantGroup<DyeColor, ColoredRotatableBlock>> variantGroupMapCodec,
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
	public MapCodec<ColoredRotatableBlock> codec() {
		return codec;
	}
}
