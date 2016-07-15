package choonster.testmod3.config;

import choonster.testmod3.TestMod3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
	static final String LANG_PREFIX = TestMod3.MODID + ".config.";

	static Configuration config;

	public static boolean fooBar;
	public static int baz;

	public static void load(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		reloadConfig();

		MinecraftForge.EVENT_BUS.register(Config.class);
	}

	private static void reloadConfig() {
		fooBar = config.getBoolean("fooBar", Configuration.CATEGORY_GENERAL, false, "This is an example boolean property.", LANG_PREFIX + "fooBar");
		baz = config.getInt("baz", Configuration.CATEGORY_CLIENT, -100, -Integer.MAX_VALUE, Integer.MAX_VALUE, "This is an example int property.", LANG_PREFIX + "baz");

		if (config.hasChanged()) {
			config.save();
		}
	}

	@SubscribeEvent
	public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.getModID().equals(TestMod3.MODID)) {
			reloadConfig();
		}
	}
}
