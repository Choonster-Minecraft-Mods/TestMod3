package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModLootTables;
import net.minecraft.world.storage.loot.*;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handler for {@link LootTableLoadEvent}.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class LootTableEventHandler {

	/**
	 * When the {@link LootTables#CHESTS_SIMPLE_DUNGEON} {@link LootTable} is loaded, add a new {@link LootPool} with
	 * a single {@link TableLootEntry} that points to {@link ModLootTables#LOOT_TABLE_TEST}.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2781780-chest-loot
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void lootTableLoad(final LootTableLoadEvent event) {
		if (event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON)) {
			event.getTable().addPool(
					LootPool.builder()
							.name(ModLootTables.LOOT_TABLE_TEST.toString())
							.rolls(new RandomValueRange(0, 1))
							.bonusRolls(0, 1)
							.addEntry(
									TableLootEntry.builder(ModLootTables.LOOT_TABLE_TEST)
							)
							.build()
			);
		}
	}
}
