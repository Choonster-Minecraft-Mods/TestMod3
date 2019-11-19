package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.TestMod3BlockStateProvider;
import choonster.testmod3.data.TestMod3ItemModelProvider;
import choonster.testmod3.data.TestMod3LootTableProvider;
import choonster.testmod3.data.TestMod3RecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
import net.minecraftforge.client.model.generators.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.GatherDataEvent;

/**
 * Registers this mod's {@link IDataProvider}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataProviders {
	@SubscribeEvent
	public static void registerDataProviders(final GatherDataEvent event) {
		final DataGenerator dataGenerator = event.getGenerator();
		final ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

		if (event.includeClient()) {
			dataGenerator.addProvider(new TestMod3BlockStateProvider(dataGenerator, existingFileHelper));
			dataGenerator.addProvider(new TestMod3ItemModelProvider(dataGenerator, existingFileHelper));
		}

		if (event.includeServer()) {
			dataGenerator.addProvider(new TestMod3RecipeProvider(dataGenerator));
			dataGenerator.addProvider(new TestMod3LootTableProvider(dataGenerator));
		}
	}
}
