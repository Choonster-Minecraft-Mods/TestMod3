package choonster.testmod3.item.variantgroup;

import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

/**
 * A group consisting of a collection of variants with an item registered for each one.
 *
 * @author Choonster
 */
public class ItemVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> implements IItemVariantGroup<VARIANT, ITEM> {
	private final String groupName;
	private final boolean isSuffix;
	private final Iterable<VARIANT> variants;

	private final ItemFactory<VARIANT, ITEM> itemFactory;

	private Map<VARIANT, ITEM> items;

	private ItemVariantGroup(final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants, final ItemFactory<VARIANT, ITEM> itemFactory) {
		this.groupName = groupName;
		this.isSuffix = isSuffix;
		this.variants = variants;
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
	 * Gets this group's items.
	 *
	 * @return The items
	 */
	@Override
	public Collection<ITEM> getItems() {
		return getItemsMap().values();
	}

	/**
	 * Gets this group's items and their corresponding variants.
	 *
	 * @return The items map
	 */
	public Map<VARIANT, ITEM> getItemsMap() {
		return items;
	}

	/**
	 * Gets the item for the specified variant.
	 *
	 * @param variant The variant
	 * @return The item
	 */
	public ITEM getItem(final VARIANT variant) {
		return items.get(variant);
	}

	/**
	 * Registers this group's items.
	 *
	 * @param registry The item registry
	 * @throws IllegalStateException If the items have already been registered
	 */
	@Override
	public void registerItems(final IForgeRegistry<Item> registry) {
		Preconditions.checkState(items == null, "Attempt to re-register Items for Variant Group %s", groupName);

		final ImmutableMap.Builder<VARIANT, ITEM> builder = ImmutableMap.builder();

		getVariants().forEach(variant -> {
			final String registryName;
			if (isSuffix) {
				registryName = groupName + "_" + variant.getName();
			} else {
				registryName = variant.getName() + "_" + groupName;
			}

			final ITEM item = itemFactory.createItem(variant, this);

			RegistryUtil.setItemName(item, registryName);

			registry.register(item);
			builder.put(variant, item);
		});

		items = builder.build();
	}

	/**
	 * A function that creates items for a variant group.
	 *
	 * @param <VARIANT> The variant type
	 * @param <ITEM>    The item type
	 */
	@FunctionalInterface
	public interface ItemFactory<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> {
		ITEM createItem(VARIANT variant, ItemVariantGroup<VARIANT, ITEM> variantGroup);
	}

	public static class Builder<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> {
		private String groupName;
		private boolean isSuffix;
		private Iterable<VARIANT> variants;

		private ItemFactory<VARIANT, ITEM> itemFactory;

		/**
		 * Creates a new variant group builder.
		 *
		 * @param <VARIANT> The variant type
		 * @param <ITEM>    The item type
		 * @return A new Variant Group Builder
		 */
		public static <VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> ItemVariantGroup.Builder<VARIANT, ITEM> create() {
			return new ItemVariantGroup.Builder<>();
		}

		private Builder() {
		}

		/**
		 * Sets the variant group's name. This is used in the item registry names.
		 *
		 * @param groupName The group name
		 * @return This builder
		 * @throws NullPointerException If {@code groupName} is {@code null}
		 */
		public ItemVariantGroup.Builder<VARIANT, ITEM> groupName(final String groupName) {
			Preconditions.checkNotNull(groupName, "groupName");
			this.groupName = groupName;
			return this;
		}

		/**
		 * Configures the variant group to append the variant name as a suffix rather than a prefix.
		 *
		 * @return This builder
		 */
		public ItemVariantGroup.Builder<VARIANT, ITEM> suffix() {
			isSuffix = true;
			return this;
		}

		/**
		 * Sets the variant group's variants. One item will be created for each variant.
		 *
		 * @param variants The variants
		 * @return This builder
		 * @throws NullPointerException If {@code variants} is {@code null}
		 */
		public ItemVariantGroup.Builder<VARIANT, ITEM> variants(final Iterable<VARIANT> variants) {
			Preconditions.checkNotNull(variants, "variants");
			this.variants = variants;
			return this;
		}

		/**
		 * Sets the variant group's variants. One item will be created for each variant.
		 *
		 * @param variants The variants
		 * @return This builder
		 * @throws NullPointerException If {@code variants} is {@code null}
		 */
		public ItemVariantGroup.Builder<VARIANT, ITEM> variants(final VARIANT[] variants) {
			Preconditions.checkNotNull(variants, "variants");
			return variants(Arrays.asList(variants));
		}

		/**
		 * Sets the factory function used to create the items.
		 *
		 * @param itemFactory The item factory function
		 * @return This builder
		 * @throws NullPointerException If {@code itemFactory} is {@code null}
		 */
		public ItemVariantGroup.Builder<VARIANT, ITEM> itemFactory(final ItemFactory<VARIANT, ITEM> itemFactory) {
			Preconditions.checkNotNull(itemFactory, "itemFactory");
			this.itemFactory = itemFactory;
			return this;
		}

		/**
		 * Creates a item variant group based on the data in this builder.
		 *
		 * @return The variant group
		 * @throws IllegalStateException If the group name hasn't been provided
		 * @throws IllegalStateException If the variants haven't been provided
		 * @throws IllegalStateException If the item factory hasn't been provided
		 */
		public ItemVariantGroup<VARIANT, ITEM> build() {
			Preconditions.checkState(groupName != null, "Group Name not provided");
			Preconditions.checkState(variants != null, "Variants not provided");
			Preconditions.checkState(itemFactory != null, "Item Factory not provided");

			return new ItemVariantGroup<>(groupName, isSuffix, variants, itemFactory);
		}
	}
}
