package choonster.testmod3.init;

/**
 * Registers this mod's data fixers.
 *
 * @author Choonster
 */
// TODO: Update if/when mod data fixers are re-implemented.
//@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModDataFixers {
 	/**
 	* The current data version.
 	*/
	public static final int DATA_VERSION = 103;
	
	/**
	 * Register this mod's data fixers.
 	*//*
	@SubscribeEvent
	public static void registerDataFixers(final FMLCommonSetupEvent event) {
		final ModFixs modFixs = FMLCommonHandler.instance().getDataFixer().init(TestMod3.MODID, DATA_VERSION);

		modFixs.registerFix(FixTypes.CHUNK, BlockFlatteningDefinitions.createBlockFlattening());
		modFixs.registerFix(FixTypes.ITEM_INSTANCE, ItemFlatteningDefinitions.createItemFlattening());
	}
*/
}
