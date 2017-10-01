package choonster.testmod3.block.slab;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.List;

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

	/**
	 * Register this container's {@link Block}s.
	 *
	 * @param registry The Block registry
	 * @throws IllegalStateException If the Blocks have already been registered
	 */
	void registerBlocks(IForgeRegistry<Block> registry);

	/**
	 * Register and return this container's {@link Item}s.
	 *
	 * @param registry The Item registry
	 * @return The registered Items
	 * @throws IllegalStateException If the Items have already been registered
	 */
	List<ItemBlock> registerItems(IForgeRegistry<Item> registry);
}
