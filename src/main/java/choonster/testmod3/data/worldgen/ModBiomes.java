package choonster.testmod3.data.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.Biome;

import static choonster.testmod3.init.levelgen.ModBiomes.DESERT_TEST;

/**
 * Registers this mod's {@link Biome}s during datagen.
 *
 * @author Choonster
 */
public class ModBiomes {
	public static void bootstrap(final BootstapContext<Biome> context) {
		final var placedFeatures = context.lookup(Registries.PLACED_FEATURE);
		final var configuredWorldCarvers = context.lookup(Registries.CONFIGURED_CARVER);

		// TODO: Figure out SurfaceRules to replace SurfaceBuilders
		context.register(DESERT_TEST, OverworldBiomes.desert(placedFeatures, configuredWorldCarvers));
	}
}
