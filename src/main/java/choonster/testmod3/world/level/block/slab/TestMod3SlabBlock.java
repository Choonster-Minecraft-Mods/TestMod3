package choonster.testmod3.world.level.block.slab;

import choonster.testmod3.world.level.block.variantgroup.IBlockVariantGroup;
import com.mojang.serialization.MapCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.SlabBlock;

import java.util.function.Supplier;

/**
 * Base class for this mod's slab blocks.
 *
 * @param <VARIANT> The variant type
 * @param <SLAB>    The slab type
 * @author Choonster
 */
public abstract class TestMod3SlabBlock<
		VARIANT extends Enum<VARIANT> & StringRepresentable,
		SLAB extends TestMod3SlabBlock<VARIANT, SLAB>
		> extends SlabBlock {

	/**
	 * The variant of this slab.
	 */
	protected final VARIANT variant;

	/**
	 * The group this slab belongs to.
	 */
	protected final Supplier<IBlockVariantGroup<VARIANT, SLAB>> variantGroup;

	/**
	 * The map codec for the variant group type.
	 */
	protected final MapCodec<IBlockVariantGroup<VARIANT, SLAB>> variantGroupMapCodec;

	/**
	 * Create a slab block.
	 *
	 * @param variant      The variant of this slab
	 * @param variantGroup The group this slab belongs to
	 * @param properties   The block properties of this slab
	 */
	public TestMod3SlabBlock(
			final VARIANT variant,
			final Supplier<IBlockVariantGroup<VARIANT, SLAB>> variantGroup,
			final MapCodec<IBlockVariantGroup<VARIANT, SLAB>> variantGroupMapCodec,
			final Properties properties
	) {
		super(properties);

		this.variant = variant;
		this.variantGroup = variantGroup;
		this.variantGroupMapCodec = variantGroupMapCodec;
	}

	@Override
	public abstract MapCodec<? extends SLAB> codec();

	public VARIANT getVariant() {
		return variant;
	}
}
