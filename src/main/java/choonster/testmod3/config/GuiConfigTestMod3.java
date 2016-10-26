package choonster.testmod3.config;


import choonster.testmod3.TestMod3;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.IConfigElement;

import java.util.List;

public class GuiConfigTestMod3 extends GuiConfig {
	private static final String LANG_PREFIX = TestMod3.MODID + ".category.";

	public GuiConfigTestMod3(GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(), TestMod3.MODID, false, false, I18n.format(TestMod3.MODID + ".config.title"));
	}

	private static List<IConfigElement> getConfigElements() {
		final Configuration configuration = ModConfig.ConfigurationHolder.getConfiguration();

		final ConfigCategory topLevelCategory = configuration.getCategory(Configuration.CATEGORY_GENERAL);
		topLevelCategory.getChildren().forEach(configCategory -> configCategory.setLanguageKey(LANG_PREFIX + configCategory.getName()));

		return new ConfigElement(topLevelCategory).getChildElements();
	}
}
