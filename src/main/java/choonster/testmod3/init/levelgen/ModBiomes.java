package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;

/**
 * Registers this mod's {@link Biome}s
 */
public class ModBiomes {
	/**
	 * A Desert-like biome with Red Sand as the top block and Brick Block as the filler block.
	 */
	public static final ResourceKey<Biome> DESERT_TEST = key("desert_test");

	private static ResourceKey<Biome> key(final String name) {
		return ResourceKey.create(Registries.BIOME, new ResourceLocation(TestMod3.MODID, name));
	}
}
