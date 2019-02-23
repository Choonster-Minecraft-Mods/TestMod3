package choonster.testmod3.block.variantgroup;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemSlab;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A group consisting of a collection of variants with a slab group registered for each one.
 *
 * @author Choonster
 */
public class SlabVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, SLAB extends BlockSlab> implements IBlockVariantGroup<VARIANT, SLAB> {

	private final String groupName;
	private final boolean isSuffix;
	private final Iterable<VARIANT> variants;
	private final Material material;

	private final SlabFactory<VARIANT, SLAB> slabFactory;
	private final ItemFactory<SLAB> itemFactory;

	private Map<VARIANT, SlabGroup> slabGroups;
	private boolean registeredItems = false;

	private SlabVariantGroup(final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants, final Material material, final SlabFactory<VARIANT, SLAB> slabFactory, final ItemFactory<SLAB> itemFactory) {
		this.groupName = groupName;
		this.isSuffix = isSuffix;
		this.material = material;
		this.variants = variants;
		this.slabFactory = slabFactory;
		this.itemFactory = itemFactory;
	}

	/**
	 * Gets the name of this group.
	 *
	 * @return The group name
	 */
	@Override
	public String getGroupName() {
		return groupName;
	}

	/**
	 * Gets this group's variants.
	 *
	 * @return The variants
	 */
	@Override
	public Iterable<VARIANT> getVariants() {
		return variants;
	}

	/**
	 * Gets this group's blocks.
	 *
	 * @return The blocks
	 */
	@Override
	public Collection<SLAB> getBlocks() {
		return getSlabGroupsMap().values().stream()
				.flatMap(slabGroup -> Stream.of(slabGroup.singleSlab, slabGroup.doubleSlab))
				.collect(Collectors.toList());
	}

	/**
	 * Gets this group's slab groups and their corresponding variants.
	 *
	 * @return The slab groups map
	 */
	public Map<VARIANT, SlabGroup> getSlabGroupsMap() {
		return slabGroups;
	}

	/**
	 * Gets the slab group for the specified variant.
	 *
	 * @param variant The variant
	 * @return The block
	 */
	public SlabGroup getSlabGroup(final VARIANT variant) {
		return slabGroups.get(variant);
	}

	/**
	 * Registers this group's blocks.
	 *
	 * @param registry The block registry
	 * @throws IllegalStateException If the blocks have already been registered
	 */
	@Override
	public void registerBlocks(final IForgeRegistry<Block> registry) {
		Preconditions.checkState(slabGroups == null, "Attempt to re-register Blocks for Slab Variant Group %s", groupName);

		final ImmutableMap.Builder<VARIANT, SlabGroup> builder = ImmutableMap.builder();

		variants.forEach(variant -> {
			final String registryName;
			if (isSuffix) {
				registryName = groupName + "_" + variant.getName();
			} else {
				registryName = variant.getName() + "_" + groupName;
			}

			final SlabGroup slabGroup = new SlabGroup(new ResourceLocation(TestMod3.MODID, registryName));

			final SLAB singleSlab = slabFactory.createSlab(variant, material, false, slabGroup);
			final SLAB doubleSlab = slabFactory.createSlab(variant, material, true, slabGroup);

			RegistryUtil.setBlockName(singleSlab, registryName);
			RegistryUtil.setBlockName(doubleSlab, "double_" + registryName);
			doubleSlab.setTranslationKey(Constants.RESOURCE_PREFIX + registryName);

			registry.registerAll(singleSlab, doubleSlab);

			slabGroup.setSlabs(singleSlab, doubleSlab);

			builder.put(variant, slabGroup);
		});

		slabGroups = builder.build();
	}

	/**
	 * Registers this group's items.
	 *
	 * @param registry The item registry
	 * @return The registered items
	 * @throws IllegalStateException If the items have already been registered
	 */
	@Override
	public List<ItemSlab> registerItems(final IForgeRegistry<Item> registry) {
		Preconditions.checkState(slabGroups != null, "Attempt to register Items before Blocks for Slab Variant Group %s", groupName);
		Preconditions.checkState(!registeredItems, "Attempt to re-register Items for Slab Variant Group %s", groupName);

		final List<ItemSlab> items = new ArrayList<>();

		slabGroups.values().forEach(slabGroup -> {
			final SLAB singleSlab = slabGroup.singleSlab;
			final ResourceLocation registryName = Preconditions.checkNotNull(singleSlab.getRegistryName(), "Single Slab %s has null registry name", singleSlab);

			final ItemSlab item = itemFactory.createItem(singleSlab, slabGroup.doubleSlab);
			item.setRegistryName(registryName);

			registry.register(item);

			slabGroup.setItem(item);

			items.add(item);
		});

		registeredItems = true;

		return items;
	}

	public class SlabGroup {
		private final ResourceLocation name;
		private SLAB singleSlab;
		private SLAB doubleSlab;
		private ItemSlab item;

		private SlabGroup(final ResourceLocation name) {
			this.name = name;
		}

		/**
		 * Gets the slab variant group that this slab group belongs to.
		 *
		 * @return The slab variant group
		 */
		public SlabVariantGroup<VARIANT, SLAB> getVariantGroup() {
			return SlabVariantGroup.this;
		}

		/**
		 * Gets the single slab block.
		 *
		 * @return The single slab block
		 */
		public SLAB getSingleSlab() {
			return singleSlab;
		}

		/**
		 * Gets the double slab block.
		 *
		 * @return The single slab block
		 */
		public SLAB getDoubleSlab() {
			return doubleSlab;
		}

		/**
		 * Get the slab item.
		 *
		 * @return The slab item
		 */
		public ItemSlab getItem() {
			return item;
		}

		private void setSlabs(final SLAB singleSlab, final SLAB doubleSlab) {
			Preconditions.checkState(this.singleSlab == null, "Attempt to re-set Single Slab for Slab Group %s", name);
			Preconditions.checkState(this.doubleSlab == null, "Attempt to re-set Double Slab for Slab Group %s", name);

			this.singleSlab = singleSlab;
			this.doubleSlab = doubleSlab;
		}

		private void setItem(final ItemSlab item) {
			Preconditions.checkState(this.item == null, "Attempt to re-set Item for Slab Group %s", name);

			this.item = item;
		}
	}

	/**
	 * A function that creates slab blocks for a variant group.
	 *
	 * @param <VARIANT> The variant type
	 * @param <SLAB>    The slab type
	 */
	@FunctionalInterface
	public interface SlabFactory<VARIANT extends Enum<VARIANT> & IStringSerializable, SLAB extends BlockSlab> {
		SLAB createSlab(VARIANT variant, Material material, boolean isDouble, SlabVariantGroup<VARIANT, SLAB>.SlabGroup slabGroup);
	}

	/**
	 * A function that creates slab items for a variant group.
	 *
	 * @param <SLAB> The slab type
	 */
	@FunctionalInterface
	public interface ItemFactory<SLAB extends BlockSlab> {
		ItemSlab createItem(SLAB singleSlab, SLAB doubleSlab);
	}

	public static class Builder<VARIANT extends Enum<VARIANT> & IStringSerializable, SLAB extends BlockSlab> {
		private String groupName;
		private boolean isSuffix;
		private Iterable<VARIANT> variants;
		private Material material;

		private SlabFactory<VARIANT, SLAB> slabFactory;
		private ItemFactory<SLAB> itemFactory = (singleSlab, doubleSlab) -> new ItemSlab(singleSlab, singleSlab, doubleSlab);

		/**
		 * Creates a new variant group builder.
		 *
		 * @param <VARIANT> The variant type
		 * @param <SLAB>    The slab type
		 * @return A new Slab Variant Group Builder
		 */
		public static <VARIANT extends Enum<VARIANT> & IStringSerializable, SLAB extends BlockSlab> Builder<VARIANT, SLAB> create() {
			return new Builder<>();
		}

		private Builder() {
		}

		/**
		 * Sets the variant group's name. This is used in the block registry names.
		 *
		 * @param groupName The group name
		 * @return This builder
		 * @throws NullPointerException If {@code groupName} is {@code null}
		 */
		public Builder<VARIANT, SLAB> groupName(final String groupName) {
			Preconditions.checkNotNull(groupName, "groupName");
			this.groupName = groupName;
			return this;
		}

		/**
		 * Configures the variant group to append the variant name as a suffix rather than a prefix.
		 *
		 * @return This builder
		 */
		public Builder<VARIANT, SLAB> suffix() {
			isSuffix = true;
			return this;
		}

		/**
		 * Sets the variant group's variants. One slab group will be created for each variant.
		 *
		 * @param variants The variants
		 * @return This builder
		 * @throws NullPointerException If {@code variants} is {@code null}
		 */
		public Builder<VARIANT, SLAB> variants(final Iterable<VARIANT> variants) {
			Preconditions.checkNotNull(variants, "variants");
			this.variants = variants;
			return this;
		}

		/**
		 * Sets the variant group's variants. One slab group will be created for each variant.
		 *
		 * @param variants The variants
		 * @return This builder
		 * @throws NullPointerException If {@code variants} is {@code null}
		 */
		public Builder<VARIANT, SLAB> variants(final VARIANT[] variants) {
			Preconditions.checkNotNull(variants, "variants");
			return variants(Arrays.asList(variants));
		}

		/**
		 * Sets the block material.
		 *
		 * @param material The material
		 * @return This builder
		 * @throws NullPointerException If {@code material} is {@code null}
		 */
		public Builder<VARIANT, SLAB> material(final Material material) {
			Preconditions.checkNotNull(material, "material");
			this.material = material;
			return this;
		}

		/**
		 * Sets the factory function used to create the slab blocks.
		 *
		 * @param slabFactory The slab factory function
		 * @return This builder
		 * @throws NullPointerException If {@code slabFactory} is {@code null}
		 */
		public Builder<VARIANT, SLAB> blockFactory(final SlabFactory<VARIANT, SLAB> slabFactory) {
			Preconditions.checkNotNull(slabFactory, "slabFactory");
			this.slabFactory = slabFactory;
			return this;
		}

		/**
		 * Sets the factory function used to create the block items. If no item factory is specified, a factory producing {@link ItemBlock} is used.
		 *
		 * @param itemFactory The item factory function
		 * @return This builder
		 * @throws NullPointerException If {@code itemFactory} is {@code null}
		 */
		public Builder<VARIANT, SLAB> itemFactory(final ItemFactory<SLAB> itemFactory) {
			Preconditions.checkNotNull(itemFactory, "itemFactory");
			this.itemFactory = itemFactory;
			return this;
		}

		/**
		 * Creates a block variant group based on the data in this builder.
		 *
		 * @return The variant group
		 * @throws IllegalStateException If the group name hasn't been provided
		 * @throws IllegalStateException If the variants haven't been provided
		 * @throws IllegalStateException If the material hasn't been provided
		 * @throws IllegalStateException If the slab factory hasn't been provided
		 */
		public SlabVariantGroup<VARIANT, SLAB> build() {
			Preconditions.checkState(groupName != null, "Group Name not provided");
			Preconditions.checkState(variants != null, "Variants not provided");
			Preconditions.checkState(material != null, "Material not provided");
			Preconditions.checkState(slabFactory != null, "Slab Factory not provided");

			return new SlabVariantGroup<>(groupName, isSuffix, variants, material, slabFactory, itemFactory);
		}
	}
}
