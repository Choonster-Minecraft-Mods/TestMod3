package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.levelgen.ModBiomes;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;

/**
 * Generates this mod's biome tags.
 *
 * @author Choonster
 */
public class TestMod3BiomeTagsProvider extends BiomeTagsProvider {
	public TestMod3BiomeTagsProvider(final DataGenerator p_211094_, @Nullable final ExistingFileHelper existingFileHelper) {
		super(p_211094_, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags() {
		tag(ModBiomes.DESERT_TEST, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_DRY_OVERWORLD, Tags.Biomes.IS_SANDY);
	}

	@SafeVarargs
	private void tag(final RegistryObject<Biome> biome, final TagKey<Biome>... tags) {
		for (final var key : tags) {
			tag(key).add(biome.get());
		}
	}
}
