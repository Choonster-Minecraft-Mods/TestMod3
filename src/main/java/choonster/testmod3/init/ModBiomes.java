package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.StructureFeatures;
import net.minecraft.data.worldgen.biome.VanillaBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.fmllegacy.RegistryObject;
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

		private static ResourceKey<Biome> key(final Biome biome) {
			return ResourceKey.create(ForgeRegistries.Keys.BIOMES, Objects.requireNonNull(ForgeRegistries.BIOMES.getKey(biome), "Biome registry name was null"));
		}
	}

	private static class Utils {
		private static final Method CALCULATE_SKY_COLOR = ObfuscationReflectionHelper.findMethod(VanillaBiomes.class, /* calculateSkyColor */ "m_127332_", float.class);

		public static Supplier<ConfiguredSurfaceBuilder<?>> surfaceBuilder(final ResourceKey<ConfiguredSurfaceBuilder<?>> key) {
			return () -> BuiltinRegistries.CONFIGURED_SURFACE_BUILDER.getOrThrow(key);
		}

		/**
		 * Creates a desert biome.
		 * <p>
		 * Adapted from {@link VanillaBiomes#desertBiome(float, float, boolean, boolean, boolean)}
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
			final MobSpawnSettings.Builder mobSpawnInfoBuilder = new MobSpawnSettings.Builder();
			BiomeDefaultFeatures.desertSpawns(mobSpawnInfoBuilder);

			final BiomeGenerationSettings.Builder biomeGenerationSettingBuilder = new BiomeGenerationSettings.Builder()
					.surfaceBuilder(surfaceBuilder);

			if (hasVillageAndOutpost) {
				biomeGenerationSettingBuilder.addStructureStart(StructureFeatures.VILLAGE_DESERT);
				biomeGenerationSettingBuilder.addStructureStart(StructureFeatures.PILLAGER_OUTPOST);
			}

			if (hasDesertPyramid) {
				biomeGenerationSettingBuilder.addStructureStart(StructureFeatures.DESERT_PYRAMID);
			}

			if (hasFossils) {
				BiomeDefaultFeatures.addFossilDecoration(biomeGenerationSettingBuilder);
			}

			BiomeDefaultFeatures.addDefaultOverworldLandStructures(biomeGenerationSettingBuilder);
			biomeGenerationSettingBuilder.addStructureStart(StructureFeatures.RUINED_PORTAL_DESERT);
			BiomeDefaultFeatures.addDefaultCarvers(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDesertLakes(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultOres(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultFlowers(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultGrass(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDesertVegetation(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultMushrooms(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDesertExtraVegetation(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDefaultSprings(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addDesertExtraDecoration(biomeGenerationSettingBuilder);
			BiomeDefaultFeatures.addSurfaceFreezing(biomeGenerationSettingBuilder);

			final int skyColour;
			try {
				skyColour = (int) CALCULATE_SKY_COLOR.invoke(null, 2);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Unable to get sky colour", e);
			}

			return new Biome.BiomeBuilder()
					.precipitation(Biome.Precipitation.NONE)
					.biomeCategory(Biome.BiomeCategory.DESERT)
					.depth(depth)
					.scale(scale)
					.temperature(2)
					.downfall(0)
					.specialEffects(
							new BiomeSpecialEffects.Builder()
									.waterColor(0x3f76e4)
									.waterFogColor(0x50533)
									.fogColor(0xc0d8ff)
									.skyColor(skyColour)
									.ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
									.build()
					)
					.mobSpawnSettings(mobSpawnInfoBuilder.build())
					.generationSettings(biomeGenerationSettingBuilder.build())
					.build();
		}
	}
}
