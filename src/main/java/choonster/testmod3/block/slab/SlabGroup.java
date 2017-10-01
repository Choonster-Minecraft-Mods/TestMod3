package choonster.testmod3.block.slab;

import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

/**
 * A group consisting of a single slab {@link Block}, a double slab {@link Block} and the {@link Item} that places them.
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

	private final String groupName;
	private final Material material;
	private final VARIANTS variants;
	private final ISlabGroupContainer<VARIANT, VARIANTS, SLAB> parentContainer;

	private SLAB singleSlab;
	private SLAB doubleSlab;
	private ItemSlab item;

	/**
	 * Create a slab group.
	 *
	 * @param groupName       The group's name
	 * @param material        The Material of the slabs
	 * @param variants        The variants of the slabs
	 * @param parentContainer The parent slab group container
	 */
	public SlabGroup(final String groupName, final Material material, final VARIANTS variants, final ISlabGroupContainer<VARIANT, VARIANTS, SLAB> parentContainer) {
		this.groupName = groupName;
		this.material = material;
		this.variants = variants;

		this.parentContainer = parentContainer;
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

	/**
	 * Get the single slab {@link Block}.
	 *
	 * @return The single slab Block
	 */
	public SLAB getSingleSlab() {
		return singleSlab;
	}

	/**
	 * Get the double slab {@link Block}.
	 *
	 * @return The single slab Block
	 */
	public SLAB getDoubleSlab() {
		return doubleSlab;
	}

	/**
	 * Get the slab {@link Item}.
	 *
	 * @return The slab Item
	 */
	public ItemSlab getItem() {
		return item;
	}

	/**
	 * Get the name of this group.
	 *
	 * @return The group name
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Get the parent slab group container.
	 *
	 * @return The parent slab group container.
	 */
	public ISlabGroupContainer<VARIANT, VARIANTS, SLAB> getParentContainer() {
		return parentContainer;
	}

	/**
	 * Register this group's {@link Block}s.
	 *
	 * @param registry The Block registry
	 * @throws IllegalStateException If the Blocks have already been registered
	 */
	public void registerBlocks(final IForgeRegistry<Block> registry) {
		Preconditions.checkState(singleSlab == null, "Attempt to re-register Blocks for Slab Group %s.%s", groupName, variants.getName());

		singleSlab = createSlab(material, false, variants);
		doubleSlab = createSlab(material, true, variants);
		registry.registerAll(singleSlab, doubleSlab);
	}

	/**
	 * Register this group's {@link Item}.
	 *
	 * @param registry The Item registry
	 * @throws IllegalStateException If the Item has already been registered
	 */
	public void registerItem(final IForgeRegistry<Item> registry) {
		Preconditions.checkState(item == null, "Attempt to re-register Item for Slab Group %s.%s", groupName, variants.getName());

		final ResourceLocation registryName = Preconditions.checkNotNull(singleSlab.getRegistryName(), "Block %s has null registry name", singleSlab);

		item = new ItemSlab(singleSlab, singleSlab, doubleSlab);
		item.setRegistryName(registryName);
		registry.register(item);
	}
}
