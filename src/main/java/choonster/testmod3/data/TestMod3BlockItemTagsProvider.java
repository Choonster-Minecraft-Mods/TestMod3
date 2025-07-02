package choonster.testmod3.data;

import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.init.ModTags;
import net.minecraft.data.tags.BlockItemTagsProvider;
import net.minecraft.tags.ItemTags;

/**
 * Generates this mod's block item tags.
 *
 * @author Choonster
 */
public abstract class TestMod3BlockItemTagsProvider extends BlockItemTagsProvider {
	@Override
	protected void run() {
		tag(ModTags.Blocks.SAPLINGS, ItemTags.SAPLINGS)
				.add(
						ModBlocks.OAK_SAPLING.get(),
						ModBlocks.SPRUCE_SAPLING.get(),
						ModBlocks.BIRCH_SAPLING.get(),
						ModBlocks.JUNGLE_SAPLING.get(),
						ModBlocks.ACACIA_SAPLING.get(),
						ModBlocks.DARK_OAK_SAPLING.get()
				);
	}
}
