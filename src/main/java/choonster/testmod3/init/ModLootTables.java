package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTableList;

import java.util.HashSet;
import java.util.Set;

import static choonster.testmod3.init.ModLootTables.RegistrationHandler.create;

/**
 * Registers this mod's {@link LootTable}s.
 *
 * @author Choonster
 */
public class ModLootTables {
	public static final ResourceLocation LOOT_TABLE_TEST = create("loot_table_test");


	/**
	 * Register this mod's {@link LootTable}s.
	 */
	public static void registerLootTables() {
		RegistrationHandler.LOOT_TABLES.forEach(LootTableList::register);
	}

	public static class RegistrationHandler {

		/**
		 * Stores the IDs of this mod's {@link LootTable}s.
		 */
		private static final Set<ResourceLocation> LOOT_TABLES = new HashSet<>();

		/**
		 * Create a {@link LootTable} ID.
		 *
		 * @param id The ID of the LootTable without the testmod3: prefix
		 * @return The ID of the LootTable
		 */
		protected static ResourceLocation create(final String id) {
			final ResourceLocation lootTable = new ResourceLocation(TestMod3.MODID, id);
			RegistrationHandler.LOOT_TABLES.add(lootTable);
			return lootTable;
		}
	}
}
