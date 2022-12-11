package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.worldgen.ModPlacedFeatures;
import choonster.testmod3.init.ModDataProviders;
import choonster.testmod3.init.ModEntities;
import choonster.testmod3.world.level.biome.modifier.CopyMobSpawnsBiomeModifier;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.JsonCodecProvider;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * @author Choonster
 */
public class TestMod3BiomeModifierProvider implements DataProvider {
	private final DataGenerator generator;
	private final ExistingFileHelper existingFileHelper;
	private final CompletableFuture<HolderLookup.Provider> lookupProvider;
	private final Map<ResourceLocation, BiomeModifier> toSerialize = new HashMap<>();

	public TestMod3BiomeModifierProvider(final DataGenerator generator, final ExistingFileHelper existingFileHelper, final CompletableFuture<HolderLookup.Provider> lookupProvider) {
		this.generator = generator;
		this.existingFileHelper = existingFileHelper;
		this.lookupProvider = lookupProvider;
	}

	protected void addModifiers(final HolderLookup.Provider lookupProvider) {
		final var biomes = ModDataProviders.wrapContextLookup(lookupProvider.lookupOrThrow(Registries.BIOME));
		final var features = lookupProvider.lookupOrThrow(Registries.PLACED_FEATURE);

		addModifier(
				"add_guardian_spawn_to_oceans",
				AddSpawnsBiomeModifier.singleSpawn(
						tag(biomes, BiomeTags.IS_OCEAN),
						new MobSpawnSettings.SpawnerData(
								EntityType.GUARDIAN,
								100,
								5,
								20
						)
				)
		);

		addModifier(
				"copy_creeper_spawns_for_player_avoiding_creeper",
				CopyMobSpawnsBiomeModifier.create(
						EntityType.CREEPER,
						ModEntities.PLAYER_AVOIDING_CREEPER.get()
				)
		);

		/*
			Generates Banners with a specific pattern in chunks with coordinates divisible by 16.
			Only generates in biomes with the Overworld type in the Biome Dictionary.

			Test for this thread:
			http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
		*/
		addModifier(
				"banners_in_chunks_divisible_by_16",
				new AddFeaturesBiomeModifier(
						tag(biomes, BiomeTags.IS_OVERWORLD),
						feature(features, ModPlacedFeatures.BANNER),
						GenerationStep.Decoration.SURFACE_STRUCTURES
				)
		);

		addModifier(
				"ore_iron_nether",
				new AddFeaturesBiomeModifier(
						tag(biomes, BiomeTags.IS_NETHER),
						feature(features, ModPlacedFeatures.ORE_IRON_NETHER),
						GenerationStep.Decoration.UNDERGROUND_ORES
				)
		);

		addModifier(
				"ore_iron_end",
				new AddFeaturesBiomeModifier(
						tag(biomes, BiomeTags.IS_END),
						feature(features, ModPlacedFeatures.ORE_IRON_END),
						GenerationStep.Decoration.UNDERGROUND_ORES
				)
		);
	}

	@Override
	public CompletableFuture<?> run(final CachedOutput cache) {
		return lookupProvider.thenCompose((lookupProvider) -> {
			final var ops = RegistryOps.create(JsonOps.INSTANCE, lookupProvider);

			addModifiers(lookupProvider);

			final JsonCodecProvider<BiomeModifier> provider = JsonCodecProvider.forDatapackRegistry(
					generator,
					existingFileHelper,
					TestMod3.MODID,
					ops,
					ForgeRegistries.Keys.BIOME_MODIFIERS,
					toSerialize
			);

			return provider.run(cache);
		});
	}

	protected <T extends BiomeModifier> void addModifier(final String modifier, final T instance) {
		toSerialize.put(new ResourceLocation(TestMod3.MODID, modifier), instance);
	}

	@Override
	public String getName() {
		return "TestMod3BiomeModifiers";
	}

	protected HolderSet<Biome> tag(final HolderGetter<Biome> holderGetter, final TagKey<Biome> key) {
		return holderGetter.getOrThrow(key);
	}

	protected HolderSet<PlacedFeature> feature(final HolderGetter<PlacedFeature> holderGetter, final ResourceKey<PlacedFeature> feature) {
		return HolderSet.direct(holderGetter.getOrThrow(feature));
	}
}
