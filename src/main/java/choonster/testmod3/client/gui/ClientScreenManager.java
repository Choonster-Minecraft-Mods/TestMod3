package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Handles this mod's client-side GUI factories
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ClientScreenManager {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final Map<ResourceLocation, IScreenFactory<?>> FACTORIES = new HashMap<>();

	public static void openScreen(final ResourceLocation id, final PacketBuffer additionalData) {
		getScreenFactory(id).ifPresent(f -> f.createAndOpenScreen(id, additionalData, Minecraft.getInstance()));
	}

	public static <T extends Container> Optional<IScreenFactory<?>> getScreenFactory(final ResourceLocation id) {
		final IScreenFactory<?> factory = FACTORIES.get(id);

		if (factory == null) {
			LOGGER.warn("Failed to create screen for id: {}", id);
			return Optional.empty();
		}

		return Optional.of(factory);
	}

	public static <S extends Screen> void registerScreenFactory(final ResourceLocation id, final IScreenFactory<S> factory) {
		final IScreenFactory<?> oldFactory = FACTORIES.put(id, factory);

		if (oldFactory != null) {
			throw new IllegalStateException("Duplicate registration for " + id);
		}
	}

	@FunctionalInterface
	public interface IScreenFactory<S extends Screen> {
		default void createAndOpenScreen(final ResourceLocation id, final PacketBuffer additionalData, final Minecraft mc) {
			final S screen = create(id, additionalData);
			mc.setScreen(screen);
		}

		S create(ResourceLocation id, PacketBuffer additionalData);
	}
}
