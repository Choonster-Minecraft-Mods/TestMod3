package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.storage.loot.functions.SetFluidTankContents;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link LootItemFunctionType}s.
 *
 * @author Choonster
 */
public class ModLootFunctionTypes {
	private static final DeferredRegister<LootItemFunctionType> LOOT_ITEM_FUNCTION_TYPES =
			DeferredRegister.create(Registries.LOOT_FUNCTION_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<LootItemFunctionType> SET_FLUID_TANK_CONTENTS = register("set_fluid_tank_contents",
			SetFluidTankContents.CODEC
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

		LOOT_ITEM_FUNCTION_TYPES.register(modEventBus);

		isInitialised = true;
	}

	private static RegistryObject<LootItemFunctionType> register(final String name, final Codec<? extends LootItemFunction> codec) {
		return LOOT_ITEM_FUNCTION_TYPES.register(name, () -> new LootItemFunctionType(codec));
	}
}
