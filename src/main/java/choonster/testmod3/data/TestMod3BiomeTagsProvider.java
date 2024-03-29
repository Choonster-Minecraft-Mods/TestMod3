package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.levelgen.ModBiomes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.Tags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

/**
 * Generates this mod's biome tags.
 *
 * @author Choonster
 */
public class TestMod3BiomeTagsProvider extends BiomeTagsProvider {
	public TestMod3BiomeTagsProvider(final PackOutput packOutput, final CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable final ExistingFileHelper existingFileHelper) {
		super(packOutput, lookupProvider, TestMod3.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(final HolderLookup.Provider p_256485_) {
		tag(ModBiomes.DESERT_TEST, Tags.Biomes.IS_HOT_OVERWORLD, Tags.Biomes.IS_DRY_OVERWORLD, Tags.Biomes.IS_SANDY);
	}

	@SafeVarargs
	private void tag(final ResourceKey<Biome> biome, final TagKey<Biome>... tags) {
		for (final var key : tags) {
			tag(key).add(biome);
		}
	}
}
