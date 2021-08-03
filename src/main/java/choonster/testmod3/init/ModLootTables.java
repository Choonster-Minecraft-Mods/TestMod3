package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static choonster.testmod3.init.ModLootTables.RegistrationHandler.register;

/**
 * Registers this mod's {@link LootTable}s.
 *
 * @author Choonster
 */
public class ModLootTables {
	public static final ResourceLocation LOOT_TABLE_TEST = register("loot_table_test");

	public static void registerLootTables() {
		// No-op method to ensure that this class is loaded and its static initialisers are run
	}

	public static class RegistrationHandler {
		private static final Method REGISTER = ObfuscationReflectionHelper.findMethod(BuiltInLootTables.class, /* register */ "m_78769_", ResourceLocation.class);

		public static ResourceLocation register(final String name) {
			final ResourceLocation id = new ResourceLocation(TestMod3.MODID, name);

			try {
				return (ResourceLocation) REGISTER.invoke(null, id);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Failed to register loot table " + id, e);
			}
		}
	}
}
