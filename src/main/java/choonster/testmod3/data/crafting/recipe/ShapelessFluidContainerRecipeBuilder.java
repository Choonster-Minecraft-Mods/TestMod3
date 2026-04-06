package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import choonster.testmod3.world.item.crafting.recipe.ShapelessFluidContainerRecipe;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapelessFluidContainerRecipe}.
 *
 * @author Choonster
 */
public class ShapelessFluidContainerRecipeBuilder extends BaseShapelessRecipeBuilder<ShapelessFluidContainerRecipe, ShapelessFluidContainerRecipeBuilder> {
	protected ShapelessFluidContainerRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result
	) {
		super(items, category, result, ShapelessFluidContainerRecipe::new);
	}

	/**
	 * Creates a new builder for a shapeless fluid container recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapelessFluidContainerRecipeBuilder shapelessFluidContainerRecipe(
			final HolderGetter<Item> items, final RecipeCategory category,
			final ItemLike result
	) {
		return shapelessFluidContainerRecipe(items, category, new ItemStackTemplate(result.asItem()));
	}

	/**
	 * Creates a new builder for a shapeless fluid container recipe.
	 *
	 * @param items  The item registry
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapelessFluidContainerRecipeBuilder shapelessFluidContainerRecipe(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result
	) {
		return new ShapelessFluidContainerRecipeBuilder(items, category, result);
	}

	@Override
	protected void validate(final ResourceKey<Recipe<?>> key) {
		super.validate(key);

		getIngredients().stream()
				.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
				.findFirst()
				.orElseThrow(() -> new JsonSyntaxException("Recipe must have at least one testmod3:fluid_container ingredient"));
	}
}
