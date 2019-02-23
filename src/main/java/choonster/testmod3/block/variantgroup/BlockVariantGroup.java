package choonster.testmod3.block.variantgroup;

import choonster.testmod3.block.BlockTestMod3;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.*;
import java.util.function.Function;

/**
 * A group consisting of a collection of variants with a block registered for each one.
 *
 * @author Choonster
 */
public class BlockVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> implements IBlockVariantGroup<VARIANT, BLOCK> {

	private final String groupName;
	private final boolean isSuffix;
	private final Iterable<VARIANT> variants;
	private final Material material;

	private final BlockFactory<VARIANT, BLOCK> blockFactory;
	private final Function<BLOCK, ItemBlock> itemFactory;

	private Map<VARIANT, BLOCK> blocks;
	private boolean registeredItems = false;

	private BlockVariantGroup(final String groupName, final boolean isSuffix, final Iterable<VARIANT> variants, final Material material, final BlockFactory<VARIANT, BLOCK> blockFactory, final Function<BLOCK, ItemBlock> itemFactory) {
		this.groupName = groupName;
		this.isSuffix = isSuffix;
		this.material = material;
		this.variants = variants;
		this.blockFactory = blockFactory;
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
	public Collection<BLOCK> getBlocks() {
		return getBlocksMap().values();
	}

	/**
	 * Gets this group's blocks and their corresponding variants.
	 *
	 * @return The blocks map
	 */
	public Map<VARIANT, BLOCK> getBlocksMap() {
		return blocks;
	}

	/**
	 * Gets the block for the specified variant.
	 *
	 * @param variant The variant
	 * @return The block
	 */
	public BLOCK getBlock(final VARIANT variant) {
		return blocks.get(variant);
	}

	/**
	 * Registers this group's blocks.
	 *
	 * @param registry The block registry
	 * @throws IllegalStateException If the blocks have already been registered
	 */
	@Override
	public void registerBlocks(final IForgeRegistry<Block> registry) {
		Preconditions.checkState(blocks == null, "Attempt to re-register Blocks for Variant Group %s", groupName);

		final ImmutableMap.Builder<VARIANT, BLOCK> builder = ImmutableMap.builder();

		getVariants().forEach(variant -> {
			final String registryName;
			if (isSuffix) {
				registryName = groupName + "_" + variant.getName();
			} else {
				registryName = variant.getName() + "_" + groupName;
			}

			final BLOCK block = blockFactory.createBlock(variant, material, this);

			BlockTestMod3.setBlockName(block, registryName);

			registry.register(block);
			builder.put(variant, block);
		});

		blocks = builder.build();
	}

	/**
	 * Registers this group's items.
	 *
	 * @param registry The item registry
	 * @return The registered items
	 * @throws IllegalStateException If the items have already been registered
	 */
	@Override
	public List<ItemBlock> registerItems(final IForgeRegistry<Item> registry) {
		Preconditions.checkState(blocks != null, "Attempt to register Items before Blocks for Variant Group %s", groupName);
		Preconditions.checkState(!registeredItems, "Attempt to re-register Items for Variant Group %s", groupName);

		final List<ItemBlock> items = new ArrayList<>();

		blocks.values().forEach(block -> {
			final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);

			final ItemBlock item = itemFactory.apply(block);
			item.setRegistryName(registryName);

			registry.register(item);

			items.add(item);
		});

		registeredItems = true;

		return items;
	}

	/**
	 * A function that creates blocks for a variant group.
	 *
	 * @param <VARIANT> The variant type
	 * @param <BLOCK>   The block type
	 */
	@FunctionalInterface
	public interface BlockFactory<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> {
		BLOCK createBlock(VARIANT variant, Material material, BlockVariantGroup<VARIANT, BLOCK> variantGroup);
	}

	public static class Builder<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> {
		private String groupName;
		private boolean isSuffix;
		private Iterable<VARIANT> variants;
		private Material material;

		private BlockFactory<VARIANT, BLOCK> blockFactory;
		private Function<BLOCK, ItemBlock> itemFactory = ItemBlock::new;

		/**
		 * Creates a new variant group builder.
		 *
		 * @param <VARIANT> The variant type
		 * @param <BLOCK>   The block type
		 * @return A new Variant Group Builder
		 */
		public static <VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> Builder<VARIANT, BLOCK> create() {
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
		 * Sets the block material.
		 *
		 * @param material The material
		 * @return This builder
		 * @throws NullPointerException If {@code material} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> material(final Material material) {
			Preconditions.checkNotNull(material, "material");
			this.material = material;
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
		 * Sets the factory function used to create the block items. If no item factory is specified, a factory producing {@link ItemBlock} is used.
		 *
		 * @param itemFactory The item factory function
		 * @return This builder
		 * @throws NullPointerException If {@code itemFactory} is {@code null}
		 */
		public Builder<VARIANT, BLOCK> itemFactory(final Function<BLOCK, ItemBlock> itemFactory) {
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
		 * @throws IllegalStateException If the block factory hasn't been provided
		 */
		public BlockVariantGroup<VARIANT, BLOCK> build() {
			Preconditions.checkState(groupName != null, "Group Name not provided");
			Preconditions.checkState(variants != null, "Variants not provided");
			Preconditions.checkState(material != null, "Material not provided");
			Preconditions.checkState(blockFactory != null, "Block Factory not provided");

			return new BlockVariantGroup<>(groupName, isSuffix, variants, material, blockFactory, itemFactory);
		}
	}
}
