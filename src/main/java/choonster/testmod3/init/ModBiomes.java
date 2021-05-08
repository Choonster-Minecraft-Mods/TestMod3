package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.registry.WorldGenRegistries;
import net.minecraft.world.biome.*;
import net.minecraft.world.gen.feature.structure.StructureFeatures;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Supplier;

import static net.minecraftforge.common.BiomeDictionary.Type.*;

/**
 * Registers this mod's {@link Biome}s
 */
public class ModBiomes {
	private static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, TestMod3.MODID);

	private static boolean isInitialised = false;

	/**
	 * A Desert-like biome with Red Sand as the top block and Brick Block as the filler block.
	 */
	public static final RegistryObject<Biome> DESERT_TEST = BIOMES.register("desert_test",
			() -> Utils.makeDesertBiome(
					Utils.surfaceBuilder(ModConfiguredSurfaceBuilders.DESERT_TEST),
					0.125f, 0.05f, true, true, true
			)
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		BIOMES.register(modEventBus);

		isInitialised = true;
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
	public static class RegistrationHandler {
		@SubscribeEvent
		public static void setupBiomes(final FMLCommonSetupEvent event) {
			event.enqueueWork(() -> {
				setupBiome(DESERT_TEST.get(), BiomeManager.BiomeType.DESERT, 1000, HOT, DRY, SANDY, OVERWORLD);
			});
		}

		private static void setupBiome(final Biome biome, final BiomeManager.BiomeType biomeType, final int weight, final BiomeDictionary.Type... types) {
			BiomeDictionary.addTypes(key(biome), types);
			BiomeManager.addBiome(biomeType, new BiomeManager.BiomeEntry(key(biome), weight));
		}

		private static RegistryKey<Biome> key(final Biome biome) {
			return RegistryKey.getOrCreateKey(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null"));
		}
	}

	private static class Utils {
		private static final Method GET_SKY_COLOR_WITH_TEMPERATURE_MODIFIER = ObfuscationReflectionHelper.findMethod(BiomeMaker.class, /* getSkyColorWithTemperatureModifier */ "func_244206_a", float.class);

		public static Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder(final RegistryKey<ConfiguredSurfaceBuilder<?>> key) {
			return () -> WorldGenRegistries.CONFIGURED_SURFACE_BUILDER.getOrThrow(key);
		}

		/**
		 * Creates a desert biome.
		 * <p>
		 * Adapted from {@link BiomeMaker#makeDesertBiome(float, float, boolean, boolean, boolean)}
		 *
		 * @return The biome
		 */
		public static Biome makeDesertBiome(
				final Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder,
				final float depth,
				final float scale,
				final boolean hasVillageAndOutpost,
				final boolean hasDesertPyramid,
				final boolean hasFossils
		) {
			final MobSpawnInfo.Builder mobSpawnInfoBuilder = new MobSpawnInfo.Builder();
			DefaultBiomeFeatures.withDesertMobs(mobSpawnInfoBuilder);

			final BiomeGenerationSettings.Builder biomeGenerationSettingBuilder = new BiomeGenerationSettings.Builder()
					.withSurfaceBuilder(surfaceBuilder);

			if (hasVillageAndOutpost) {
				biomeGenerationSettingBuilder.withStructure(StructureFeatures.VILLAGE_DESERT);
				biomeGenerationSettingBuilder.withStructure(StructureFeatures.PILLAGER_OUTPOST);
			}

			if (hasDesertPyramid) {
				biomeGenerationSettingBuilder.withStructure(StructureFeatures.DESERT_PYRAMID);
			}

			if (hasFossils) {
				DefaultBiomeFeatures.withFossils(biomeGenerationSettingBuilder);
			}

			DefaultBiomeFeatures.withStrongholdAndMineshaft(biomeGenerationSettingBuilder);
			biomeGenerationSettingBuilder.withStructure(StructureFeatures.RUINED_PORTAL_DESERT);
			DefaultBiomeFeatures.withCavesAndCanyons(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withLavaLakes(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withMonsterRoom(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withCommonOverworldBlocks(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withOverworldOres(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withDisks(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withDefaultFlowers(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withBadlandsGrass(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withDesertDeadBushes(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withNormalMushroomGeneration(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withDesertVegetation(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withLavaAndWaterSprings(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withDesertWells(biomeGenerationSettingBuilder);
			DefaultBiomeFeatures.withFrozenTopLayer(biomeGenerationSettingBuilder);

			final int skyColour;
			try {
				skyColour = (int) GET_SKY_COLOR_WITH_TEMPERATURE_MODIFIER.invoke(null, 2);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Unable to get sky colour", e);
			}

			return new Biome.Builder()
					.precipitation(Biome.RainType.NONE)
					.category(Biome.Category.DESERT)
					.depth(depth)
					.scale(scale)
					.temperature(2)
					.downfall(0)
					.setEffects(
							new BiomeAmbience.Builder()
									.setWaterColor(0x3f76e4)
									.setWaterFogColor(0x50533)
									.setFogColor(0xc0d8ff)
									.withSkyColor(skyColour)
									.setMoodSound(MoodSoundAmbience.DEFAULT_CAVE)
									.build()
					)
					.withMobSpawnSettings(mobSpawnInfoBuilder.build())
					.withGenerationSettings(biomeGenerationSettingBuilder.build())
					.build();
		}
	}
}
