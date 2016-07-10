package choonster.testmod3.init;

import choonster.testmod3.event.MapGenHandler;
import choonster.testmod3.world.gen.WorldGenBanner;
import choonster.testmod3.world.gen.WorldGenOres;
import choonster.testmod3.world.gen.structure.MapGenScatteredFeatureModBiomes;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModMapGen {
	public static void registerMapGen() {
		MapGenStructureIO.registerStructure(MapGenScatteredFeatureModBiomes.Start.class, "testmod3:MapGenScatteredFeatureModBiomes");

		MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenHandler());
	}

	public static void registerWorldGenerators() {
		GameRegistry.registerWorldGenerator(new WorldGenBanner(), 100);
		GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);
	}
}
