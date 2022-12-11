package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.*;
import net.minecraft.data.DataProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link DataProvider}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataProviders {
	@SubscribeEvent
	public static void registerDataProviders(final GatherDataEvent event) {
		final var dataGenerator = event.getGenerator();
		final var output = dataGenerator.getPackOutput();
		final var existingFileHelper = event.getExistingFileHelper();
		final var lookupProvider = event.getLookupProvider();

		dataGenerator.addProvider(event.includeClient(), new TestMod3LanguageProvider(dataGenerator));

		final var itemModelProvider = new TestMod3ItemModelProvider(dataGenerator, existingFileHelper);
		dataGenerator.addProvider(event.includeClient(), itemModelProvider);

		// Let blockstate provider see generated item models by passing its existing file helper
		dataGenerator.addProvider(event.includeClient(), new TestMod3BlockStateProvider(dataGenerator, itemModelProvider.existingFileHelper));

		dataGenerator.addProvider(event.includeServer(), new TestMod3RecipeProvider(output));
		dataGenerator.addProvider(event.includeServer(), TestMod3LootTableProvider.create(output));
//		dataGenerator.addProvider(event.includeServer(), new TestMod3WorldgenRegistryDumpReport(dataGenerator));
		dataGenerator.addProvider(event.includeServer(), new TestMod3LootModifierProvider(dataGenerator));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeModifierProvider(dataGenerator, existingFileHelper, lookupProvider));

		final var blockTagsProvider = new TestMod3BlockTagsProvider(output, lookupProvider, existingFileHelper);
		dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
		dataGenerator.addProvider(event.includeServer(), new TestMod3ItemTagsProvider(output, lookupProvider, blockTagsProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeTagsProvider(output, lookupProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3FluidTagsProvider(output, lookupProvider, existingFileHelper));
	}
}
