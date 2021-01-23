package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.inventory.container.ModChestContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link ContainerType}s.
 *
 * @author Choonster
 */
public class ModContainerTypes {
	private static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<ContainerType<ModChestContainer>> CHEST = CONTAINER_TYPES.register("chest",
			() -> new ContainerType<>(new ModChestContainer.Factory())
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

		CONTAINER_TYPES.register(modEventBus);

		isInitialised = true;
	}
}
