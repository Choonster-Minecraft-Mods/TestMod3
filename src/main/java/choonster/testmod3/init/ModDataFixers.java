package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.datafix.BlockFlatteningDefinitions;
import choonster.testmod3.datafix.ItemFlatteningDefinitions;
import net.minecraft.util.datafix.FixTypes;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

/**
 * Registers this mod's data fixers.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataFixers {
	/**
	 * The current data version.
	 */
	public static final int DATA_VERSION = 103;

	/**
	 * Register this mod's data fixers.
	 */
	@SubscribeEvent
	public static void registerDataFixers(final FMLCommonSetupEvent event) {
		final ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(TestMod3.MODID, DATA_VERSION);

		modFixs.registerFix(FixTypes.CHUNK, BlockFlatteningDefinitions.createBlockFlattening());
		modFixs.registerFix(FixTypes.ITEM_INSTANCE, ItemFlatteningDefinitions.createItemFlattening());
	}
}
