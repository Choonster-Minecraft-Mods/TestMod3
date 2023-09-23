package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.mojang.serialization.Codec;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;

/**
 * A shaped recipe class that copies the item damage of the first armour ingredient to the output. The damage is clamped to the output item's damage range.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipe extends ShapedRecipe {
	private final ItemStack result;

	private ShapedArmourUpgradeRecipe(
			final String group,
			final CraftingBookCategory category,
			final int width,
			final int height,
			final NonNullList<Ingredient> ingredients,
			final ItemStack result,
			final boolean showNotification
	) {
		super(group, category, width, height, ingredients, result, showNotification);
		this.result = result;
	}

	@Override
	public ItemStack assemble(final CraftingContainer inv, final RegistryAccess registryAccess) {
		final var output = super.assemble(inv, registryAccess); // Get the default output

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

	public static class Serializer implements RecipeSerializer<ShapedArmourUpgradeRecipe> {
		private static final Codec<ShapedArmourUpgradeRecipe> CODEC = RecipeUtil.shapedRecipeCodec(ShapedArmourUpgradeRecipe::new);

		@Override
		public Codec<ShapedArmourUpgradeRecipe> codec() {
			return CODEC;
		}
		
		@Override
		public ShapedArmourUpgradeRecipe fromNetwork(final FriendlyByteBuf buffer) {
			final var width = buffer.readVarInt();
			final var height = buffer.readVarInt();
			final var group = buffer.readUtf(Short.MAX_VALUE);
			final var category = buffer.readEnum(CraftingBookCategory.class);
			final var ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

			final var result = buffer.readItem();
			final var showNotification = buffer.readBoolean();

			return new ShapedArmourUpgradeRecipe(group, category, width, height, ingredients, result, showNotification);
		}

		@Override
		public void toNetwork(final FriendlyByteBuf buffer, final ShapedArmourUpgradeRecipe recipe) {
			buffer.writeVarInt(recipe.getRecipeWidth());
			buffer.writeVarInt(recipe.getRecipeHeight());
			buffer.writeUtf(recipe.getGroup());
			buffer.writeEnum(recipe.category());

			for (final var ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.result);
			buffer.writeBoolean(recipe.showNotification());
		}
	}
}
