package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.world.item.crafting.ingredient.ConditionalIngredientCodec;
import choonster.testmod3.world.item.crafting.ingredient.IngredientUtil;
import com.google.gson.JsonElement;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds an {@link Ingredient} that can be deserialised by {@link ConditionalIngredientCodec}.
 *
 * @author Choonster
 */
public class ConditionalIngredientBuilder {
	private final List<ICondition> conditions = new ArrayList<>();
	private final Ingredient ingredient;

	private ConditionalIngredientBuilder(final Ingredient ingredient) {
		this.ingredient = ingredient;
	}

	/**
	 * Creates a new {@link ConditionalIngredientBuilder}.
	 *
	 * @param items The Ingredient Items to be used when the conditions are met
	 * @return The builder
	 */
	public static ConditionalIngredientBuilder conditionalIngredient(final ItemLike... items) {
		return conditionalIngredient(Ingredient.of(items));
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
	 * @param condition The condition
	 * @return This builder
	 */
	public ConditionalIngredientBuilder addCondition(final ICondition condition) {
		conditions.add(condition);
		return this;
	}

	/**
	 * Validates that the ingredient has at least one condition.
	 */
	private void validate() {
		if (conditions.isEmpty()) {
			final var stacks = Arrays.stream(ingredient.getItems())
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
		return new Result(conditions, ingredient);
	}

	/**
	 * An {@link Ingredient} that serialises into JSON that can be deserialised by ConditionalIngredientSerializer.
	 * <p>
	 * Note: This is only intended for use during recipe generation, it won't match any items if used in a recipe during gameplay.
	 */
	public static class Result extends Ingredient {
		private final List<ICondition> conditions;
		private final Ingredient ingredient;

		private Result(final List<ICondition> conditions, final Ingredient ingredient) {
			super(Stream.empty());
			this.conditions = conditions;
			this.ingredient = ingredient;
		}

		@Override
		public JsonElement toJson(final boolean allowEmpty) {
			final var data = new ConditionalIngredientCodec.Data(conditions, ingredient);

			return IngredientUtil.toJson(ConditionalIngredientCodec.DATA_CODEC, data);
		}
	}
}
