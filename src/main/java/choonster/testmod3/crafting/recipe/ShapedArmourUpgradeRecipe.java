package choonster.testmod3.crafting.recipe;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapedRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
	public ItemStack getCraftingResult(final IInventory inv) {
		final ItemStack output = super.getCraftingResult(inv); // Get the default output

		if (!output.isEmpty()) {
			for (int i = 0; i < inv.getSizeInventory(); i++) { // For each slot in the crafting inventory,
				final ItemStack ingredient = inv.getStackInSlot(i); // Get the ingredient in the slot

				if (!ingredient.isEmpty() && ingredient.getItem() instanceof ItemArmor) { // If it's an armour item,
					// Clone its item damage, clamping it to the output's damage range
					final int newDamage = MathHelper.clamp(ingredient.getDamage(), 0, output.getMaxDamage());
					output.setDamage(newDamage);
					break; // Break now
				}
			}
		}

		return output; // Return the modified output
	}

	@Override
	public IRecipeSerializer<?> getSerializer() {
		return ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED;
	}

	public static class Serializer implements IRecipeSerializer<ShapedArmourUpgradeRecipe> {
		private static final ResourceLocation NAME = new ResourceLocation(TestMod3.MODID, "armour_upgrade_shaped");

		@Override
		public ShapedArmourUpgradeRecipe read(final ResourceLocation recipeID, final JsonObject json) {
			final String group = JsonUtils.getString(json, "group", "");
			final RecipeUtil.ShapedPrimer primer = RecipeUtil.parseShaped(json);
			final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), true);

			return new ShapedArmourUpgradeRecipe(recipeID, group, primer.getRecipeWidth(), primer.getRecipeHeght(), primer.getIngredients(), result);
		}

		@Override
		public ShapedArmourUpgradeRecipe read(final ResourceLocation recipeID, final PacketBuffer buffer) {
			final int width = buffer.readVarInt();
			final int height = buffer.readVarInt();
			final String group = buffer.readString(Short.MAX_VALUE);
			final NonNullList<Ingredient> ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

			for (int i = 0; i < ingredients.size(); ++i) {
				ingredients.set(i, Ingredient.read(buffer));
			}

			final ItemStack result = buffer.readItemStack();

			return new ShapedArmourUpgradeRecipe(recipeID, group, width, height, ingredients, result);
		}

		@Override
		public void write(final PacketBuffer buffer, final ShapedArmourUpgradeRecipe recipe) {
			buffer.writeVarInt(recipe.getRecipeWidth());
			buffer.writeVarInt(recipe.getRecipeHeight());
			buffer.writeString(recipe.getGroup());

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
