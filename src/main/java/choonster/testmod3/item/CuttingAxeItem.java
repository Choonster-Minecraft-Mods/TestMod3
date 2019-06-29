package choonster.testmod3.item;

import net.minecraft.item.AxeItem;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemStack;

/**
 * An axe that loses durability when used in crafting recipes
 *
 * @author Choonster
 */
public class CuttingAxeItem extends AxeItem {
	public CuttingAxeItem(final IItemTier tier, final float attackDamage, final float attackSpeed, final Properties properties) {
		super(tier, attackDamage, attackSpeed, properties);
	}

	@Override
	public boolean hasContainerItem(final ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getContainerItem(ItemStack stack) {
		stack = stack.copy();
		stack.attemptDamageItem(1, random, null);
		return stack;
	}
}
