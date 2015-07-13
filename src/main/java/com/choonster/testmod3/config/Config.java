package com.choonster.testmod3.config;

import com.choonster.testmod3.TestMod3;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class Config {
	public static final String CATEGORY_BIOME = "biomes";

	public static Configuration config;

	public static int desertBiomeID;

	public static void load(FMLPreInitializationEvent event) {
		config = new Configuration(event.getSuggestedConfigurationFile());
		reloadConfig();

		FMLCommonHandler.instance().bus().register(new Config());
	}

	public static void reloadConfig() {
		desertBiomeID = getBiomeID("desert", 50, "Desert Test");

		if (config.hasChanged()) {
			config.save();
		}
	}

	private static int getBiomeID(String name, int defaultValue, String comment) {
		Property property = config.get(CATEGORY_BIOME, name, defaultValue, comment, 40, 255);
		property.setRequiresMcRestart(true);
		return property.getInt();
	}

	@SubscribeEvent
	public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equals(TestMod3.MODID)) {
			reloadConfig();
		}
	}
}
