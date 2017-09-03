package choonster.testmod3.block.slab;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.IStringSerializable;

/**
 * A group consisting of a single and a double slab.
 *
 * @param <VARIANT>  The variant type
 * @param <VARIANTS> The variant collection type
 * @param <SLAB>     The slab type
 */
public abstract class SlabGroup<
		VARIANT extends Enum<VARIANT> & IStringSerializable,
		VARIANTS extends Iterable<VARIANT> & IStringSerializable,
		SLAB extends BlockSlabTestMod3<VARIANT, VARIANTS, SLAB>
		> {

	public final SLAB singleSlab;
	public final SLAB doubleSlab;
	public final String groupName;
	public final ItemSlab item;

	/**
	 * Create a slab group.
	 *
	 * @param groupName The group's name
	 * @param material  The Material of the slabs
	 * @param variants  The variants of the slabs
	 */
	public SlabGroup(final String groupName, final Material material, final VARIANTS variants) {
		this.groupName = groupName;
		this.singleSlab = createSlab(material, false, variants);
		this.doubleSlab = createSlab(material, true, variants);
		this.item = new ItemSlab(singleSlab, singleSlab, doubleSlab);
	}

	/**
	 * Create a slab block.
	 *
	 * @param material The Material
	 * @param isDouble Is this a double slab?
	 * @param variants The variants
	 * @return The slab block
	 */
	public abstract SLAB createSlab(Material material, boolean isDouble, VARIANTS variants);
}
