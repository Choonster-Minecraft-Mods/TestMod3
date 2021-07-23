package choonster.testmod3.world.item.crafting.ingredient;

import net.minecraft.world.item.ItemStack;
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
