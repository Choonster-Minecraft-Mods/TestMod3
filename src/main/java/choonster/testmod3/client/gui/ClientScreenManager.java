package choonster.testmod3.client.gui;

import choonster.testmod3.TestMod3;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import org.slf4j.Logger;

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
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<ResourceLocation, IScreenConstructor<?>> CONSTRUCTORS = new HashMap<>();

	public static void openScreen(final ResourceLocation id, final FriendlyByteBuf additionalData) {
		getScreenConstructor(id).ifPresent(f -> f.createAndOpenScreen(id, additionalData, Minecraft.getInstance()));
	}

	public static <T extends AbstractContainerMenu> Optional<IScreenConstructor<?>> getScreenConstructor(final ResourceLocation id) {
		final IScreenConstructor<?> constructor = CONSTRUCTORS.get(id);

		if (constructor == null) {
			LOGGER.warn("Failed to create screen for id: {}", id);
			return Optional.empty();
		}

		return Optional.of(constructor);
	}

	public static <S extends Screen> void registerScreenConstructor(final ResourceLocation id, final IScreenConstructor<S> constructor) {
		final IScreenConstructor<?> oldConstructor = CONSTRUCTORS.put(id, constructor);

		if (oldConstructor != null) {
			throw new IllegalStateException("Duplicate registration for " + id);
		}
	}

	@FunctionalInterface
	public interface IScreenConstructor<S extends Screen> {
		default void createAndOpenScreen(final ResourceLocation id, final FriendlyByteBuf additionalData, final Minecraft mc) {
			final S screen = create(id, additionalData);
			mc.setScreen(screen);
		}

		S create(ResourceLocation id, FriendlyByteBuf additionalData);
	}
}
