package choonster.testmod3.config;

import choonster.testmod3.Logger;
import choonster.testmod3.TestMod3;
import choonster.testmod3.util.ReflectionUtil;
import net.minecraftforge.common.config.Config;
import net.minecraftforge.common.config.ConfigManager;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.config.IConfigElement;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.lang.invoke.MethodHandle;
import java.util.Map;
import java.util.Optional;

@Config(modid = TestMod3.MODID)
public class ModConfig {

	@Config.Comment("This is an example boolean property.")
	public static boolean fooBar = false;

	public static Client client = new Client();

	public static class Client {

		@Config.Comment("This is an example int property.")
		public int baz = -100;

		public HUDPos chunkEnergyHUDPos = new HUDPos(0, 0);

		public static class HUDPos {
			public HUDPos(int x, int y) {
				this.x = x;
				this.y = y;
			}

			@Config.Comment("The x coordinate")
			public int x;

			@Config.Comment("The y coordinate")
			public int y;
		}
	}

	@Mod.EventBusSubscriber
	static class ConfigurationHolder {

		/**
		 * The {@link ConfigManager#CONFIGS} getter.
		 */
		private static final MethodHandle CONFIGS_GETTER = ReflectionUtil.findFieldGetter(ConfigManager.class, "CONFIGS", null);

		/**
		 * The {@link Configuration} instance.
		 */
		private static Configuration configuration;

		/**
		 * Get the {@link Configuration} instance from {@link ConfigManager}.
		 * <p>
		 * TODO: Use a less hackish method of getting the {@link Configuration}/{@link IConfigElement}s when possible.
		 *
		 * @return The Configuration instance
		 */
		static Configuration getConfiguration() {
			if (configuration == null) {
				try {
					final String fileName = TestMod3.MODID + ".cfg";

					@SuppressWarnings("unchecked")
					final Map<String, Configuration> configsMap = (Map<String, Configuration>) CONFIGS_GETTER.invokeExact();

					final Optional<Map.Entry<String, Configuration>> entryOptional = configsMap.entrySet().stream()
							.filter(entry -> fileName.equals(new File(entry.getKey()).getName()))
							.findFirst();

					entryOptional.ifPresent(stringConfigurationEntry -> configuration = stringConfigurationEntry.getValue());
				} catch (Throwable throwable) {
					Logger.error(throwable, "Failed to get Configuration instance");
				}
			}

			return configuration;
		}

		/**
		 * Inject the new values and save to the config file when the config has been changed from the GUI.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent event) {
			if (event.getModID().equals(TestMod3.MODID)) {
				ConfigManager.load(TestMod3.MODID, Config.Type.INSTANCE);
			}
		}
	}
}
