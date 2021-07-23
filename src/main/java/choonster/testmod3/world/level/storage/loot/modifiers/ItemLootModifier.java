package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.Deserializers;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctions;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.function.BiFunction;

/**
 * A global loot modifier that uses an {@link Item} and a list of {@link LootItemFunction}s to generate a single
 * {@link ItemStack}.
 *
 * @author Choonster
 */
public class ItemLootModifier extends LootModifier {
	private final Item item;
	private final LootItemFunction[] functions;
	private final BiFunction<ItemStack, LootContext, ItemStack> combinedFunctions;

	public ItemLootModifier(final LootItemCondition[] conditions, final Item item, final LootItemFunction[] functions) {
		super(conditions);
		this.item = item;
		this.functions = functions;
		combinedFunctions = LootItemFunctions.compose(functions);
	}

	@Override
	protected List<ItemStack> doApply(final List<ItemStack> generatedLoot, final LootContext context) {
		final ItemStack stack = new ItemStack(item);

		combinedFunctions.apply(stack, context);

		generatedLoot.add(stack);

		return generatedLoot;
	}

	public static class Serializer extends GlobalLootModifierSerializer<ItemLootModifier> {
		/**
		 * Gson instance used to serialise/deserialise {@link LootItemFunction}s.
		 */
		private static final Gson LOOT_FUNCTION_GSON = Deserializers.createFunctionSerializer().create();

		@Override
		public ItemLootModifier read(final ResourceLocation location, final JsonObject object, final LootItemCondition[] conditions) {
			final Item item = GsonHelper.getAsItem(object, "name");

			// Can't use JSONUtils.deserializeClass because we don't have a JsonDeserializationContext
			final LootItemFunction[] functions;
			if (object.has("functions")) {
				final JsonArray functionsJsonArray = GsonHelper.getAsJsonArray(object, "functions");
				functions = LOOT_FUNCTION_GSON.fromJson(functionsJsonArray, LootItemFunction[].class);
			} else {
				functions = new LootItemFunction[0];
			}

			return new ItemLootModifier(conditions, item, functions);
		}

		@Override
		public JsonObject write(final ItemLootModifier instance) {
			final JsonObject object = makeConditions(instance.conditions);

			final ResourceLocation registryName = instance.item.getRegistryName();
			if (registryName == null) {
				throw new IllegalArgumentException("Can't serialize unknown item " + instance.item);
			} else {
				object.addProperty("name", registryName.toString());
			}

			if (!ArrayUtils.isEmpty(instance.functions)) {
				object.add("functions", LOOT_FUNCTION_GSON.toJsonTree(instance.functions));
			}

			return object;
		}
	}
}
