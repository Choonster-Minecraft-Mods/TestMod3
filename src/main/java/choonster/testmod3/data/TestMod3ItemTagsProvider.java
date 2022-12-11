package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.init.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Generates this mod's item tags.
 *
 * @author Choonster
 */
public class TestMod3ItemTagsProvider extends ItemTagsProvider {
	public TestMod3ItemTagsProvider(
			final PackOutput output,
			final CompletableFuture<HolderLookup.Provider> holderLookup,
			final TagsProvider<Block> blockTagsProvider,
			@Nullable final ExistingFileHelper existingFileHelper
	) {
		super(output, holderLookup, blockTagsProvider, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(final HolderLookup.Provider p_256380_) {
		tag(ItemTags.ARROWS).add(
				ModItems.ARROW.get(),
				ModItems.BLOCK_DETECTION_ARROW.get()
		);

		copy(ModTags.Blocks.SAPLINGS, ItemTags.SAPLINGS);

		tag(ModTags.Items.VANILLA_DYES)
				.add(
						Items.WHITE_DYE,
						Items.ORANGE_DYE,
						Items.MAGENTA_DYE,
						Items.LIGHT_BLUE_DYE,
						Items.YELLOW_DYE,
						Items.LIME_DYE,
						Items.PINK_DYE,
						Items.GRAY_DYE,
						Items.LIGHT_GRAY_DYE,
						Items.CYAN_DYE,
						Items.PURPLE_DYE,
						Items.BLUE_DYE,
						Items.BROWN_DYE,
						Items.GREEN_DYE,
						Items.RED_DYE
				);

		tag(ModTags.Items.VANILLA_TERRACOTTA)
				.add(
						Items.WHITE_TERRACOTTA,
						Items.ORANGE_TERRACOTTA,
						Items.MAGENTA_TERRACOTTA,
						Items.LIGHT_BLUE_TERRACOTTA,
						Items.YELLOW_TERRACOTTA,
						Items.LIME_TERRACOTTA,
						Items.PINK_TERRACOTTA,
						Items.GRAY_TERRACOTTA,
						Items.LIGHT_GRAY_TERRACOTTA,
						Items.CYAN_TERRACOTTA,
						Items.PURPLE_TERRACOTTA,
						Items.BLUE_TERRACOTTA,
						Items.BROWN_TERRACOTTA,
						Items.GREEN_TERRACOTTA,
						Items.RED_TERRACOTTA,
						Items.BLACK_TERRACOTTA
				);

		tag(ItemTags.PLANKS)
				.add(
						ModBlocks.PLANKS.get().asItem()
				);

		tag(ModTags.Items.RUBBER)
				.add(
						ModItems.RUBBER.get()
				);
	}
}
