package choonster.testmod3.block.variantgroup;

import choonster.testmod3.TestMod3;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
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
 * A group consisting of a collection of variants with a block registered for each one.
 *
 * @author Choonster
 */
public class BlockVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> implements IBlockVariantGroup<VARIANT, BLOCK> {
	private final String groupName;
	private final Iterable<VARIANT> variants;

	private final Map<VARIANT, RegistryObject<BLOCK>> blocks;

	private BlockVariantGroup(
			final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants,
			final Function<VARIANT, Block.Properties> blockPropertiesFactory, final BlockFactory<VARIANT, BLOCK> blockFactory,
			final Function<VARIANT, Item.Properties> itemPropertiesFactory, final ItemFactory<VARIANT, BLOCK> itemFactory,
			final DeferredRegister<Block> blocks, final DeferredRegister<Item> items
	) {
		this.groupName = groupName;
		this.variants = variants;

		this.blocks = register(
				groupName, isSuffix, variants,
				blockPropertiesFactory, blockFactory,
				itemPropertiesFactory, itemFactory,
				blocks, items
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
	 * Gets this group's blocks.
	 *
	 * @return The blocks
	 */
	@Override
	public Collection<RegistryObject<BLOCK>> getEntries() {
		return getBlocksMap().values();
	}

	/**
	 * Gets this group's blocks and their corresponding variants.
	 *
	 * @return The blocks map
	 */
	public Map<VARIANT, RegistryObject<BLOCK>> getBlocksMap() {
		return blocks;
	}

	/**
	 * Gets the block for the specified variant.
	 *
	 * @param variant The variant
	 * @return The block
	 */
	public RegistryObject<BLOCK> getBlock(final VARIANT variant) {
		return blocks.get(variant);
	}

	/**
	 * Registers the variant group's blocks and items using the provided DeferredRegister instance.
	 *
	 * @return A map of variants to their corresponding blocks
	 */
	private Map<VARIANT, RegistryObject<BLOCK>> register(
			final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants,
			final Function<VARIANT, Block.Properties> blockPropertiesFactory, final BlockFactory<VARIANT, BLOCK> blockFactory,
			final Function<VARIANT, Item.Properties> itemPropertiesFactory, final ItemFactory<VARIANT, BLOCK> itemFactory,
			final DeferredRegister<Block> blocks, final DeferredRegister<Item> items
	) {
		final ImmutableMap.Builder<VARIANT, RegistryObject<BLOCK>> builder = ImmutableMap.builder();

		variants.forEach(variant -> {
			final String registryName;
			if (isSuffix) {
				registryName = groupName + "_" + variant.getString();
			} else {
				registryName = variant.getString() + "_" + groupName;
			}

			final RegistryObject<BLOCK> block = blocks.register(registryName, () -> {
				final Block.Properties properties = blockPropertiesFactory.apply(variant);

				return blockFactory.createBlock(variant, this, properties);
			});

			builder.put(variant, block);

			items.register(registryName, () -> {
				final Item.Properties properties = itemPropertiesFactory.apply(variant);

				return itemFactory.createItem(block.get(), properties, variant);
			});
		});

		return builder.build();
	}

	@FunctionalInterface
	public interface BlockFactory<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> {
		BLOCK createBlock(VARIANT variant, BlockVariantGroup<VARIANT, BLOCK> variantGroup, Block.Properties properties);
	}

	/**
	 * A function that creates item blocks for a variant group.
	 *
	 * @param <VARIANT> The variant type
	 * @param <BLOCK>   The block type
	 */
	@FunctionalInterface
	public interface ItemFactory<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> {
		BlockItem createItem(BLOCK block, Item.Properties properties, VARIANT variant);
	}

	public static class Builder<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> {
		private final DeferredRegister<Block> blocks;
		private final DeferredRegister<Item> items;

		private String groupName;
		private boolean isSuffix;
		private Iterable<VARIANT> variants;

		private Function<VARIANT, Block.Properties> blockPropertiesFactory;
		private BlockFactory<VARIANT, BLOCK> blockFactory;

		private Function<VARIANT, Item.Properties> itemPropertiesFactory = variant -> new Item.Properties().group(TestMod3.ITEM_GROUP);
		private ItemFactory<VARIANT, BLOCK> itemFactory = (block, properties, variant) -> new BlockItem(block, properties);

		/**
		 * Creates a new variant group builder.
		 *
		 * @param blocks    The DeferredRegister instance to register the group's blocks with
		 * @param items     The DeferredRegister instance to register the group's block items with
		 * @param <VARIANT> The variant type
		 * @param <BLOCK>   The block type
		 * @return A new Variant Group Builder
		 */
		public static <VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> Builder<VARIANT, BLOCK> create(final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
			return new Builder<>(blocks, items);
		}

		public Builder(final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
			this.blocks = blocks;
			this.items = items;
		}

		/**
		 * Sets the variant group's name. This is used in the block registry names.
		 *
		 * @param groupName The group name
		 * @return This builder
		 * @throws NullPointerException If {@code groupName} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> groupName(final String groupName) {
			Preconditions.checkNotNull(groupName, "groupName");
			this.groupName = groupName;
			return this;
		}

		/**
		 * Configures the variant group to append the variant name as a suffix rather than a prefix.
		 *
		 * @return This builder
		 */
		public Builder<VARIANT, BLOCK> suffix() {
			isSuffix = true;
			return this;
		}

		/**
		 * Sets the variant group's variants. One block will be created for each variant.
		 *
		 * @param variants The variants
		 * @return This builder
		 * @throws NullPointerException If {@code variants} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> variants(final Iterable<VARIANT> variants) {
			Preconditions.checkNotNull(variants, "variants");
			this.variants = variants;
			return this;
		}

		/**
		 * Sets the variant group's variants. One block will be created for each variant.
		 *
		 * @param variants The variants
		 * @return This builder
		 * @throws NullPointerException If {@code variants} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> variants(final VARIANT[] variants) {
			Preconditions.checkNotNull(variants, "variants");
			return variants(Arrays.asList(variants));
		}

		/**
		 * Sets the factory function used to create the properties for each block.
		 *
		 * @param blockPropertiesFactory The block properties factory function
		 * @return This builder
		 * @throws NullPointerException If {@code blockPropertiesFactory} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> blockPropertiesFactory(final Function<VARIANT, Block.Properties> blockPropertiesFactory) {
			Preconditions.checkNotNull(blockPropertiesFactory, "blockPropertiesFactory");
			this.blockPropertiesFactory = blockPropertiesFactory;
			return this;
		}

		/**
		 * Sets the factory function used to create the blocks.
		 *
		 * @param blockFactory The block factory function
		 * @return This builder
		 * @throws NullPointerException If {@code blockFactory} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> blockFactory(final BlockFactory<VARIANT, BLOCK> blockFactory) {
			Preconditions.checkNotNull(blockFactory, "blockFactory");
			this.blockFactory = blockFactory;
			return this;
		}

		/**
		 * Sets the factory function used to create the properties for each block item.
		 * <p>
		 * If no item properties factory is specified, a factory producing item properties with the {@link ItemGroup}
		 * set to {@link TestMod3#ITEM_GROUP} is used.
		 *
		 * @param itemPropertiesFactory The item properties factory function
		 * @return This builder
		 * @throws NullPointerException If {@code itemPropertiesFactory} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> itemPropertiesFactory(final Function<VARIANT, Item.Properties> itemPropertiesFactory) {
			Preconditions.checkNotNull(itemPropertiesFactory, "itemPropertiesFactory");
			this.itemPropertiesFactory = itemPropertiesFactory;
			return this;
		}

		/**
		 * Sets the factory function used to create the block items.
		 * <p>
		 * If no item factory is specified, a factory producing {@link BlockItem} is used.
		 *
		 * @param itemFactory The item factory function
		 * @return This builder
		 * @throws NullPointerException If {@code itemFactory} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> itemFactory(final ItemFactory<VARIANT, BLOCK> itemFactory) {
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
		 * @throws IllegalStateException If the blockPropertiesFactory hasn't been provided
		 * @throws IllegalStateException If the block factory hasn't been provided
		 */
		public BlockVariantGroup<VARIANT, BLOCK> build() {
			Preconditions.checkState(groupName != null, "Group Name not provided");
			Preconditions.checkState(variants != null, "Variants not provided");
			Preconditions.checkState(blockPropertiesFactory != null, "Block Properties Factory not provided");
			Preconditions.checkState(blockFactory != null, "Block Factory not provided");

			return new BlockVariantGroup<>(
					groupName, isSuffix, variants,
					blockPropertiesFactory, blockFactory,
					itemPropertiesFactory, itemFactory,
					blocks, items
			);
		}
	}
}
