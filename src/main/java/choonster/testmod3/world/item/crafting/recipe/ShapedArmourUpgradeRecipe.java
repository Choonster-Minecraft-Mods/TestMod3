package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraftforge.common.crafting.CraftingHelper;

/**
 * A shaped recipe class that copies the item damage of the first armour ingredient to the output. The damage is clamped to the output item's damage range.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipe extends ShapedRecipe {
	private ShapedArmourUpgradeRecipe(final ResourceLocation id, final String group, final int recipeWidth, final int recipeHeight, final NonNullList<Ingredient> ingredients, final ItemStack recipeOutput) {
		super(id, group, recipeWidth, recipeHeight, ingredients, recipeOutput);
	}

	@Override
	public ItemStack assemble(final CraftingContainer inv) {
		final ItemStack output = super.assemble(inv); // Get the default output

		if (!output.isEmpty()) {
			for (int i = 0; i < inv.getContainerSize(); i++) { // For each slot in the crafting inventory,
				final ItemStack ingredient = inv.getItem(i); // Get the ingredient in the slot

				if (!ingredient.isEmpty() && ingredient.getItem() instanceof ArmorItem) { // If it's an armour item,
					// Clone its item damage, clamping it to the output's damage range
					final int newDamage = Mth.clamp(ingredient.getDamageValue(), 0, output.getMaxDamage());
					output.setDamageValue(newDamage);
					break; // Break now
				}
			}
		}

		return output; // Return the modified output
	}

	@Override
	public RecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED.get();
	}

	public static class Serializer implements RecipeSerializer<ShapedArmourUpgradeRecipe> {
		@Override
		public ShapedArmourUpgradeRecipe fromJson(final ResourceLocation recipeID, final JsonObject json) {
			final String group = GsonHelper.getAsString(json, "group", "");
			final RecipeUtil.ShapedPrimer primer = RecipeUtil.parseShaped(json);
			final ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);

			return new ShapedArmourUpgradeRecipe(recipeID, group, primer.recipeWidth(), primer.recipeHeight(), primer.ingredients(), result);
		}

		@Override
		public ShapedArmourUpgradeRecipe fromNetwork(final ResourceLocation recipeID, final FriendlyByteBuf buffer) {
			final int width = buffer.readVarInt();
			final int height = buffer.readVarInt();
			final String group = buffer.readUtf(Short.MAX_VALUE);
			final NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

			final ItemStack result = buffer.readItem();

			return new ShapedArmourUpgradeRecipe(recipeID, group, width, height, ingredients, result);
		}

		@Override
		public void toNetwork(final FriendlyByteBuf buffer, final ShapedArmourUpgradeRecipe recipe) {
			buffer.writeVarInt(recipe.getRecipeWidth());
			buffer.writeVarInt(recipe.getRecipeHeight());
			buffer.writeUtf(recipe.getGroup());

			for (final Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem());
		}
	}
}
