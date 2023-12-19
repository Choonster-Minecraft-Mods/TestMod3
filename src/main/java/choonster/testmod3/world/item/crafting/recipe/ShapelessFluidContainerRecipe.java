package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.NonNullList;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeHooks;

import java.util.stream.Collectors;

/**
 * A shapeless recipe that drains fluids from any {@link FluidContainerIngredient}s.
 * <p>
 * The recipe must have at least one of these ingredients.
 *
 * @author Choonster
 */
public class ShapelessFluidContainerRecipe extends ShapelessRecipe {
	private ShapelessFluidContainerRecipe(
			final String group,
			final CraftingBookCategory category,
			final ItemStack result,
			final NonNullList<Ingredient> ingredients
	) {
		super(group, category, result, ingredients);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingContainer inv) {
		final var fluidContainerIngredients = getIngredients()
				.stream()
				.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
				.map(ingredient -> (FluidContainerIngredient) ingredient)
				.collect(Collectors.toSet());

		final var remainingItems = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for (var i = 0; i < remainingItems.size(); ++i) {
			final var stack = inv.getItem(i);

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

	public static class Serializer extends ShapelessRecipeSerializer<ShapelessFluidContainerRecipe> {
		private final Codec<ShapelessFluidContainerRecipe> codec;

		public Serializer() {
			super(ShapelessFluidContainerRecipe::new);

			codec = ExtraCodecs.validate(
					super.codec(),
					recipe -> recipe.getIngredients()
							.stream()
							.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
							.findFirst()
							.map(ingredient -> DataResult.success(recipe))
							.orElseGet(() -> DataResult.error(() -> "Recipe must have at least one testmod3:fluid_container ingredient"))
			);
		}

		@Override
		public Codec<ShapelessFluidContainerRecipe> codec() {
			return codec;
		}
	}
}
