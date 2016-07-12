package choonster.testmod3.recipe.brewing;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraftforge.common.brewing.BrewingOreRecipe;
import net.minecraftforge.oredict.OreDictionary;

import java.util.List;

/**
 * A brewing recipe that imitates vanilla's {@link PotionType} conversion ({@link PotionHelper#registerPotionTypeConversion}) with an Ore Dictionary name as the ingredient.
 * <p>
 * Adapted from {@link BrewingOreRecipe}.
 *
 * @author Choonster
 */
public class OrePotionTypeConversionBrewingRecipe extends AbstractPotionTypeConversionBrewingRecipe<List<ItemStack>> {
	public OrePotionTypeConversionBrewingRecipe(PotionType input, String oreName, PotionType output) {
		super(input, OreDictionary.getOres(oreName), output);
	}

	public OrePotionTypeConversionBrewingRecipe(PotionType input, List<ItemStack> ingredient, PotionType output) {
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
		for (ItemStack target : this.getIngredient()) {
			if (OreDictionary.itemMatches(target, ingredient, false)) {
				return true;
			}
		}

		return false;
	}
}
