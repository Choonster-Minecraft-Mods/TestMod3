package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

/**
 * A global loot modifier that generates loot from another loot table.
 *
 * @author Choonster
 */
public class LootTableLootModifier extends LootModifier {
	private final ResourceLocation lootTableID;

	public LootTableLootModifier(final LootItemCondition[] conditions, final ResourceLocation lootTableID) {
		super(conditions);
		this.lootTableID = lootTableID;
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(final ObjectArrayList<ItemStack> generatedLoot, final LootContext context) {
		final LootTable lootTable = context.getLootTable(lootTableID);

		// Generate additional loot without applying loot modifiers, otherwise each modifier would run multiple times
		// for the same loot generation.
		lootTable.getRandomItemsRaw(context, generatedLoot::add);

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<LootTableLootModifier> {
		@Override
		public LootTableLootModifier read(final ResourceLocation location, final JsonObject object, final LootItemCondition[] lootConditions) {
			final ResourceLocation lootTableID = new ResourceLocation(GsonHelper.getAsString(object, "loot_table"));
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
