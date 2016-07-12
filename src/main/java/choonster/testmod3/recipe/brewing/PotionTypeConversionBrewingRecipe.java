package choonster.testmod3.recipe.brewing;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.oredict.OreDictionary;

/**
 * A brewing recipe that imitates vanilla's {@link PotionType} conversion ({@link PotionHelper#registerPotionTypeConversion}) with a single {@link ItemStack} as the ingredient.
 * <p>
 * Adapted from {@link BrewingRecipe}.
 *
 * @author Choonster
 */
public class PotionTypeConversionBrewingRecipe extends AbstractPotionTypeConversionBrewingRecipe<ItemStack> {
	public PotionTypeConversionBrewingRecipe(PotionType input, ItemStack ingredient, PotionType output) {
		super(input, ingredient, output);
	}


	/**
	 * Returns true if the passed ItemStack is an ingredient for this recipe.
	 * "Ingredient" being the item that goes in the top slot of the brewing
	 * stand (e.g: nether wart)
	 *
	 * @param ingredient The ingredient
	 */
	@Override
	public boolean isIngredient(ItemStack ingredient) {
		return OreDictionary.itemMatches(this.getIngredient(), ingredient, false);
	}
}
