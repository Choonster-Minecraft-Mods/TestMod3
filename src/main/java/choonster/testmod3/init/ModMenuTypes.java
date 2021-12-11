package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.inventory.menu.ModChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link MenuType}s.
 *
 * @author Choonster
 */
public class ModMenuTypes {
	private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<MenuType<ModChestMenu>> CHEST = MENU_TYPES.register("chest",
			() -> new MenuType<>(new ModChestMenu.Factory())
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		MENU_TYPES.register(modEventBus);

		isInitialised = true;
	}
}
