package com.choonster.testmod3.init;

import com.choonster.testmod3.event.MapGenHandler;
import com.choonster.testmod3.world.gen.WorldGenBanner;
import com.choonster.testmod3.world.gen.structure.MapGenScatteredFeatureModBiomes;
import net.minecraft.world.gen.structure.MapGenStructureIO;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ModMapGen {
	public static void registerMapGen() {
		MapGenStructureIO.registerStructure(MapGenScatteredFeatureModBiomes.Start.class, "testmod3_MapGenScatteredFeatureModBiomes");

		MinecraftForge.TERRAIN_GEN_BUS.register(new MapGenHandler());
	}

	public static void registerWorldGenerators() {
		GameRegistry.registerWorldGenerator(new WorldGenBanner(), 100);
	}
}
