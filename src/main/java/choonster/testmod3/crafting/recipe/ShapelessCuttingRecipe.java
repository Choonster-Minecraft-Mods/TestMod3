package choonster.testmod3.crafting.recipe;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.ForgeEventFactory;

/**
 * A shapeless recipe that damages any {@link ItemAxe} ingredients.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipe extends ShapelessRecipe {
	private ShapelessCuttingRecipe(final ResourceLocation id, final String group, final ItemStack recipeOutput, final NonNullList<Ingredient> ingredients) {
		super(id, group, recipeOutput, ingredients);
	}

	private ItemStack damageAxe(final ItemStack stack) {
		final EntityPlayer craftingPlayer = ForgeHooks.getCraftingPlayer();
		if (stack.attemptDamageItem(1, craftingPlayer.getEntityWorld().rand, craftingPlayer instanceof EntityPlayerMP ? (EntityPlayerMP) craftingPlayer : null)) {
			ForgeEventFactory.onPlayerDestroyItem(craftingPlayer, stack, null);
			return ItemStack.EMPTY;
		}

		return stack;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final IInventory inv) {
		final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);

		for (int i = 0; i < remainingItems.size(); ++i) {
			final ItemStack itemstack = inv.getStackInSlot(i);

			if (!itemstack.isEmpty() && itemstack.getItem() instanceof ItemAxe) {
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

	public static class Serializer implements IRecipeSerializer<ShapelessCuttingRecipe> {
		private static final ResourceLocation NAME = new ResourceLocation(TestMod3.MODID, "cutting_shapeless");

		@Override
		public ShapelessCuttingRecipe read(final ResourceLocation recipeID, final JsonObject json) {
			final String group = JsonUtils.getString(json, "group", "");
			final NonNullList<Ingredient> ingredients = RecipeUtil.parseShapeless(json);
			final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), true);

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

		@Override
		public ResourceLocation getName() {
			return NAME;
		}
	}
}
