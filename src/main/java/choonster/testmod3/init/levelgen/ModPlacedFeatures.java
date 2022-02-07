package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.registry.DeferredVanillaRegister;
import choonster.testmod3.registry.VanillaRegistryObject;
import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16Filter;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;

/**
 * Registers this mod's {@link PlacedFeature}s.
 *
 * @author Choonster
 */
public class ModPlacedFeatures {
	private static final DeferredVanillaRegister<PlacedFeature> PLACED_FEATURES =
			DeferredVanillaRegister.create(BuiltinRegistries.PLACED_FEATURE, TestMod3.MODID);

	private static boolean isInitialised = false;

	// Places a banner at the surface, but only in chunks with coordinates divisible by 16.
	// Test for this thread:
	// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
	public static final VanillaRegistryObject<PlacedFeature> BANNER = PLACED_FEATURES.register("banner",
			() -> ModConfiguredFeatures.BANNER.get().placed(
					CountPlacement.of(1),
					InSquarePlacement.spread(),
					PlacementUtils.HEIGHTMAP,
					InChunksDivisibleBy16Filter.instance()
			)
	);

	public static final VanillaRegistryObject<PlacedFeature> ORE_IRON_NETHER = PLACED_FEATURES.register("ore_iron_nether",
			() -> ModConfiguredFeatures.ORE_IRON_NETHER.get().placed(
					CountPlacement.of(16),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
			)
	);

	public static final VanillaRegistryObject<PlacedFeature> ORE_IRON_END = PLACED_FEATURES.register("ore_iron_end",
			() -> ModConfiguredFeatures.ORE_IRON_END.get().placed(
					CountPlacement.of(16),
					InSquarePlacement.spread(),
					HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(118))
			)
	);

	/**
	 * Registers the {@link DeferredVanillaRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		// Ensure this is run after the ConfiguredFeature registration in ModConfiguredFeatures
		PLACED_FEATURES.register(modEventBus, EventPriority.LOW);

		isInitialised = true;
	}
}
