package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModItems;
import net.minecraft.data.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

/**
 * Generates this mod's item tags.
 *
 * @author Choonster
 */
public class TestMod3ItemTagsProvider extends ItemTagsProvider {
	public TestMod3ItemTagsProvider(final DataGenerator dataGenerator, final BlockTagsProvider blockTagProvider, @Nullable final ExistingFileHelper existingFileHelper) {
		super(dataGenerator, blockTagProvider, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ItemTags.ARROWS).add(
				ModItems.ARROW.get(),
				ModItems.BLOCK_DETECTION_ARROW.get()
		);
	}
}
