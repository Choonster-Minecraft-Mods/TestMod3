package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.registry.DeferredVanillaRegister;
import choonster.testmod3.registry.VanillaRegistryObject;
import choonster.testmod3.world.level.storage.loot.functions.SetFluidTankContents;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link LootItemFunctionType}s.
 *
 * @author Choonster
 */
public class ModLootFunctionTypes {
	private static final DeferredVanillaRegister<LootItemFunctionType> LOOT_ITEM_FUNCTION_TYPES = DeferredVanillaRegister.create(Registry.LOOT_FUNCTION_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final VanillaRegistryObject<LootItemFunctionType> SET_FLUID_TANK_CONTENTS = register("set_fluid_tank_contents",
			SetFluidTankContents.Serializer::new
	);

	/**
	 * Registers the {@link DeferredVanillaRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		LOOT_ITEM_FUNCTION_TYPES.register(modEventBus);

		isInitialised = true;
	}

	private static VanillaRegistryObject<LootItemFunctionType> register(final String name, final Supplier<Serializer<? extends LootItemFunction>> serializerFactory) {
		return LOOT_ITEM_FUNCTION_TYPES.register(name, () -> new LootItemFunctionType(serializerFactory.get()));
	}
}
