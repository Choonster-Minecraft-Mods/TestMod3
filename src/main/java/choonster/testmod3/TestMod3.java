package choonster.testmod3;

import choonster.testmod3.client.gui.GuiHandler;
import choonster.testmod3.init.*;
import choonster.testmod3.proxy.IProxy;
import choonster.testmod3.remap.Remapper;
import choonster.testmod3.tests.Tests;
import choonster.testmod3.tweak.spawnerdrops.SpawnerDrops;
import choonster.testmod3.util.BlockDumper;
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

@Mod(modid = TestMod3.MODID, name = TestMod3.NAME, acceptedMinecraftVersions = "[1.12]")
public class TestMod3 {
	public static final String MODID = "testmod3";
	public static final String NAME = "Test Mod 3";

	public static final CreativeTabTestMod3 creativeTab = new CreativeTabTestMod3();

	@SidedProxy(clientSide = "choonster.testmod3.proxy.CombinedClientProxy", serverSide = "choonster.testmod3.proxy.DedicatedServerProxy")
	public static IProxy proxy;

	@Instance(MODID)
	public static TestMod3 instance;

	public static SimpleNetworkWrapper network;

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		Logger.setLogger(event.getModLog());

		FMLLog.bigWarning("Random UUID: %s", UUID.randomUUID().toString());

		ModCapabilities.registerCapabilities();

		network = NetworkRegistry.INSTANCE.newSimpleChannel(MODID);

		ModMessages.registerMessages();
		ModItems.initialiseItems();
		ModFluids.registerFluidContainers();
		ModMapGen.registerMapGen();
		ModEntities.registerEntities();
		ModDispenseBehaviors.registerDispenseBehaviors();
		ModLootTables.registerLootTables();

		proxy.preInit();
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		ModRecipes.registerRecipes();
		ModRecipes.removeCraftingRecipes();
		ModMapGen.registerWorldGenerators();
		ModEntities.addSpawns();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		proxy.init();
	}

	@EventHandler
	public void postInit(final FMLPostInitializationEvent event) {
		BlockDumper.dump();

		proxy.postInit();

		Tests.runTests();
	}

	@EventHandler
	public void serverStarting(final FMLServerStartingEvent event) {
		ModCommands.registerCommands(event);
	}

	@EventHandler
	public void serverStopped(final FMLServerStoppedEvent event) {
		SpawnerDrops.serverStopped();
	}

	@EventHandler
	public void missingMappings(final FMLMissingMappingsEvent event) {
		Remapper.remap(event.get());
	}
}
