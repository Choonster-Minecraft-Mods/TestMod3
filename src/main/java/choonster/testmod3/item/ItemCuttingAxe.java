package choonster.testmod3.item;

import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;

/**
 * An axe that loses durability when used in crafting recipes
 *
 * @author Choonster
 */
public class ItemCuttingAxe extends ItemAxe {

	public ItemCuttingAxe(final ToolMaterial material) {
		super(material);
	}

	@Override
	public boolean hasContainerItem(final ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		stack = stack.copy();
		stack.attemptDamageItem(1, itemRand, null);
		return stack;
	}
}
