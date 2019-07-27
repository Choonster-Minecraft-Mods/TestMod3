package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.TestMod3RecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.IDataProvider;
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

		if (event.includeServer()) {
			dataGenerator.addProvider(new TestMod3RecipeProvider(dataGenerator));
		}
	}
}
