package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * A shaped recipe class that copies the item damage of the first armour ingredient to the output. The damage is clamped to the output item's damage range.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2513998-help-needed-creating-crafting-recipe-with-damaged
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipe extends BaseShapedRecipe {
	public static final MapCodec<ShapedArmourUpgradeRecipe> MAP_CODEC =
			ShapedRecipeCodecs.mapCodec(ShapedArmourUpgradeRecipe::new);

	public static final StreamCodec<RegistryFriendlyByteBuf, ShapedArmourUpgradeRecipe> STREAM_CODEC =
			ShapedRecipeCodecs.streamCodec(ShapedArmourUpgradeRecipe::new);

	public static final RecipeSerializer<ShapedArmourUpgradeRecipe> SERIALIZER =
			new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	public ShapedArmourUpgradeRecipe(
			final CommonInfo commonInfo,
			final CraftingBookInfo bookInfo,
			final ShapedRecipePattern pattern,
			final ItemStackTemplate result
	) {
		super(commonInfo, bookInfo, pattern, result);
	}

	@Override
	public ItemStack assemble(final CraftingInput input) {
		final var output = super.assemble(input); // Get the default output

		if (!output.isEmpty()) {
			for (var i = 0; i < input.size(); i++) { // For each slot in the crafting inventory,
				final var ingredient = input.getItem(i); // Get the ingredient in the slot

				if (!ingredient.isEmpty() && ingredient.isDamageableItem()) { // If it's a damageable item,
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
	public RecipeSerializer<ShapedArmourUpgradeRecipe> getSerializer() {
		return ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED.get();
	}
}
