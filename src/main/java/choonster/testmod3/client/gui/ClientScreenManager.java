package choonster.testmod3.client.gui;

import choonster.testmod3.init.ModClientScreenTypes;
import com.mojang.logging.LogUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * Handles this mod's client-side screen factories
 *
 * @author Choonster
 */
public class ClientScreenManager {
	private static final Logger LOGGER = LogUtils.getLogger();

	private static final Map<ResourceLocation, IScreenConstructor<?, ?>> CONSTRUCTORS = new HashMap<>();

	@SuppressWarnings("unchecked")
	public static <T> void openScreen(final ClientScreenType<T> clientScreenType, final T extraData) {
		final var id = Objects.requireNonNull(ModClientScreenTypes.REGISTRY.get().getKey(clientScreenType));
		getScreenConstructor(id).ifPresent(f -> createAndOpenScreen(clientScreenType, extraData, (IScreenConstructor<T, ?>) f));
	}

	public static Optional<IScreenConstructor<?, ?>> getScreenConstructor(final ResourceLocation id) {
		final var constructor = CONSTRUCTORS.get(id);

		if (constructor == null) {
			LOGGER.warn("Failed to create screen for id: {}", id);
			return Optional.empty();
		}

		return Optional.of(constructor);
	}

	public static <T, S extends Screen> void registerScreenConstructor(
			final RegistryObject<ClientScreenType<T>> clientScreenType,
			final IScreenConstructor<T, S> constructor
	) {
		final var id = clientScreenType.getId();
		final var oldConstructor = CONSTRUCTORS.put(id, constructor);

		if (oldConstructor != null) {
			throw new IllegalStateException("Duplicate registration for " + id);
		}
	}

	private static <T, S extends Screen> void createAndOpenScreen(
			final ClientScreenType<T> clientScreenType,
			final T additionalData,
			final IScreenConstructor<T, S> screenConstructor
	) {
		final var screen = screenConstructor.create(clientScreenType, additionalData);
		Minecraft.getInstance().setScreen(screen);
	}

	@FunctionalInterface
	public interface IScreenConstructor<T, S extends Screen> {
		S create(ClientScreenType<T> clientScreenType, T additionalData);
	}
}
