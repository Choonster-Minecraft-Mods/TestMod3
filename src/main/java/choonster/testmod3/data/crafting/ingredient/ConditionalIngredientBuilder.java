package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.world.item.crafting.ingredient.ConditionalIngredientSerializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Builds an {@link Ingredient} that can be deserialised by {@link ConditionalIngredientSerializer}.
 *
 * @author Choonster
 */
public class ConditionalIngredientBuilder {
	@Nullable
	private ICondition condition = null;
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
	 * Sets the condition for the ingredient.
	 *
	 * @param condition The condition
	 * @return This builder
	 */
	public ConditionalIngredientBuilder condition(final ICondition condition) {
		if (this.condition != null) {
			throw new IllegalStateException("Attempted to override condition");
		}

		this.condition = condition;
		return this;
	}

	/**
	 * Builds the final {@link Ingredient}.
	 *
	 * @return The Ingredient
	 */
	public Result build() {
		if (condition == null) {
			final var stacks = Arrays.stream(ingredient.getItems())
					.map(ItemStack::toString)
					.collect(Collectors.joining(","));

			throw new IllegalStateException("Conditional ingredient producing [" + stacks + "] has no conditions");
		}

		return new Result(condition, ingredient);
	}

	/**
	 * An {@link Ingredient} that serialises into JSON that can be deserialised by ConditionalIngredientSerializer.
	 * <p>
	 * Note: This is only intended for use during recipe generation, it won't match any items if used in a recipe during gameplay.
	 */
	public static class Result extends AbstractIngredient {
		private final ICondition condition;
		private final Ingredient ingredient;

		private Result(final ICondition condition, final Ingredient ingredient) {
			super(Stream.empty());
			this.condition = condition;
			this.ingredient = ingredient;
		}

		@Override
		public boolean isSimple() {
			return false;
		}

		@Override
		public IIngredientSerializer<? extends Ingredient> serializer() {
			return ModCrafting.Ingredients.CONDITIONAL.get();
		}

		@Override
		public JsonElement toJson(final boolean allowEmpty) {
			final var output = (JsonObject) ModJsonUtil.toJson(ConditionalIngredientSerializer.CODEC, ingredient);

			// Manually add the type and condition to the output
			output.addProperty("type", ModCrafting.Ingredients.CONDITIONAL.getId().toString());
			output.add(ICondition.DEFAULT_FIELD, ModJsonUtil.toJson(ICondition.CODEC, condition));

			return output;
		}
	}
}
