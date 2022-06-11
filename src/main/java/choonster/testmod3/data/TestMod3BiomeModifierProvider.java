package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.levelgen.ModPlacedFeatures;
import choonster.testmod3.world.level.biome.modifier.AddFeaturesBiomeModifier;
import choonster.testmod3.world.level.biome.modifier.AddMobSpawnBiomeModifier;
import choonster.testmod3.world.level.biome.modifier.CopyMobSpawnsBiomeModifier;
import com.google.common.base.Preconditions;
import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.JsonOps;
import cpw.mods.modlauncher.api.LamdbaExceptionUtils;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.packs.PackType;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Choonster
 */
public class TestMod3BiomeModifierProvider implements DataProvider {
	private static final Logger LOGGER = LogUtils.getLogger();

	private final DataGenerator generator;
	private final Map<String, BiomeModifier> toSerialize = new HashMap<>();

	public TestMod3BiomeModifierProvider(final DataGenerator generator) {
		this.generator = generator;
	}

	protected void addModifiers(final RegistryOps<JsonElement> ops) {
		final var biomeRegistry = ops.registry(Registry.BIOME_REGISTRY).orElseThrow();
		final var featureRegistry = ops.registry(Registry.PLACED_FEATURE_REGISTRY).orElseThrow();

		addModifier(
				"add_guardian_spawn_to_oceans",
				AddMobSpawnBiomeModifier.create(
						tag(biomeRegistry, BiomeTags.IS_OCEAN),
						MobCategory.WATER_CREATURE,
						EntityType.GUARDIAN,
						100,
						5,
						20
				)
		);

		addModifier(
				"copy_creeper_spawns_for_player_avoiding_creeper",
				CopyMobSpawnsBiomeModifier.create(
						MobCategory.MONSTER,
						EntityType.CREEPER,
						MobCategory.MONSTER,
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
				AddFeaturesBiomeModifier.create(
						tag(biomeRegistry, BiomeTags.IS_OVERWORLD),
						feature(featureRegistry, ModPlacedFeatures.BANNER),
						GenerationStep.Decoration.SURFACE_STRUCTURES
				)
		);

		addModifier(
				"ore_iron_nether",
				AddFeaturesBiomeModifier.create(
						tag(biomeRegistry, BiomeTags.IS_NETHER),
						feature(featureRegistry, ModPlacedFeatures.ORE_IRON_NETHER),
						GenerationStep.Decoration.UNDERGROUND_ORES
				)
		);

		addModifier(
				"ore_iron_end",
				AddFeaturesBiomeModifier.create(
						tag(biomeRegistry, BiomeTags.IS_END),
						feature(featureRegistry, ModPlacedFeatures.ORE_IRON_END),
						GenerationStep.Decoration.UNDERGROUND_ORES
				)
		);
	}

	@Override
	public void run(final CachedOutput cache) {
		final var ops = RegistryOps.create(JsonOps.INSTANCE, RegistryAccess.builtinCopy());

		addModifiers(ops);

		final var outputFolder = generator.getOutputFolder();
		final var directory = PackType.SERVER_DATA.getDirectory();

		final var biomeModifiersRegistryID = ForgeRegistries.Keys.BIOME_MODIFIERS.location();
		final var biomeModifiersNamespace = biomeModifiersRegistryID.getNamespace();
		final var biomeModifiersPath = biomeModifiersRegistryID.getPath();

		toSerialize.forEach((modifier, instance) -> {
			final var pathString = String.join(
					"/",
					directory,
					TestMod3.MODID,
					biomeModifiersNamespace,
					biomeModifiersPath,
					modifier + ".json"
			);

			final var path = outputFolder.resolve(pathString);

			BiomeModifier.DIRECT_CODEC.encodeStart(ops, instance)
					.resultOrPartial(msg -> LOGGER.error("Failed to encode {}: {}", path, msg))
					.ifPresent(LamdbaExceptionUtils.rethrowConsumer(json -> DataProvider.saveStable(cache, json, path)));
		});
	}

	protected <T extends BiomeModifier> void addModifier(final String modifier, final T instance) {
		toSerialize.put(modifier, instance);
	}

	@Override
	public String getName() {
		return "TestMod3BiomeModifiers";
	}

	protected HolderSet<Biome> tag(final Registry<Biome> registry, final TagKey<Biome> key) {
		return new HolderSet.Named<>(registry, key);
	}

	protected HolderSet<PlacedFeature> feature(final Registry<PlacedFeature> registry, final RegistryObject<PlacedFeature> feature) {
		final var key = Preconditions.checkNotNull(feature.getKey(), "%s has no registry key", feature.get());
		return HolderSet.direct(registry.getHolderOrThrow(key));
	}
}
