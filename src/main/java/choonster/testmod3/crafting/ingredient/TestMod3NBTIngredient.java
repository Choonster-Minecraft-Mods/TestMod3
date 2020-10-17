package choonster.testmod3.crafting.ingredient;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.crafting.NBTIngredient;

/**
 * This class only exists to access the protected constructor of {@link NBTIngredient}, it doesn't add or change any
 * functionality.
 *
 * @author Choonster
 */
public class TestMod3NBTIngredient extends NBTIngredient {
	protected TestMod3NBTIngredient(final ItemStack stack) {
		super(stack);
	}
}
