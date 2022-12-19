package choonster.testmod3.data.worldgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModEntities;
import choonster.testmod3.world.level.biome.modifier.CopyMobSpawnsBiomeModifier;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddFeaturesBiomeModifier;
import net.minecraftforge.common.world.ForgeBiomeModifiers.AddSpawnsBiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link BiomeModifier}s during datagen.
 *
 * @author Choonster
 */
public class ModBiomeModifiers {
	public static final ResourceKey<BiomeModifier> ADD_GUARDIAN_SPAWN_TO_OCEANS = key("add_guardian_spawn_to_oceans");
	public static final ResourceKey<BiomeModifier> COPY_CREEPER_SPAWNS_FOR_PLAYER_AVOIDING_CREEPER = key("copy_creeper_spawns_for_player_avoiding_creeper");
	public static final ResourceKey<BiomeModifier> BANNERS_IN_CHUNKS_DIVISIBLE_BY_16 = key("banners_in_chunks_divisible_by_16");
	public static final ResourceKey<BiomeModifier> ORE_IRON_NETHER = key("ore_iron_nether");
	public static final ResourceKey<BiomeModifier> ORE_IRON_END = key("ore_iron_end");

	public static void bootstrap(final BootstapContext<BiomeModifier> context) {
		final var biomes = context.lookup(Registries.BIOME);
		final var features = context.lookup(Registries.PLACED_FEATURE);

		context.register(
				ADD_GUARDIAN_SPAWN_TO_OCEANS,
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

		context.register(
				COPY_CREEPER_SPAWNS_FOR_PLAYER_AVOIDING_CREEPER,
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
		context.register(
				BANNERS_IN_CHUNKS_DIVISIBLE_BY_16,
				new AddFeaturesBiomeModifier(
						tag(biomes, BiomeTags.IS_OVERWORLD),
						feature(features, ModPlacedFeatures.BANNER),
						GenerationStep.Decoration.SURFACE_STRUCTURES
				)
		);

		context.register(
				ORE_IRON_NETHER,
				new AddFeaturesBiomeModifier(
						tag(biomes, BiomeTags.IS_NETHER),
						feature(features, ModPlacedFeatures.ORE_IRON_NETHER),
						GenerationStep.Decoration.UNDERGROUND_ORES
				)
		);

		context.register(
				ORE_IRON_END,
				new AddFeaturesBiomeModifier(
						tag(biomes, BiomeTags.IS_END),
						feature(features, ModPlacedFeatures.ORE_IRON_END),
						GenerationStep.Decoration.UNDERGROUND_ORES
				)
		);
	}

	private static ResourceKey<BiomeModifier> key(final String name) {
		return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, new ResourceLocation(TestMod3.MODID, name));
	}

	private static HolderSet<Biome> tag(final HolderGetter<Biome> holderGetter, final TagKey<Biome> key) {
		return holderGetter.getOrThrow(key);
	}

	private static HolderSet<PlacedFeature> feature(final HolderGetter<PlacedFeature> holderGetter, final ResourceKey<PlacedFeature> feature) {
		return HolderSet.direct(holderGetter.getOrThrow(feature));
	}
}
