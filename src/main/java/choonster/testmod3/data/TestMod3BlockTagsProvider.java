package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;

/**
 * Generates this mod's block tags.
 *
 * @author Choonster
 */
public class TestMod3BlockTagsProvider extends BlockTagsProvider {
	public TestMod3BlockTagsProvider(final DataGenerator generatorIn, @Nullable final ExistingFileHelper existingFileHelper) {
		super(generatorIn, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		
	}
}
