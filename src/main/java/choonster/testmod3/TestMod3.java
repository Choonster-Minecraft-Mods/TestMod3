package choonster.testmod3;

import choonster.testmod3.client.gui.GuiHandler;
import choonster.testmod3.config.Config;
import choonster.testmod3.event.BlockEventHandler;
import choonster.testmod3.event.ItemCombinationHandler;
import choonster.testmod3.event.NetworkEventHandler;
import choonster.testmod3.event.PlayerEventHandler;
import choonster.testmod3.init.*;
import choonster.testmod3.proxy.IProxy;
import choonster.testmod3.remap.Remapper;
import choonster.testmod3.tests.Tests;
import choonster.testmod3.tweak.snowbuildup.SnowBuildup;
import choonster.testmod3.tweak.spawnerdrops.SpawnerDrops;
import choonster.testmod3.util.BlockDumper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

import java.util.UUID;

@Mod(modid = TestMod3.MODID, acceptedMinecraftVersions = "[1.10.2]", guiFactory = "choonster.testmod3.config.GuiConfigFactoryTestMod3")
public class TestMod3 {
	public static final String MODID = "testmod3";

	public static CreativeTabTestMod3 creativeTab;

	@SidedProxy(clientSide = "choonster.testmod3.proxy.CombinedClientProxy", serverSide = "choonster.testmod3.proxy.DedicatedServerProxy")
	public static IProxy proxy;

	@Instance(MODID)
	public static TestMod3 instance;

	public static SimpleNetworkWrapper network;

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		Logger.setLogger(event.getModLog());

		FMLLog.bigWarning("Random UUID: %s", UUID.randomUUID().toString());

		creativeTab = new CreativeTabTestMod3();
		Config.load(event);

		ModCapabilities.registerCapabilities();

		MinecraftForge.EVENT_BUS.register(new BlockEventHandler());
		MinecraftForge.EVENT_BUS.register(new NetworkEventHandler());
		MinecraftForge.EVENT_BUS.register(new PlayerEventHandler());
		MinecraftForge.EVENT_BUS.register(ItemCombinationHandler.class);

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

		ModSoundEvents.registerSounds();
		ModMessages.registerMessages();
		ModFluids.registerFluids();
		ModBlocks.registerBlocks();
		ModBlocks.registerTileEntities();
		ModItems.registerItems();
		ModFluids.registerFluidContainers();
		ModBiomes.registerBiomes();
		ModMapGen.registerMapGen();
		ModEntities.registerEntities();
		ModPotions.registerPotions();
		ModPotionTypes.registerPotionTypes();

		SnowBuildup.init();
		SpawnerDrops.init();

		proxy.preInit();
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		ModRecipes.registerRecipes();
		ModRecipes.removeCraftingRecipes();
		ModMapGen.registerWorldGenerators();
		ModEntities.addSpawns();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		FMLInterModComms.sendMessage("Waila", "register", "choonster.testmod3.compat.waila.WailaCompat.register");

		proxy.init();
	}

	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		BlockDumper.dump();

		proxy.postInit();

		Tests.runTests();
	}

	@EventHandler
	public void serverStarting(FMLServerStartingEvent event) {
		ModCommands.registerCommands(event);
	}

	@EventHandler
	public void serverStopped(FMLServerStoppedEvent event) {
		SpawnerDrops.serverStopped();
	}

	@EventHandler
	public void missingMapings(FMLMissingMappingsEvent event) {
		Remapper.remap(event.get());
	}
}
