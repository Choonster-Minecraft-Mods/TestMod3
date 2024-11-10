package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;

/**
 * A shapeless recipe that damages any {@link AxeItem} ingredients.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipe extends ShapelessRecipe {
	private ShapelessCuttingRecipe(
			final String group,
			final CraftingBookCategory category,
			final ItemStack result,
			final List<Ingredient> ingredients
	) {
		super(group, category, result, ingredients);
	}

	private ItemStack damageAxe(final ItemStack stack) {
		final var craftingPlayer = ForgeHooks.getCraftingPlayer();

		if (craftingPlayer.level() instanceof final ServerLevel serverLevel) {
			stack.hurtAndBreak(
					1,
					serverLevel,
					craftingPlayer instanceof final ServerPlayer serverPlayer ? serverPlayer : null,
					item -> stack.setCount(0)
			);
		}

		return stack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingInput input) {
		final var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (var i = 0; i < remainingItems.size(); ++i) {
			final var stack = input.getItem(i);

			if (!stack.isEmpty() && stack.getItem() instanceof AxeItem) {
				remainingItems.set(i, damageAxe(stack.copy()));
			} else {
				remainingItems.set(i, ForgeHooks.getCraftingRemainingItem(stack));
			}
		}

		return remainingItems;
	}

	@Override
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return ModCrafting.Recipes.CUTTING_SHAPELESS.get();
	}

	public static class Serializer extends ShapelessRecipeSerializer<ShapelessRecipe> {
		public Serializer() {
			super(ShapelessCuttingRecipe::new);
		}
	}
}
