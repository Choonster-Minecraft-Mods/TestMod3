package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.storage.loot.predicates.IsChestLoot;
import choonster.testmod3.world.level.storage.loot.predicates.MatchBlockTag;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Registers this mod's {@link LootItemConditionType}s.
 *
 * @author Choonster
 */
public class ModLootConditionTypes {
	public static final LootItemConditionType IS_CHEST_LOOT = register("is_chest_loot", new IsChestLoot.ConditionSerializer());
	public static final LootItemConditionType MATCH_BLOCK_TAG = register("match_block_tag", new MatchBlockTag.ConditionSerializer());

	public static void register() {
		// No-op method to ensure that this class is loaded and its static initialisers are run
	}

	private static LootItemConditionType register(final String name, final Serializer<? extends LootItemCondition> serializer) {
		return Registry.register(Registry.LOOT_CONDITION_TYPE, new ResourceLocation(TestMod3.MODID, name), new LootItemConditionType(serializer));
	}
}
