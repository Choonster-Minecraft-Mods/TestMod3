package choonster.testmod3.event;

import choonster.testmod3.init.ModLootTables;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.LootCondition;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

/**
 * Handler for {@link LootTableLoadEvent}.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber
public class LootTableEventHandler {

	/**
	 * When the {@link LootTableList#CHESTS_SIMPLE_DUNGEON} {@link LootTable} is loaded, add a new {@link LootPool} with
	 * a single {@link LootEntryTable} that points to {@link ModLootTables#LOOT_TABLE_TEST}.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2781780-chest-loot
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void lootTableLoad(final LootTableLoadEvent event) {
		if (event.getName().equals(LootTableList.CHESTS_SIMPLE_DUNGEON)) {
			final String name = ModLootTables.LOOT_TABLE_TEST.toString();

			final LootEntry entry = new LootEntryTable(ModLootTables.LOOT_TABLE_TEST, 1, 0,
					new LootCondition[0], name);

			final RandomValueRange rolls = new RandomValueRange(0, 1);

			final LootPool pool = new LootPool(new LootEntry[]{entry}, new LootCondition[0], rolls, rolls, name);

			event.getTable().addPool(pool);
		}
	}
}
