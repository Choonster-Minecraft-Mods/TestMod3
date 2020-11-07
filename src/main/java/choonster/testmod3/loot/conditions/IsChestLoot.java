package choonster.testmod3.loot.conditions;

import choonster.testmod3.init.ModLootConditionTypes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootParameters;
import net.minecraft.loot.conditions.ILootCondition;

/**
 * A condition that makes a best-effort attempt to determine if the loot is being generated by a chest.
 *
 * @author Choonster
 */
public class IsChestLoot implements ILootCondition {
	private static final IsChestLoot INSTANCE = new IsChestLoot();

	private IsChestLoot() {
	}

	@Override
	public LootConditionType func_230419_b_() {
		return ModLootConditionTypes.IS_CHEST_LOOT;
	}

	@Override
	public boolean test(final LootContext lootContext) {
		// TileEntityLockableLoot#fillWithLoot always provides the ORIGIN parameter, sometimes provides THIS_ENTITY and
		// never provides any other parameter.

		return lootContext.has(LootParameters./* ORIGIN */field_237457_g_) &&
				!lootContext.has(LootParameters.DAMAGE_SOURCE) &&
				!lootContext.has(LootParameters.KILLER_ENTITY) &&
				!lootContext.has(LootParameters.DIRECT_KILLER_ENTITY) &&
				!lootContext.has(LootParameters.BLOCK_STATE) &&
				!lootContext.has(LootParameters.BLOCK_ENTITY) &&
				!lootContext.has(LootParameters.TOOL) &&
				!lootContext.has(LootParameters.EXPLOSION_RADIUS);
	}

	public static ILootCondition.IBuilder builder() {
		return () -> INSTANCE;
	}

	public static class Serializer implements ILootSerializer<IsChestLoot> {
		@Override
		public void serialize(final JsonObject object, final IsChestLoot instance, final JsonSerializationContext context) {
			// No-op
		}

		@Override
		public IsChestLoot deserialize(final JsonObject object, final JsonDeserializationContext context) {
			return IsChestLoot.INSTANCE;
		}
	}
}
