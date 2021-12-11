package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.registry.TestRegistryEntry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link TestRegistryEntry TestRegistryEntries}.
 *
 * @author Choonster
 */
public class ModTestRegistryEntries {
	private static final DeferredRegister<TestRegistryEntry> TEST_REGISTRY_ENTRIES = DeferredRegister.create(TestRegistryEntry.class, TestMod3.MODID);

	public static final Supplier<IForgeRegistry<TestRegistryEntry>> REGISTRY = TEST_REGISTRY_ENTRIES.makeRegistry("test_registry_entry", RegistryBuilder::new);

	private static boolean isInitialised = false;

	public static final RegistryObject<TestRegistryEntry> EXAMPLE_1 = TEST_REGISTRY_ENTRIES.register("example_1",
			TestRegistryEntry::new
	);

	public static final RegistryObject<TestRegistryEntry> EXAMPLE_2 = TEST_REGISTRY_ENTRIES.register("example_2",
			TestRegistryEntry::new
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		TEST_REGISTRY_ENTRIES.register(modEventBus);

		isInitialised = true;
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class EventHandler {
		private static final Logger LOGGER = LogManager.getLogger();

		@SubscribeEvent
		public static void commonSetup(final FMLCommonSetupEvent event) {
			LOGGER.info("TestRegistryEntry 1: {}", EXAMPLE_1.get());
			LOGGER.info("TestRegistryEntry 2: {}", EXAMPLE_2.get());
		}
	}
}
