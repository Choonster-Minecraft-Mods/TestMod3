package choonster.testmod3.recipe;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraft.util.NonNullList;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

import java.util.Random;

/**
 * A shapeless recipe that damages any {@link ItemAxe} ingredients.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipe extends ShapelessRecipes {
	private final Random random = new Random();

	public ShapelessCuttingRecipe(final String group, final ItemStack result, final NonNullList<Ingredient> ingredients) {
		super(group, result, ingredients);
	}

	private ItemStack damageAxe(final ItemStack stack) {
		final EntityPlayer craftingPlayer = ForgeHooks.getCraftingPlayer();
		if (stack.attemptDamageItem(1, random, craftingPlayer instanceof EntityPlayerMP ? (EntityPlayerMP) craftingPlayer : null)) {
			ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, stack, null);
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final InventoryCrafting inventoryCrafting) {
		final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inventoryCrafting.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < remainingItems.size(); ++i) {
			final ItemStack itemstack = inventoryCrafting.getStackInSlot(i);

			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemAxe) {
				remainingItems.set(i, damageAxe(itemstack.copy()));
			} else {
				remainingItems.set(i, ForgeHooks.getContainerItem(itemstack));
			}
		}

		return remainingItems;
	}
}
