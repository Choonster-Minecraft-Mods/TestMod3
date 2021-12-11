package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Objects;

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
			OverworldBiomes::desert // TODO: Figure out SurfaceRules to replace SurfaceBuilders
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

}
