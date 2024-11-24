package choonster.testmod3.world.level.block.variantgroup;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

/**
 * A factory function used to create a block codec based on a variant group and variant group map codec.
 *
 * @author Choonster
 */
@FunctionalInterface
public interface IBlockCodecFactory<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> {
	Codec<BLOCK> getBlockCodec(
			Supplier<IBlockVariantGroup<VARIANT, BLOCK>> variantGroupSupplier,
			MapCodec<IBlockVariantGroup<VARIANT, BLOCK>> variantGroupMapCodec
	);
}
