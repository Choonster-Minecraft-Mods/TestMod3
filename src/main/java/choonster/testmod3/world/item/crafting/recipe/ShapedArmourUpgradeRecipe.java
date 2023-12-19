package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import net.minecraft.core.RegistryAccess;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * A shaped recipe class that copies the item damage of the first armour ingredient to the output. The damage is clamped to the output item's damage range.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipe extends ShapedRecipe {
	private ShapedArmourUpgradeRecipe(
			final String group,
			final CraftingBookCategory category,
			final ShapedRecipePattern pattern,
			final ItemStack result,
			final boolean showNotification
	) {
		super(group, category, pattern, result, showNotification);
	}

	@Override
	public ItemStack assemble(final CraftingContainer inv, final RegistryAccess registryAccess) {
		final var output = super.assemble(inv, registryAccess).copy(); // Get the default output

		if (!output.isEmpty()) {
			for (var i = 0; i < inv.getContainerSize(); i++) { // For each slot in the crafting inventory,
				final var ingredient = inv.getItem(i); // Get the ingredient in the slot

				if (!ingredient.isEmpty() && ingredient.getItem() instanceof ArmorItem) { // If it's an armour item,
					// Clone its item damage, clamping it to the output's damage range
					final var newDamage = Mth.clamp(ingredient.getDamageValue(), 0, output.getMaxDamage());
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

	public static class Serializer extends ShapedRecipeSerializer<ShapedArmourUpgradeRecipe> {
		public Serializer() {
			super(ShapedArmourUpgradeRecipe::new);
		}
	}
}
