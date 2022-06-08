package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.levelgen.ModBiomes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

/**
 * @author Choonster
 */
public class TestMod3BiomeTagsProvider extends BiomeTagsProvider {
	public TestMod3BiomeTagsProvider(final DataGenerator p_211094_, @Nullable final ExistingFileHelper existingFileHelper) {
		super(p_211094_, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(Tags.Biomes.IS_HOT).add(
				ModBiomes.DESERT_TEST.get()
		);

		tag(Tags.Biomes.IS_DRY).add(
				ModBiomes.DESERT_TEST.get()
		);

		tag(Tags.Biomes.IS_SANDY).add(
				ModBiomes.DESERT_TEST.get()
		);

		tag(Tags.Biomes.IS_OVERWORLD).add(
				ModBiomes.DESERT_TEST.get()
		);
	}
}
