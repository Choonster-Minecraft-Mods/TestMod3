package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link LootTable}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModLootTables {
	public static final ResourceLocation LOOT_TABLE_TEST = new ResourceLocation(TestMod3.MODID, " loot_table_test");
}
