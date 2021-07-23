package choonster.testmod3.world.level.block.slab;

import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;

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
	protected final BlockVariantGroup<VARIANT, SLAB> variantGroup;

	/**
	 * Create a slab block.
	 *
	 * @param variant      The variant of this slab
	 * @param variantGroup The group this slab belongs to
	 * @param properties   The block properties of this slab
	 */
	public TestMod3SlabBlock(final VARIANT variant, final BlockVariantGroup<VARIANT, SLAB> variantGroup, final Block.Properties properties) {
		super(properties);

		this.variant = variant;
		this.variantGroup = variantGroup;
	}

	public VARIANT getVariant() {
		return variant;
	}
}
