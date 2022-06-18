package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

/**
 * Registers this mod's {@link DataProvider}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataProviders {
	@SubscribeEvent
	public static void registerDataProviders(final GatherDataEvent event) {
		final DataGenerator dataGenerator = event.getGenerator();
		final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		dataGenerator.addProvider(event.includeClient(), new TestMod3LanguageProvider(dataGenerator));

		final TestMod3ItemModelProvider itemModelProvider = new TestMod3ItemModelProvider(dataGenerator, existingFileHelper);
		dataGenerator.addProvider(event.includeClient(), itemModelProvider);

		// Let blockstate provider see generated item models by passing its existing file helper
		dataGenerator.addProvider(event.includeClient(), new TestMod3BlockStateProvider(dataGenerator, itemModelProvider.existingFileHelper));

		dataGenerator.addProvider(event.includeServer(), new TestMod3RecipeProvider(dataGenerator));
		dataGenerator.addProvider(event.includeServer(), new TestMod3LootTableProvider(dataGenerator));
		dataGenerator.addProvider(event.includeServer(), new TestMod3WorldgenRegistryDumpReport(dataGenerator));
		dataGenerator.addProvider(event.includeServer(), new TestMod3LootModifierProvider(dataGenerator));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeModifierProvider(dataGenerator, existingFileHelper));

		final TestMod3BlockTagsProvider blockTagsProvider = new TestMod3BlockTagsProvider(dataGenerator, existingFileHelper);
		dataGenerator.addProvider(event.includeServer(), blockTagsProvider);
		dataGenerator.addProvider(event.includeServer(), new TestMod3ItemTagsProvider(dataGenerator, blockTagsProvider, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3BiomeTagsProvider(dataGenerator, existingFileHelper));
		dataGenerator.addProvider(event.includeServer(), new TestMod3FluidTagsProvider(dataGenerator, existingFileHelper));
	}
}
