package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.loot.conditions.IsChestLoot;
import choonster.testmod3.loot.conditions.MatchBlockTag;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;

/**
 * Registers this mod's {@link LootConditionType}s.
 *
 * @author Choonster
 */
public class ModLootConditionTypes {
	public static final LootConditionType IS_CHEST_LOOT = register("is_chest_loot", new IsChestLoot.Serializer());
	public static final LootConditionType MATCH_BLOCK_TAG = register("match_block_tag", new MatchBlockTag.Serializer());

	public static void register() {
		// No-op method to ensure that this class is loaded and its static initialisers are run
	}

	private static LootConditionType register(final String name, final ILootSerializer<? extends ILootCondition> serializer) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(TestMod3.MODID, name), new LootConditionType(serializer));
	}
}
