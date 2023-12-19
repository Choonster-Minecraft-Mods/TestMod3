package choonster.testmod3.world.level.block.variantgroup;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.function.Function;

/**
 * A group consisting of a collection of variants with a block registered for each one.
 *
 * @author Choonster
 */
public class BlockVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> implements IBlockVariantGroup<VARIANT, BLOCK> {
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
	 * Gets the codec for this group.
	 *
	 * @return The codec
	 */
	public <CODEC_BLOCK extends Block> RecordCodecBuilder<CODEC_BLOCK, BlockVariantGroup<VARIANT, BLOCK>> codec() {
		// TODO: Figure out a real codec for this when block codecs are used somewhere
		return RecordCodecBuilder.point(this);
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
				registryName = groupName + "_" + variant.getSerializedName();
			} else {
				registryName = variant.getSerializedName() + "_" + groupName;
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
	public interface BlockFactory<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> {
		BLOCK createBlock(VARIANT variant, BlockVariantGroup<VARIANT, BLOCK> variantGroup, Block.Properties properties);
	}

	/**
	 * A function that creates item blocks for a variant group.
	 *
	 * @param <VARIANT> The variant type
	 * @param <BLOCK>   The block type
	 */
	@FunctionalInterface
	public interface ItemFactory<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> {
		BlockItem createItem(BLOCK block, Item.Properties properties, VARIANT variant);
	}

	public static class Builder<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> {
		private final DeferredRegister<Block> blocks;
		private final DeferredRegister<Item> items;

		@Nullable
		private String groupName;
		private boolean isSuffix;
		@Nullable
		private Iterable<VARIANT> variants;

		@Nullable
		private Function<VARIANT, Block.Properties> blockPropertiesFactory;
		@Nullable
		private BlockFactory<VARIANT, BLOCK> blockFactory;

		private Function<VARIANT, Item.Properties> itemPropertiesFactory = variant -> new Item.Properties();
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
		public static <VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> Builder<VARIANT, BLOCK> create(final DeferredRegister<Block> blocks, final DeferredRegister<Item> items) {
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
		 * If no item properties factory is specified, the default item properties are used.
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
