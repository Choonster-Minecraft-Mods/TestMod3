package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.advancements.criterion.ItemFluidContainerPredicate;
import com.mojang.serialization.Codec;
import net.minecraft.advancements.critereon.ItemSubPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link ItemSubPredicate ItemSubPredicates}.
 *
 * @author Choonster
 */
public class ModItemSubPredicates {
	private static final DeferredRegister<ItemSubPredicate.Type<?>> ITEM_SUB_PREDICATE_TYPES =
			DeferredRegister.create(Registries.ITEM_SUB_PREDICATE_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<ItemSubPredicate.Type<ItemFluidContainerPredicate>> FLUID_CONTAINER = register("fluid_container",
			ItemFluidContainerPredicate.CODEC
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

		ITEM_SUB_PREDICATE_TYPES.register(modEventBus);

		isInitialised = true;
	}

	private static <T extends ItemSubPredicate> RegistryObject<ItemSubPredicate.Type<T>> register(final String name, final Codec<T> codec) {
		return ITEM_SUB_PREDICATE_TYPES.register(name, () -> new ItemSubPredicate.Type<>(codec));
	}
}
