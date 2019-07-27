package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.TestMod3;
import choonster.testmod3.crafting.ingredient.ConditionalIngredientSerializer;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds an {@link Ingredient} that can be deserialised by {@link ConditionalIngredientSerializer}.
 *
 * @author Choonster
 */
public class ConditionalIngredientBuilder {
	private final Ingredient ingredient;
	private final List<Condition> conditions;

	private ConditionalIngredientBuilder(final Ingredient ingredient) {
		this.ingredient = ingredient;
		conditions = new ArrayList<>();
	}

	/**
	 * Creates a new {@link ConditionalIngredientBuilder}.
	 *
	 * @param items The Ingredient Items to be used when the conditions are met
	 * @return The builder
	 */
	public static ConditionalIngredientBuilder conditionalIngredient(final IItemProvider... items) {
		return conditionalIngredient(Ingredient.fromItems(items));
	}

	/**
	 * Creates a new {@link ConditionalIngredientBuilder}.
	 *
	 * @param ingredient The Ingredient to be used when the conditions are met
	 * @return The builder
	 */
	public static ConditionalIngredientBuilder conditionalIngredient(final Ingredient ingredient) {
		return new ConditionalIngredientBuilder(ingredient);
	}

	/**
	 * Adds a condition without any extra data.
	 *
	 * @param type The condition type
	 * @return This builder
	 */
	public ConditionalIngredientBuilder addCondition(final ResourceLocation type) {
		return addCondition(type, new JsonObject());
	}

	/**
	 * Adds a condition with extra data.
	 *
	 * @param type The condition type
	 * @param data The data
	 * @return This builder
	 */
	public ConditionalIngredientBuilder addCondition(final ResourceLocation type, final JsonObject data) {
		conditions.add(new Condition(type, data));
		return this;
	}

	/**
	 * Validates that the ingredient has at least one condition.
	 */
	private void validate() {
		if (conditions.isEmpty()) {
			final String stacks = Arrays.stream(ingredient.getMatchingStacks())
					.map(ItemStack::toString)
					.collect(Collectors.joining(","));

			throw new IllegalStateException("Conditional ingredient producing [" + stacks + "] has no conditions");
		}
	}

	/**
	 * Builds the final {@link Ingredient}.
	 *
	 * @return The Ingredient
	 */
	public Result build() {
		validate();
		return new Result(ingredient, conditions);
	}

	/**
	 * Represents a condition type and its accompanying data.
	 */
	private static class Condition {
		private final ResourceLocation type;
		private final JsonObject data;

		Condition(final ResourceLocation type, final JsonObject data) {
			this.type = type;
			this.data = data;
		}

		JsonElement serialize() {
			data.addProperty("type", type.toString());
			return data;
		}
	}

	/**
	 * An {@link Ingredient} that serialises into JSON that can be deserialised by {@link ConditionalIngredientSerializer}.
	 * <p>
	 * Note: This is only intended for use during recipe generation, it won't match any items if used in a recipe during gameplay.
	 */
	public static class Result extends Ingredient {
		private final Ingredient ingredient;
		private final List<Condition> conditions;

		private Result(final Ingredient ingredient, final List<Condition> conditions) {
			super(Stream.empty());
			this.ingredient = ingredient;
			this.conditions = conditions;
		}

		@Override
		public JsonElement serialize() {
			final JsonObject rootObject = new JsonObject();
			rootObject.addProperty("type", new ResourceLocation(TestMod3.MODID, "conditional").toString());

			final JsonArray conditionsArray = new JsonArray();
			conditions.forEach(condition -> conditionsArray.add(condition.serialize()));
			rootObject.add("conditions", conditionsArray);

			final JsonElement ingredientObject = ingredient.serialize();
			rootObject.add("ingredient", ingredientObject);

			return rootObject;
		}
	}
}
