package choonster.testmod3.crafting.recipe;

import com.google.gson.JsonObject;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IRecipeFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.oredict.ShapedOreRecipe;

import javax.annotation.Nullable;

/**
 * A shaped recipe class that copies the item damage of the first armour ingredient to the output. The damage is clamped to the output item's damage range.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class ShapedArmourUpgradeRecipe extends ShapedOreRecipe {
	public ShapedArmourUpgradeRecipe(@Nullable final ResourceLocation group, final ItemStack result, final CraftingHelper.ShapedPrimer primer) {
		super(group, result, primer);
	}

	@Override
	public ItemStack getCraftingResult(final InventoryCrafting inv) {
		final ItemStack output = super.getCraftingResult(inv); // Get the default output

		if (!output.isEmpty()) {
			for (int i = 0; i < inv.getSizeInventory(); i++) { // For each slot in the crafting inventory,
				final ItemStack ingredient = inv.getStackInSlot(i); // Get the ingredient in the slot

				if (!ingredient.isEmpty() && ingredient.getItem() instanceof ItemArmor) { // If it's an armour item,
					// Clone its item damage, clamping it to the output's damage range
					final int newDamage = MathHelper.clamp(ingredient.getItemDamage(), 0, output.getMaxDamage());
					output.setItemDamage(newDamage);
					break; // Break now
				}
			}
		}

		return output; // Return the modified output
	}

	@Override
	public String getGroup() {
		return group == null ? "" : group.toString();
	}

	public static class Factory implements IRecipeFactory {

		@Override
		public IRecipe parse(final JsonContext context, final JsonObject json) {
			final String group = JsonUtils.getString(json, "group", "");
			final CraftingHelper.ShapedPrimer primer = RecipeUtil.parseShaped(context, json);
			final ItemStack result = CraftingHelper.getItemStack(JsonUtils.getJsonObject(json, "result"), context);

			return new ShapedArmourUpgradeRecipe(group.isEmpty() ? null : new ResourceLocation(group), result, primer);
		}
	}
}
