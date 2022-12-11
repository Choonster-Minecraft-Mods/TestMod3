package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * A shapeless recipe that damages any {@link AxeItem} ingredients.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipe extends ShapelessRecipe {
	private ShapelessCuttingRecipe(
			final ResourceLocation id,
			final String group,
			final CraftingBookCategory category,
			final ItemStack recipeOutput,
			final NonNullList<Ingredient> ingredients
	) {
		super(id, group, category, recipeOutput, ingredients);
	}

	private ItemStack damageAxe(final ItemStack stack) {
		final var craftingPlayer = ForgeHooks.getCraftingPlayer();
		if (stack.hurt(1, craftingPlayer.getCommandSenderWorld().random, craftingPlayer instanceof ServerPlayer ? (ServerPlayer) craftingPlayer : null)) {
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

	public static class Serializer implements RecipeSerializer<ShapelessCuttingRecipe> {
		@Override
		public ShapelessCuttingRecipe fromJson(final ResourceLocation recipeID, final JsonObject json) {
			final var group = GsonHelper.getAsString(json, "group", "");
			final var category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
			final var ingredients = RecipeUtil.parseShapeless(json);
			final var result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);

			return new ShapelessCuttingRecipe(recipeID, group, category, result, ingredients);
		}

		@Override
		public ShapelessCuttingRecipe fromNetwork(final ResourceLocation recipeID, final FriendlyByteBuf buffer) {
			final var group = buffer.readUtf(Short.MAX_VALUE);
			final var category = buffer.readEnum(CraftingBookCategory.class);
			final var numIngredients = buffer.readVarInt();
			final var ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

			final var result = buffer.readItem();

			return new ShapelessCuttingRecipe(recipeID, group, category, result, ingredients);
		}

		@Override
		public void toNetwork(final FriendlyByteBuf buffer, final ShapelessCuttingRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeEnum(recipe.category());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (final var ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem());
		}
	}
}
