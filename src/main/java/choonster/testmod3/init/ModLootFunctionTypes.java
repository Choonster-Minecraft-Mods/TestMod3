package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.storage.loot.functions.SetFluidTankContents;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;

/**
 * Registers this mod's {@link LootItemFunctionType}s.
 *
 * @author Choonster
 */
public class ModLootFunctionTypes {
	public static final LootItemFunctionType SET_FLUID_TANK_CONTENTS = register("set_fluid_tank_contents", new SetFluidTankContents.Serializer());

	public static void register() {
		// No-op method to ensure that this class is loaded and its static initialisers are run
	}

	private static LootItemFunctionType register(final String name, final Serializer<? extends LootItemFunction> serializer) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(TestMod3.MODID, name), new LootItemFunctionType(serializer));
	}
}
