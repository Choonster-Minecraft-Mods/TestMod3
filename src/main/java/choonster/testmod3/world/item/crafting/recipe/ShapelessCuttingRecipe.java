package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.ForgeEventFactory;

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
			final NonNullList<Ingredient> ingredients
	) {
		super(group, category, result, ingredients);
	}

	private ItemStack damageAxe(final ItemStack stack) {
		final var craftingPlayer = ForgeHooks.getCraftingPlayer();
		if (stack.hurt(1, craftingPlayer.getCommandSenderWorld().random, craftingPlayer instanceof final ServerPlayer serverPlayer ? serverPlayer : null)) {
			ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, stack, null);
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingContainer inv) {
		final var remainingItems = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for (var i = 0; i < remainingItems.size(); ++i) {
			final var stack = inv.getItem(i);

			if (!stack.isEmpty() && stack.getItem() instanceof AxeItem) {
				remainingItems.set(i, damageAxe(stack.copy()));
			} else {
				remainingItems.set(i, ForgeHooks.getCraftingRemainingItem(stack));
			}
		}

		return remainingItems;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.CUTTING_SHAPELESS.get();
	}

	public static class Serializer extends ShapelessRecipeSerializer<ShapelessCuttingRecipe> {
		public Serializer() {
			super(ShapelessCuttingRecipe::new);
		}
	}
}
