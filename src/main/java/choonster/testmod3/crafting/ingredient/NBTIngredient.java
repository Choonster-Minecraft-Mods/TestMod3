package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntComparators;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.client.util.RecipeItemHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nullable;

/**
 * An {@link Ingredient} that matches NBT and capabilities in addition to the {@link Item} and metadata.
 * <p>
 * JSON Properties:
 * <ul>
 * <li>All properties used by {@link CraftingHelper#getItemStack}</li>
 * </ul>
 *
 * @author Choonster
 */
public class NBTIngredient extends Ingredient {
	private final ItemStack[] matchingStacks;
	private IntList matchingStacksPacked;

	protected NBTIngredient(final ItemStack... stacks) {
		super(0);

		matchingStacks = stacks;
	}

	@Override
	public ItemStack[] getMatchingStacks() {
		return matchingStacks;
	}

	@Override
	public boolean apply(@Nullable final ItemStack stackToCheck) {
		if (stackToCheck == null) return false;

		for (final ItemStack stack : matchingStacks) {
			if (stack.getItem() == stackToCheck.getItem()) {
				final int metadata = stack.getMetadata();

				if ((metadata == OreDictionary.WILDCARD_VALUE || metadata == stackToCheck.getMetadata()) &&
						ItemStack.areItemStackTagsEqual(stackToCheck, stack)) {
					return true;
				}
			}
		}

		return false;
	}

	@Override
	public IntList getValidItemStacksPacked() {
		if (matchingStacksPacked == null) {
			matchingStacksPacked = new IntArrayList(this.matchingStacks.length);

			for (final ItemStack itemstack : this.matchingStacks) {
				matchingStacksPacked.add(RecipeItemHelper.pack(itemstack));
			}

			matchingStacksPacked.sort(IntComparators.NATURAL_COMPARATOR);
		}

		return matchingStacksPacked;
	}

	@Override
	protected void invalidate() {
		matchingStacksPacked = null;
	}

	public static Ingredient fromStacks(final ItemStack... stacks) {
		if (stacks.length > 0) {
			for (final ItemStack itemstack : stacks) {
				if (!itemstack.isEmpty()) {
					return new NBTIngredient(stacks);
				}
			}
		}

		return EMPTY;
	}

	@SuppressWarnings("unused")
	public static class Factory implements IIngredientFactory {

		@Override
		public Ingredient parse(final JsonContext context, final JsonObject json) {
			final ItemStack stack = CraftingHelper.getItemStack(json, context);

			return NBTIngredient.fromStacks(stack);
		}
	}
}
