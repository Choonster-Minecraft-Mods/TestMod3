package choonster.testmod3.block.slab;

import net.minecraft.util.IStringSerializable;

/**
 * A container that holds one ore more {@link SlabGroup}s.
 *
 * @param <VARIANT>  The variant type
 * @param <VARIANTS> The variant collection type
 * @param <SLAB>     The slab type
 */
// TODO: Will this still be required with the removal of block metadata in 1.13?
public interface ISlabGroupContainer<
		VARIANT extends Enum<VARIANT> & IStringSerializable,
		VARIANTS extends Iterable<VARIANT> & IStringSerializable,
		SLAB extends BlockSlabTestMod3<VARIANT, VARIANTS, SLAB>
		> {

	/**
	 * Get the slab group containing the specified variant.
	 *
	 * @param variant The variant
	 * @return The slab group
	 */
	SlabGroup<VARIANT, VARIANTS, SLAB> getSlabGroupForVariant(VARIANT variant);
}
