package com.choonster.testmod3;

import com.choonster.testmod3.compat.nei.NEICompat;
import com.choonster.testmod3.config.Config;
import com.choonster.testmod3.event.BlockEventHandler;
import com.choonster.testmod3.event.BucketFillHandler;
import com.choonster.testmod3.init.*;
import com.choonster.testmod3.proxy.CommonProxy;
import com.choonster.testmod3.tests.Tests;
import com.choonster.testmod3.util.BiomeBlockReplacer;
import com.choonster.testmod3.util.BlockDumper;
import com.choonster.testmod3.world.gen.WorldGenOres;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid = TestMod3.MODID, version = TestMod3.VERSION, guiFactory = "com.choonster.testmod3.config.GuiConfigFactoryTestMod3")
public class TestMod3 {
	public static final String MODID = "testmod3";
	public static final String VERSION = "1.0";

	public static CreativeTabExample creativeTab;

	@SidedProxy(clientSide = "com.choonster.testmod3.proxy.CombinedClientProxy", serverSide = "com.choonster.testmod3.proxy.DedicatedServerProxy")
	public static CommonProxy proxy;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		creativeTab = new CreativeTabExample();
		Config.load(event);

		MinecraftForge.EVENT_BUS.register(new BucketFillHandler());
		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());

		ModFluids.registerFluids();
		ModBlocks.registerBlocks();
		ModItems.registerItems();
		ModFluids.registerBuckets();
		ModBiomes.registerBiomes();
		ModMapGen.registerMapGen();

		if (Loader.isModLoaded("NotEnoughItems")) {
			NEICompat.init();
		}

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModRecipes.registerRecipes();

		GameRegistry.registerWorldGenerator(new WorldGenOres(), 0);

		ModRecipes.removeCraftingRecipes();

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		MinecraftForge.TERRAIN_GEN_BUS.register(new BiomeBlockReplacer());

		BlockDumper.dump();

		proxy.postInit();

		Tests.runTests();
	}
}
