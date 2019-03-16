package choonster.testmod3;

import choonster.testmod3.client.gui.GuiHandler;
import choonster.testmod3.init.*;
import choonster.testmod3.proxy.IProxy;
import choonster.testmod3.tests.Tests;
import choonster.testmod3.tweak.spawnerdrops.SpawnerDrops;
import choonster.testmod3.util.BlockDumper;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.util.UUID;

@Mod(TestMod3.MODID)
public class TestMod3 {
	public static final String MODID = "testmod3";
	public static final String NAME = "Test Mod 3";

	public static final ItemGroupTestMod3 ITEM_GROUP = new ItemGroupTestMod3();

	@SidedProxy(clientSide = "choonster.testmod3.proxy.CombinedClientProxy", serverSide = "choonster.testmod3.proxy.DedicatedServerProxy")
	public static IProxy proxy;

	public static final SimpleChannel network = ModNetwork.getNetworkChannel();

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	@EventHandler
	public void preInit(final FMLPreInitializationEvent event) {
		FMLLog.bigWarning("Random UUID: {}", UUID.randomUUID().toString());

		ModCapabilities.registerCapabilities();

		ModWorldGen.registerMapGen();
		ModDispenseBehaviors.registerDispenseBehaviors();
		ModLootTables.registerLootTables();

		ModSoundEvents.RegistrationHandler.initialiseSoundEvents();

		proxy.preInit();
	}

	@EventHandler
	public void init(final FMLInitializationEvent event) {
		ModRecipes.registerRecipes();
		ModWorldGen.registerFeatures();
		ModDataFixers.registerDataFixers();

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
	public void serverStopped(final FMLServerStoppedEvent event) {
		SpawnerDrops.serverStopped();
	}
}
