package choonster.testmod3.crafting.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.IngredientNBT;

/**
 * This class only exists to access the protected constructor of {@link IngredientNBT}, it doesn't add or change any
 * functionality.
 *
 * @author Choonster
 */
public class IngredientNBTTestMod3 extends IngredientNBT {
	protected IngredientNBTTestMod3(final ItemStack stack) {
		super(stack);
	}
}
