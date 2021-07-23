package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.registries.ForgeRegistryEntry;

/**
 * A shapeless recipe that damages any {@link AxeItem} ingredients.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipe extends ShapelessRecipe {
	private ShapelessCuttingRecipe(final ResourceLocation id, final String group, final ItemStack recipeOutput, final NonNullList<Ingredient> ingredients) {
		super(id, group, recipeOutput, ingredients);
	}

	private ItemStack damageAxe(final ItemStack stack) {
		final Player craftingPlayer = ForgeHooks.getCraftingPlayer();
		if (stack.hurt(1, craftingPlayer.getCommandSenderWorld().random, craftingPlayer instanceof ServerPlayer ? (ServerPlayer) craftingPlayer : null)) {
			ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, stack, null);
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingContainer inv) {
		final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < remainingItems.size(); ++i) {
			final ItemStack itemstack = inv.getItem(i);

			if (!itemstack.isEmpty() && itemstack.getItem() instanceof AxeItem) {
				remainingItems.set(i, damageAxe(itemstack.copy()));
			} else {
				remainingItems.set(i, ForgeHooks.getContainerItem(itemstack));
			}
		}

		return remainingItems;
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.CUTTING_SHAPELESS.get();
	}

	public static class Serializer extends ForgeRegistryEntry<RecipeSerializer<?>> implements RecipeSerializer<ShapelessCuttingRecipe> {
		@Override
		public ShapelessCuttingRecipe fromJson(final ResourceLocation recipeID, final JsonObject json) {
			final String group = GsonHelper.getAsString(json, "group", "");
			final NonNullList<Ingredient> ingredients = RecipeUtil.parseShapeless(json);
			final ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);

			return new ShapelessCuttingRecipe(recipeID, group, result, ingredients);
		}

		@Override
		public ShapelessCuttingRecipe fromNetwork(final ResourceLocation recipeID, final FriendlyByteBuf buffer) {
			final String group = buffer.readUtf(Short.MAX_VALUE);
			final int numIngredients = buffer.readVarInt();
			final NonNullList<Ingredient> ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			final ItemStack result = buffer.readItem();

			return new ShapelessCuttingRecipe(recipeID, group, result, ingredients);
		}

		@Override
		public void toNetwork(final FriendlyByteBuf buffer, final ShapelessCuttingRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (final Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem());
		}
	}
}
