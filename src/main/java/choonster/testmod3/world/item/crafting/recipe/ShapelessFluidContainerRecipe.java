package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.common.ForgeHooks;

import java.util.List;
import java.util.stream.Collectors;

/**
 * A shapeless recipe that drains fluids from any {@link FluidContainerIngredient}s.
 * <p>
 * The recipe must have at least one of these ingredients.
 *
 * @author Choonster
 */
public class ShapelessFluidContainerRecipe extends ShapelessRecipe {
	private final List<Ingredient> ingredients;

	private ShapelessFluidContainerRecipe(
			final String group,
			final CraftingBookCategory category,
			final ItemStack result,
			final List<Ingredient> ingredients
	) {
		super(group, category, result, ingredients);
		this.ingredients = ingredients;
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingInput input) {
		final var fluidContainerIngredients = ingredients
				.stream()
				.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
				.map(ingredient -> (FluidContainerIngredient) ingredient)
				.collect(Collectors.toSet());

		final var remainingItems = NonNullList.withSize(input.size(), ItemStack.EMPTY);

		for (var i = 0; i < remainingItems.size(); ++i) {
			final var stack = input.getItem(i);

			if (stack.isEmpty()) {
				continue;
			}

			final var matchingIngredient = fluidContainerIngredients
					.stream()
					.filter(ingredient -> ingredient.test(stack))
					.findFirst();

			if (matchingIngredient.isPresent()) {
				final var ingredient = matchingIngredient.get();
				final var drainResult = ModFluidUtil.drainContainer(stack, ingredient.getFluidStack());

				if (drainResult.isSuccess()) {
					remainingItems.set(i, drainResult.getResult());

					fluidContainerIngredients.remove(ingredient);

					continue;
				}
			}

			remainingItems.set(i, ForgeHooks.getCraftingRemainingItem(stack));
		}

		return remainingItems;
	}

	@Override
	public RecipeSerializer<ShapelessRecipe> getSerializer() {
		return ModCrafting.Recipes.FLUID_CONTAINER_SHAPELESS.get();
	}

	public static class Serializer extends ShapelessRecipeSerializer<ShapelessRecipe> {
		private final MapCodec<ShapelessRecipe> codec;

		public Serializer() {
			super(ShapelessFluidContainerRecipe::new);

			codec = super.codec().validate(
					recipe -> ((ShapelessFluidContainerRecipe) recipe).ingredients
							.stream()
							.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
							.findFirst()
							.map(ingredient -> DataResult.success(recipe))
							.orElseGet(() -> DataResult.error(() -> "Recipe must have at least one testmod3:fluid_container ingredient"))
			);
		}

		@Override
		public MapCodec<ShapelessRecipe> codec() {
			return codec;
		}
	}
}
