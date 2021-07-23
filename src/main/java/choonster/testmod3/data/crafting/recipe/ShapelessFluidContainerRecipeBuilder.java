package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import choonster.testmod3.world.item.crafting.recipe.ShapelessFluidContainerRecipe;
import com.google.gson.JsonSyntaxException;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;

/**
 * Builder for {@link ShapelessFluidContainerRecipe}.
 *
 * @author Choonster
 */
public class ShapelessFluidContainerRecipeBuilder extends EnhancedShapelessRecipeBuilder<ShapelessFluidContainerRecipe, ShapelessFluidContainerRecipeBuilder> {
	protected ShapelessFluidContainerRecipeBuilder(final ItemStack result) {
		super(result, ModCrafting.Recipes.FLUID_CONTAINER_SHAPELESS.get());
	}

	/**
	 * Creates a new builder for a shapeless fluid container recipe.
	 *
	 * @param result The recipe result item
	 * @return The builder
	 */
	public static ShapelessFluidContainerRecipeBuilder shapelessFluidContainerRecipe(final ItemLike result) {
		return shapelessFluidContainerRecipe(new ItemStack(result));
	}

	/**
	 * Creates a new builder for a shapeless fluid container recipe.
	 *
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapelessFluidContainerRecipeBuilder shapelessFluidContainerRecipe(final ItemStack result) {
		return new ShapelessFluidContainerRecipeBuilder(result);
	}

	@Override
	protected void validate(final ResourceLocation id) {
		super.validate(id);

		getIngredients().stream()
				.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
				.findFirst()
				.orElseThrow(() -> new JsonSyntaxException("Recipe must have at least one testmod3:fluid_container ingredient"));
	}
}
