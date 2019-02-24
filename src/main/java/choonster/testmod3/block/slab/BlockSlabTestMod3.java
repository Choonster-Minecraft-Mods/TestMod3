package choonster.testmod3.block.slab;

import choonster.testmod3.block.variantgroup.SlabVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.util.IStringSerializable;

/**
 * Base class for this mod's slab blocks.
 *
 * @param <VARIANT> The variant type
 * @param <SLAB>    The slab type
 * @author Choonster
 */
public abstract class BlockSlabTestMod3<
		VARIANT extends Enum<VARIANT> & IStringSerializable,
		SLAB extends BlockSlabTestMod3<VARIANT, SLAB>
		> extends BlockSlab {

	/**
	 * The variant of this slab.
	 */
	protected final VARIANT variant;

	/**
	 * The group this slab belongs to.
	 */
	protected final SlabVariantGroup<VARIANT, SLAB>.SlabGroup slabGroup;

	/**
	 * Create a slab block.
	 *
	 * @param variant    The variant of this slab
	 * @param properties The block properties of this slab
	 * @param slabGroup  The group this slab belongs to
	 */
	public BlockSlabTestMod3(final VARIANT variant, final Block.Properties properties, final SlabVariantGroup<VARIANT, SLAB>.SlabGroup slabGroup) {
		super(properties);

		this.variant = variant;
		this.slabGroup = slabGroup;
	}
}
