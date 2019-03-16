package choonster.testmod3;

import choonster.testmod3.client.command.ModCommandsClient;
import choonster.testmod3.client.gui.GuiHandler;
import choonster.testmod3.client.init.ModKeyBindings;
import choonster.testmod3.client.renderer.entity.ModRenderers;
import choonster.testmod3.init.*;
import choonster.testmod3.tests.Tests;
import choonster.testmod3.util.BlockDumper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.event.*;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

@Mod(TestMod3.MODID)
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class TestMod3 {
	private static final Logger LOGGER = LogManager.getLogger();

	public static final String MODID = "testmod3";
	public static final String NAME = "Test Mod 3";

	public static final ItemGroupTestMod3 ITEM_GROUP = new ItemGroupTestMod3();

	public static final SimpleChannel network = ModNetwork.getNetworkChannel();

	static {
		FluidRegistry.enableUniversalBucket(); // Must be called before preInit
	}

	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.warn("****************************************");
		LOGGER.warn("Random UUID: {}", UUID.randomUUID().toString());
		LOGGER.warn("****************************************");

		ModCapabilities.registerCapabilities();

		ModWorldGen.registerFeatures();
		ModDispenseBehaviors.registerDispenseBehaviors();
		ModLootTables.registerLootTables();

		ModRecipes.registerRecipes();
		ModDataFixers.registerDataFixers();

		NetworkRegistry.INSTANCE.registerGuiHandler(this, new GuiHandler());

		BlockDumper.dump();
		Tests.runTests();
	}

	@SubscribeEvent
	public static void clientSetup(final FMLClientSetupEvent event) {
		ModRenderers.register();
		ModCommandsClient.registerCommands();
		ModKeyBindings.registerKeyBindings();
	}
}
