package choonster.testmod3.item.variantgroup;

import choonster.testmod3.TestMod3;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * A group consisting of a collection of variants with an item registered for each one.
 *
 * @author Choonster
 */
public class ItemVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> implements IItemVariantGroup<VARIANT, ITEM> {
	private final String groupName;
	private final Iterable<VARIANT> variants;

	private final Map<VARIANT, RegistryObject<ITEM>> items;

	private ItemVariantGroup(
			final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants,
			final Function<VARIANT, Item.Properties> itemPropertiesFactory, final ItemFactory<VARIANT, ITEM> itemFactory,
			final DeferredRegister<Item> items
	) {
		this.groupName = groupName;
		this.variants = variants;

		this.items = register(
				groupName, isSuffix, variants,
				itemPropertiesFactory, itemFactory,
				items
		);
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
	public Collection<RegistryObject<ITEM>> getEntries() {
		return getItemsMap().values();
	}

	/**
	 * Gets this group's items and their corresponding variants.
	 *
	 * @return The items map
	 */
	public Map<VARIANT, RegistryObject<ITEM>> getItemsMap() {
		return items;
	}

	/**
	 * Gets the item for the specified variant.
	 *
	 * @param variant The variant
	 * @return The item
	 */
	public RegistryObject<ITEM> getItem(final VARIANT variant) {
		return items.get(variant);
	}

	/**
	 * Registers the variant group's items using the provided DeferredRegister instance.
	 *
	 * @return A map of variants to their corresponding items
	 */
	private Map<VARIANT, RegistryObject<ITEM>> register(
			final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants,
			final Function<VARIANT, Item.Properties> itemPropertiesFactory, final ItemFactory<VARIANT, ITEM> itemFactory,
			final DeferredRegister<Item> items
	) {
		final ImmutableMap.Builder<VARIANT, RegistryObject<ITEM>> builder = ImmutableMap.builder();

		variants.forEach(variant -> {
			final String registryName;
			if (isSuffix) {
				registryName = groupName + "_" + variant.getSerializedName();
			} else {
				registryName = variant.getSerializedName() + "_" + groupName;
			}

			final RegistryObject<ITEM> item = items.register(registryName, () -> {
				final Item.Properties properties = itemPropertiesFactory.apply(variant);

				return itemFactory.createItem(variant, this, properties);
			});

			builder.put(variant, item);
		});

		return builder.build();
	}

	/**
	 * A function that creates items for a variant group.
	 *
	 * @param <VARIANT> The variant type
	 * @param <ITEM>    The item type
	 */
	@FunctionalInterface
	public interface ItemFactory<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> {
		ITEM createItem(VARIANT variant, ItemVariantGroup<VARIANT, ITEM> variantGroup, Item.Properties properties);
	}

	public static class Builder<VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> {
		private final DeferredRegister<Item> items;

		private String groupName;
		private boolean isSuffix;
		private Iterable<VARIANT> variants;

		private Function<VARIANT, Item.Properties> itemPropertiesFactory = variant -> new Item.Properties().tab(TestMod3.ITEM_GROUP);
		private ItemFactory<VARIANT, ITEM> itemFactory;

		/**
		 * Creates a new variant group builder.
		 *
		 * @param items     The DeferredRegister instance to register the group's items with
		 * @param <VARIANT> The variant type
		 * @param <ITEM>    The item type
		 * @return A new Variant Group Builder
		 */
		public static <VARIANT extends Enum<VARIANT> & IStringSerializable, ITEM extends Item> ItemVariantGroup.Builder<VARIANT, ITEM> create(final DeferredRegister<Item> items) {
			return new ItemVariantGroup.Builder<>(items);
		}

		private Builder(final DeferredRegister<Item> items) {
			this.items = items;
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
		 * Sets the factory function used to create the properties for each item.
		 * <p>
		 * If no item properties factory is specified, a factory producing item properties with the {@link ItemGroup}
		 * set to {@link TestMod3#ITEM_GROUP} is used.
		 *
		 * @param itemPropertiesFactory The item properties factory function
		 * @return This builder
		 * @throws NullPointerException If {@code itemPropertiesFactory} is {@code null}
		 */
		public ItemVariantGroup.Builder<VARIANT, ITEM> itemPropertiesFactory(final Function<VARIANT, Item.Properties> itemPropertiesFactory) {
			Preconditions.checkNotNull(itemPropertiesFactory, "itemPropertiesFactory");
			this.itemPropertiesFactory = itemPropertiesFactory;
			return this;
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

			return new ItemVariantGroup<>(groupName, isSuffix, variants, itemPropertiesFactory, itemFactory, items);
		}
	}
}
