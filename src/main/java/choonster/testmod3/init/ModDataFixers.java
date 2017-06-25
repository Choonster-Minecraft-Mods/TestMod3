package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraftforge.common.util.ModFixs;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Registers this mod's data fixers.
 *
 * @author Choonster
 */
public class ModDataFixers {
	/**
	 * The current data version.
	 */
	private static final int DATA_VERSION = 1;

	private static final ModFixs MOD_FIXES = FMLCommonHandler.instance().getDataFixer().init(TestMod3.MODID, DATA_VERSION);

	/**
	 * Register this mod's data fixers.
	 */
	public static void registerDataFixers() {

	}
}
