package choonster.testmod3.datafix;

import choonster.testmod3.block.slab.BlockColouredSlab;
import choonster.testmod3.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.Objects;

/**
 * Manages the flattening definitions for the {@link ItemFlattening} data fixer.
 *
 * @author Choonster
 */
public class ItemFlatteningDefinitions {
	/**
	 * Creates an instance of the ItemFlattening data fixer with the definitions of the items to flatten.
	 *
	 * @return The ItemFlattening instance
	 */
	public static ItemFlattening createItemFlattening() {
		final ImmutableList.Builder<ItemFlattening.FlatteningDefinition> flatteningDefinitions = new ImmutableList.Builder<>();

		ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS.getBlocksMap().forEach((color, blockColoredRotatable) -> {
			flatteningDefinitions.add(new ItemFlattening.FlatteningDefinition(
					"colored_rotatable",
					color.getMetadata(),
					Item.getItemFromBlock(blockColoredRotatable),
					(item, oldMetadata, oldStackTagCompound) -> new ItemStack(item)
			));
		});

		ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS.getBlocksMap().forEach((color, blockColoredMultiRotatable) -> {
			flatteningDefinitions.add(new ItemFlattening.FlatteningDefinition(
					"colored_multi_rotatable",
					color.getMetadata(),
					Item.getItemFromBlock(blockColoredMultiRotatable),
					(item, oldMetadata, oldStackTagCompound) -> new ItemStack(item)
			));
		});

		ModBlocks.VariantGroups.VARIANTS_BLOCKS.getBlocksMap().forEach((type, blockVariants) -> {
			//noinspection deprecation
			flatteningDefinitions.add(new ItemFlattening.FlatteningDefinition(
					"variants",
					type.getMeta(),
					Item.getItemFromBlock(blockVariants),
					(item, oldMetadata, oldStackTagCompound) -> new ItemStack(item)
			));
		});

		ModBlocks.VariantGroups.TERRACOTTA_SLABS.getSlabGroupsMap().forEach((color, slabGroup) -> {
			//noinspection deprecation
			final BlockColouredSlab.EnumColourGroup colourGroup = Objects.requireNonNull(BlockColouredSlab.EnumColourGroup.getGroupForColour(color));

			final String oldName = colourGroup == BlockColouredSlab.EnumColourGroup.LOW ? "stained_clay_slab_low" : "stained_clay_slab_high";

			flatteningDefinitions.add(new ItemFlattening.FlatteningDefinition(
					oldName,
					colourGroup.getOffsetMetadata(color),
					slabGroup.getItem(),
					(item, oldMetadata, oldStackTagCompound) -> new ItemStack(item)
			));
		});

		return new ItemFlattening(flatteningDefinitions.build());
	}
}
