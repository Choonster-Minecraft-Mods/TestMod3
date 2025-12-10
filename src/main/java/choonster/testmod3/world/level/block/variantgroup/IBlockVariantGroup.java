package choonster.testmod3.world.level.block.variantgroup;

import choonster.testmod3.registry.IVariantGroup;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import org.jspecify.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A group consisting of a collection of variants with one or more blocks registered for each one.
 *
 * @author Choonster
 */
public interface IBlockVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> extends IVariantGroup<VARIANT, Block, BLOCK> {
	/**
	 * Creates a map codec for a variant group type.
	 *
	 * @param variants          The variants for the variant group
	 * @param variantCodec      The codec for the variant type
	 * @param blockCodecFactory The factory function used to create the block codec
	 * @param <VARIANT>         The variant type
	 * @param <BLOCK>           The block type
	 * @return The map codec
	 */
	static <
			VARIANT extends Enum<VARIANT> & StringRepresentable,
			BLOCK extends Block
			>
	MapCodec<IBlockVariantGroup<VARIANT, BLOCK>> mapCodec(
			final List<VARIANT> variants,
			final Codec<VARIANT> variantCodec,
			final IBlockCodecFactory<VARIANT, BLOCK> blockCodecFactory
	) {
		return new BlockVariantGroupMapCodec<>(variants, variantCodec, blockCodecFactory);
	}

	/**
	 * Creates a codec for a variant group type.
	 *
	 * @param variants          The variants for the variant group
	 * @param variantCodec      The codec for the variant type
	 * @param blockCodecFactory The factory function used to create the block codec
	 * @param <VARIANT>         The variant type
	 * @param <BLOCK>           The block type
	 * @return The codec
	 */
	static <
			VARIANT extends Enum<VARIANT> & StringRepresentable,
			BLOCK extends Block
			>
	Codec<IBlockVariantGroup<VARIANT, BLOCK>> codec(
			final List<VARIANT> variants,
			final Codec<VARIANT> variantCodec,
			final IBlockCodecFactory<VARIANT, BLOCK> blockCodecFactory
	) {
		return mapCodec(variants, variantCodec, blockCodecFactory).codec();
	}

	/**
	 * Creates a map codec for a block based on a variant group and variant.
	 *
	 * @param variantGroupCodec  The codec for the variant type
	 * @param variantGroupGetter A function to get the variant group from a block
	 * @param variant            The variant
	 * @param <VARIANT>          The variant type
	 * @param <BLOCK>            The block type
	 * @return The map codec
	 */
	static <
			VARIANT extends Enum<VARIANT> & StringRepresentable,
			BLOCK extends Block
			>
	MapCodec<BLOCK> blockMapCodec(
			final MapCodec<IBlockVariantGroup<VARIANT, BLOCK>> variantGroupCodec,
			final Function<BLOCK, IBlockVariantGroup<VARIANT, BLOCK>> variantGroupGetter,
			final VARIANT variant
	) {
		return variantGroupCodec.flatXmap(
				variantGroup ->
						Optional.ofNullable(variantGroup.getBlock(variant))
								.map(blockSupplier -> DataResult.success(blockSupplier.get()))
								.orElseGet(() -> DataResult.error(() -> "Block not present for variant " + variant)),
				block -> DataResult.success(variantGroupGetter.apply(block))
		);
	}

	/**
	 * Gets this group's blocks.
	 *
	 * @return The blocks
	 */
	default Collection<? extends Supplier<BLOCK>> getBlocks() {
		return getEntries();
	}


	/**
	 * Gets this group's blocks and their corresponding variants.
	 *
	 * @return The blocks map
	 */
	Map<VARIANT, ? extends Supplier<BLOCK>> getBlocksMap();

	/**
	 * Gets the block for the specified variant.
	 *
	 * @param variant The variant
	 * @return The block
	 */
	@Nullable
	Supplier<BLOCK> getBlock(VARIANT variant);
}
