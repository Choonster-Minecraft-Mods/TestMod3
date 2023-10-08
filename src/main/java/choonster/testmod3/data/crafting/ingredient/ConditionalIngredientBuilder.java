package choonster.testmod3.data.crafting.ingredient;

import choonster.testmod3.world.item.crafting.ingredient.ConditionalIngredient;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.conditions.ICondition;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * Builder for {@link ConditionalIngredient}.
 *
 * @author Choonster
 */
public class ConditionalIngredientBuilder {
	@Nullable
	private ICondition condition;
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
	public ConditionalIngredient build() {
		if (condition == null) {
			final var stacks = Arrays.stream(ingredient.getItems())
					.map(ItemStack::toString)
					.collect(Collectors.joining(","));

			throw new IllegalStateException("Conditional ingredient producing [" + stacks + "] has no conditions");
		}

		return new ConditionalIngredient(condition, ingredient);
	}
}
