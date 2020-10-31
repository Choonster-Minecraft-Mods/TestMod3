package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.loot.functions.SetFluidTankContents;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/**
 * Registers this mod's {@link LootFunctionType}s.
 *
 * @author Choonster
 */
public class ModLootFunctionTypes {
	public static final LootFunctionType SET_FLUID_TANK_CONTENTS = register("set_fluid_tank_contents", new SetFluidTankContents.Serializer());

	public static void register() {
		// No-op method to ensure that this class is loaded and its static initialisers are run
	}

	private static LootFunctionType register(final String name, final ILootSerializer<? extends ILootFunction> serializer) {
		return Registry.register(Registry.LOOT_FUNCTION_TYPE, new ResourceLocation(TestMod3.MODID, name), new LootFunctionType(serializer));
	}
}
