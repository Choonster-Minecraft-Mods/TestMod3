package choonster.testmod3.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.AxeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
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
		final PlayerEntity craftingPlayer = ForgeHooks.getCraftingPlayer();
		if (stack.attemptDamageItem(1, craftingPlayer.getEntityWorld().rand, craftingPlayer instanceof ServerPlayerEntity ? (ServerPlayerEntity) craftingPlayer : null)) {
			ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, stack, null);
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingInventory inv) {
		final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < remainingItems.size(); ++i) {
			final ItemStack itemstack = inv.getStackInSlot(i);

			if (!itemstack.isEmpty() && itemstack.getItem() instanceof AxeItem) {
				remainingItems.set(i, damageAxe(itemstack.copy()));
			} else {
				remainingItems.set(i, ForgeHooks.getContainerItem(itemstack));
			}
		}

		return remainingItems;
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.CUTTING_SHAPELESS;
	}

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessCuttingRecipe> {
		@Override
		public ShapelessCuttingRecipe read(final ResourceLocation recipeID, final JsonObject json) {
			final String group = JSONUtils.getString(json, "group", "");
			final NonNullList<Ingredient> ingredients = RecipeUtil.parseShapeless(json);
			final ItemStack result = CraftingHelper.getItemStack(JSONUtils.getJsonObject(json, "result"), true);

			return new ShapelessCuttingRecipe(recipeID, group, result, ingredients);
		}

		@Override
		public ShapelessCuttingRecipe read(final ResourceLocation recipeID, final PacketBuffer buffer) {
			final String group = buffer.readString(Short.MAX_VALUE);
			final int numIngredients = buffer.readVarInt();
			final NonNullList<Ingredient> ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.read(buffer));
			}

			final ItemStack result = buffer.readItemStack();

			return new ShapelessCuttingRecipe(recipeID, group, result, ingredients);
		}

		@Override
		public void write(final PacketBuffer buffer, final ShapelessCuttingRecipe recipe) {
			buffer.writeString(recipe.getGroup());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (final Ingredient ingredient : recipe.getIngredients()) {
				ingredient.write(buffer);
			}

			buffer.writeItemStack(recipe.getRecipeOutput());
		}
	}
}
