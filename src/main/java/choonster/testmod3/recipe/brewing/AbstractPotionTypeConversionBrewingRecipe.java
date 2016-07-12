package choonster.testmod3.recipe.brewing;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.potion.PotionUtils;
import net.minecraftforge.common.brewing.AbstractBrewingRecipe;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nullable;

/**
 * A brewing recipe that imitates vanilla's {@link PotionType} conversion ({@link PotionHelper#registerPotionTypeConversion}).
 * <p>
 * Adapted from {@link AbstractBrewingRecipe} and {@link PotionHelper#doReaction}
 *
 * @author Choonster
 */
public abstract class AbstractPotionTypeConversionBrewingRecipe<T> implements IBrewingRecipe {
	/**
	 * The input {@link PotionType}.
	 */
	private final PotionType input;

	/**
	 * The ingredient.
	 */
	private final T ingredient;

	/**
	 * The output {@link PotionType}.
	 */
	private final PotionType output;

	public AbstractPotionTypeConversionBrewingRecipe(PotionType input, T ingredient, PotionType output) {
		this.input = input;
		this.ingredient = ingredient;
		this.output = output;
	}

	/**
	 * Returns true is the passed ItemStack is an input for this recipe. "Input"
	 * being the item that goes in one of the three bottom slots of the brewing
	 * stand (e.g: water bottle)
	 *
	 * @param input The input
	 */
	@Override
	public boolean isInput(ItemStack input) {
		return PotionUtils.getPotionFromItem(input) == getInput();
	}

	/**
	 * Returns the output when the passed input is brewed with the passed
	 * ingredient. Null if invalid input or ingredient.
	 *
	 * @param input      The input
	 * @param ingredient The ingredient
	 * @return The output, or null if invalid input or ingredient.
	 */
	@Nullable
	@Override
	public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
		return isInput(input) && isIngredient(ingredient) ? PotionUtils.addPotionToItemStack(new ItemStack(input.getItem()), getOutput()) : null;
	}

	/**
	 * Get the input {@link PotionType}.
	 *
	 * @return The input PotionType.
	 */
	public PotionType getInput() {
		return input;
	}

	/**
	 * Get the ingredient.
	 *
	 * @return The ingredient.
	 */
	public T getIngredient() {
		return ingredient;
	}

	/**
	 * Get the output {@link PotionType}.
	 *
	 * @return The output PotionType.
	 */
	public PotionType getOutput() {
		return output;
	}
}
