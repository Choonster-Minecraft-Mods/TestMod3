package choonster.testmod3;

import choonster.testmod3.config.TestMod3Config;
import choonster.testmod3.init.*;
import choonster.testmod3.init.levelgen.ModBiomeModifierSerializers;
import choonster.testmod3.init.levelgen.ModFeatures;
import choonster.testmod3.init.levelgen.ModPlacementModifierTypes;
import choonster.testmod3.util.BlockDumper;
import com.mojang.logging.LogUtils;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.network.SimpleChannel;
import org.slf4j.Logger;

import java.util.UUID;

@Mod(TestMod3.MODID)
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class TestMod3 {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final String MODID = "testmod3";

	public static final SimpleChannel network = ModNetwork.getNetworkChannel();

	public TestMod3(final FMLJavaModLoadingContext context) {
		TestMod3Config.register(context);

		final var modEventBus = context.getModBusGroup();

		ModFluids.initialise(modEventBus);
		ModBlocks.initialise(modEventBus);
		ModItems.initialise(modEventBus);
		ModMenuTypes.initialise(modEventBus);
		ModMobEffects.initialise(modEventBus);
		ModEntities.initialise(modEventBus);
		ModFeatures.initialise(modEventBus);
		ModLootModifierSerializers.initialise(modEventBus);
		ModPotions.initialise(modEventBus);
		ModCrafting.Ingredients.initialise(modEventBus);
		ModCrafting.Recipes.initialise(modEventBus);
		ModSoundEvents.initialise(modEventBus);
		ModBlockEntities.initialise(modEventBus);
		ModTestRegistryEntries.initialise(modEventBus);
		ModPlacementModifierTypes.initialise(modEventBus);
		ModLootItemConditions.initialise(modEventBus);
		ModLootItemFunctions.initialise(modEventBus);
		ModArgumentTypes.initialise(modEventBus);
		ModBiomeModifierSerializers.initialise(modEventBus);
		ModCreativeTabs.initialise(modEventBus);
		ModDataComponents.initialise(modEventBus);
		ModClientScreenTypes.initialise(modEventBus);
		ModDataComponentPredicates.initialise(modEventBus);
	}

	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent event) {
		LOGGER.warn("****************************************");
		LOGGER.warn("Random UUID: {}", UUID.randomUUID());
		LOGGER.warn("****************************************");

		event.enqueueWork(() -> {
			BlockDumper.dump();
		});
	}
}
