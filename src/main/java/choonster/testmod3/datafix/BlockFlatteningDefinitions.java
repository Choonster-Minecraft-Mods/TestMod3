package choonster.testmod3.datafix;

import choonster.testmod3.block.BlockColoredRotatable;
import choonster.testmod3.block.slab.BlockColouredSlab;
import choonster.testmod3.init.ModBlocks;
import com.google.common.collect.ImmutableList;
import net.minecraft.block.BlockSlab;
import net.minecraft.util.EnumFacing;

import java.util.Objects;

/**
 * Manages the flattening definitions for the {@link BlockFlattening} DataFixer.
 *
 * @author Choonster
 */
public class BlockFlatteningDefinitions {
	/**
	 * Create an instance of the BlockFlattening DataFixer with the definitions of the blocks to flatten.
	 *
	 * @return The BlockFlattening instance
	 */
	public static BlockFlattening createBlockFlattening() {
		final ImmutableList.Builder<BlockFlattening.FlatteningDefinition> flatteningDefinitions = new ImmutableList.Builder<>();

		ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS.getBlocksMap().forEach((color, blockColoredRotatable) -> {
			flatteningDefinitions.add(new BlockFlattening.FlatteningDefinition(
					"colored_rotatable",
					color.getMetadata(),
					blockColoredRotatable,
					(block, tileEntityNBT) -> {
						final int facingIndex = Objects.requireNonNull(tileEntityNBT).getInteger("facing");
						final EnumFacing facing = EnumFacing.byIndex(facingIndex);
						return block.getDefaultState().withProperty(BlockColoredRotatable.FACING, facing);
					},
					tileEntityNBT -> BlockFlattening.TileEntityAction.REMOVE
			));
		});

		ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS.getBlocksMap().forEach((color, blockColoredMultiRotatable) -> {
			flatteningDefinitions.add(new BlockFlattening.FlatteningDefinition(
					"colored_multi_rotatable",
					color.getMetadata(),
					blockColoredMultiRotatable,
					(block, tileEntityNBT) -> {
						final int facingIndex = Objects.requireNonNull(tileEntityNBT).getInteger("facing");
						final EnumFacing facing = EnumFacing.byIndex(facingIndex);
						return block.getDefaultState().withProperty(BlockColoredRotatable.FACING, facing);
					},
					tileEntityNBT -> {
						tileEntityNBT.removeTag("Facing");
						return BlockFlattening.TileEntityAction.KEEP;
					}
			));
		});

		ModBlocks.VariantGroups.VARIANTS_BLOCKS.getBlocksMap().forEach((type, blockVariants) -> {
			//noinspection deprecation
			flatteningDefinitions.add(new BlockFlattening.FlatteningDefinition(
					"variants",
					type.getMeta(),
					blockVariants,
					(block, tileEntityNBT) -> block.getDefaultState(),
					null
			));
		});

		ModBlocks.VariantGroups.TERRACOTTA_SLABS.getSlabGroupsMap().forEach((color, slabGroup) -> {
			//noinspection deprecation
			final BlockColouredSlab.EnumColourGroup colourGroup = Objects.requireNonNull(BlockColouredSlab.EnumColourGroup.getGroupForColour(color));

			final String oldSingleName = colourGroup == BlockColouredSlab.EnumColourGroup.LOW ? "stained_clay_slab_low" : "stained_clay_slab_high";
			final String oldDoubleName = "double_" + oldSingleName;

			final int metadata = colourGroup.getOffsetMetadata(color);

			// Single slab, bottom half
			flatteningDefinitions.add(new BlockFlattening.FlatteningDefinition(
					oldSingleName,
					metadata,
					slabGroup.getSingleSlab(),
					(block, tileEntityNBT) -> block.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM),
					null
			));

			// Single slab, top half
			flatteningDefinitions.add(new BlockFlattening.FlatteningDefinition(
					oldSingleName,
					metadata | 8,
					slabGroup.getSingleSlab(),
					(block, tileEntityNBT) -> block.getDefaultState().withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.TOP),
					null
			));

			// Double slab
			flatteningDefinitions.add(new BlockFlattening.FlatteningDefinition(
					oldDoubleName,
					metadata,
					slabGroup.getDoubleSlab(),
					(block, tileEntityNBT) -> block.getDefaultState(),
					null
			));
		});

		return new BlockFlattening(flatteningDefinitions.build());
	}
}
