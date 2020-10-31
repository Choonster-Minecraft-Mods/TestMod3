package choonster.testmod3;

import choonster.testmod3.config.TestMod3Config;
import choonster.testmod3.init.*;
import choonster.testmod3.tests.Tests;
import choonster.testmod3.util.BlockDumper;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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

	public TestMod3() {
		TestMod3Config.register(ModLoadingContext.get());

		final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

		ModFluids.initialise(modEventBus);
		ModBlocks.initialise(modEventBus);
		ModItems.initialise(modEventBus);
		ModBiomes.initialise(modEventBus);
		ModContainerTypes.initialise(modEventBus);
		ModEffects.initialise(modEventBus);
		ModEntities.initialise(modEventBus);
		ModFeatures.initialise(modEventBus);
		ModPlacements.initialise(modEventBus);
		ModPotions.initialise(modEventBus);
		ModCrafting.Recipes.initialise(modEventBus);
		ModSoundEvents.initialise(modEventBus);
		ModTileEntities.initialise(modEventBus);
		ModTestRegistryEntries.initialise(modEventBus);
	}

	@SubscribeEvent(priority = EventPriority.HIGH) // Ensure this is run before the Biome DeferredRegister in ModBiomes
	public static void registerBiomes(final RegistryEvent.Register<Biome> event) {
		// TODO: This runs before Features are registered, crashing the game
		ModConfiguredFeatures.register();
		ModConfiguredSurfaceBuilders.register();
	}

	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.warn("****************************************");
		LOGGER.warn("Random UUID: {}", UUID.randomUUID().toString());
		LOGGER.warn("****************************************");

		event.enqueueWork(() -> {
			ModCrafting.Ingredients.register();
			ModLootTables.registerLootTables();
			ModLootFunctionTypes.register();

			BlockDumper.dump();
			Tests.runTests();
		});
	}
}
