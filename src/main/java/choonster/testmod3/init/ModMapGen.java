package choonster.testmod3.init;

import choonster.testmod3.world.gen.WorldGenBanner;
import choonster.testmod3.world.gen.WorldGenOres;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModMapGen {
	public static void registerWorldGenerators() {
		GameRegistry.registerWorldGenerator(new WorldGenBanner(), 100);
		GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);
	}
}
