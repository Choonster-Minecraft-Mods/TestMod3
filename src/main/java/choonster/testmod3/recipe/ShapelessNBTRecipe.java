package choonster.testmod3.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A shapeless ore recipe that supports NBT on ingredient ItemStacks.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,34577.0.html
 *
 * @author Choonster
 */
public class ShapelessNBTRecipe extends ShapelessOreRecipe {
	public ShapelessNBTRecipe(Block result, Object... recipe) {
		super(result, recipe);
	}

	public ShapelessNBTRecipe(Item result, Object... recipe) {
		super(result, recipe);
	}

	public ShapelessNBTRecipe(ItemStack result, Object... recipe) {
		super(result, recipe);
	}

	@Override
	public boolean matches(InventoryCrafting var1, World world) {
		final ArrayList<Object> required = new ArrayList<>(input);

		for (int x = 0; x < var1.getSizeInventory(); x++) {
			final ItemStack slot = var1.getStackInSlot(x);

			if (!slot.func_190926_b()) {
				boolean inRecipe = false;
				final Iterator<Object> req = required.iterator();

				while (req.hasNext()) {
					boolean match = false;

					final Object next = req.next();

					if (next instanceof ItemStack) {
						match = itemMatches((ItemStack) next, slot);
					} else if (next instanceof List) {
						@SuppressWarnings("unchecked")
						final Iterator<ItemStack> itr = ((List<ItemStack>) next).iterator();
						while (itr.hasNext() && !match) {
							match = itemMatches(itr.next(), slot);
						}
					}

					if (match) {
						inRecipe = true;
						req.remove();
						break;
					}
				}

				if (!inRecipe) {
					return false;
				}
			}
		}

		return required.isEmpty();
	}


	protected boolean itemMatches(ItemStack required, ItemStack present) {
		return OreDictionary.itemMatches(required, present, false) && ItemStack.areItemStackTagsEqual(required, present);
	}
}
