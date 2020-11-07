package choonster.testmod3.loot.modifiers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.loot.LootFunction;
import net.minecraft.loot.LootSerializers;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.LootFunctionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;
import org.apache.commons.lang3.ArrayUtils;

import java.util.List;
import java.util.function.BiFunction;

/**
 * A global loot modifier that uses an {@link Item} and a list of {@link LootFunction}s to generate a single
 * {@link ItemStack}.
 *
 * @author Choonster
 */
public class ItemLootModifier extends LootModifier {
	private final Item item;
	private final ILootFunction[] functions;
	private final BiFunction<ItemStack, LootContext, ItemStack> combinedFunctions;

	public ItemLootModifier(final ILootCondition[] conditions, final Item item, final ILootFunction[] functions) {
		super(conditions);
		this.item = item;
		this.functions = functions;
		combinedFunctions = LootFunctionManager.combine(functions);
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
		 * Gson instance used to serialise/deserialise {@link ILootFunction}s.
		 */
		private static final Gson LOOT_FUNCTION_GSON = LootSerializers.func_237387_b_().create();

		@Override
		public ItemLootModifier read(final ResourceLocation location, final JsonObject object, final ILootCondition[] conditions) {
			final Item item = JSONUtils.getItem(object, "name");

			// Can't use JSONUtils.deserializeClass because we don't have a JsonDeserializationContext
			final ILootFunction[] functions;
			if (object.has("functions")) {
				final JsonArray functionsJsonArray = JSONUtils.getJsonArray(object, "functions");
				functions = LOOT_FUNCTION_GSON.fromJson(functionsJsonArray, ILootFunction[].class);
			} else {
				functions = new ILootFunction[0];
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
