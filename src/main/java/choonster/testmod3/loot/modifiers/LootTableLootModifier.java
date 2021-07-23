package choonster.testmod3.loot.modifiers;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import java.util.List;

/**
 * A global loot modifier that generates loot from another loot table.
 *
 * @author Choonster
 */
public class LootTableLootModifier extends LootModifier {
	private final ResourceLocation lootTableID;

	public LootTableLootModifier(final ILootCondition[] conditions, final ResourceLocation lootTableID) {
		super(conditions);
		this.lootTableID = lootTableID;
	}

	@Override
	protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
		final LootTable lootTable = context.getLootTable(lootTableID);

		// Generate additional loot without applying loot modifiers, otherwise each modifier would run multiple times
		// for the same loot generation.
		lootTable.getRandomItemsRaw(context, generatedLoot::add);

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<LootTableLootModifier> {
		@Override
		public LootTableLootModifier read(final ResourceLocation location, final JsonObject object, final ILootCondition[] lootConditions) {
			final ResourceLocation lootTableID = new ResourceLocation(JSONUtils.getAsString(object, "loot_table"));
			return new LootTableLootModifier(lootConditions, lootTableID);
		}

		@Override
		public JsonObject write(final LootTableLootModifier instance) {
			final JsonObject object = makeConditions(instance.conditions);
			object.addProperty("loot_table", instance.lootTableID.toString());
			return object;
		}
	}
}
