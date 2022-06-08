package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.world.item.crafting.ingredient.FluidContainerIngredient;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidActionResult;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A shapeless recipe that drains fluids from any {@link FluidContainerIngredient}s.
 * <p>
 * The recipe must have at least one of these ingredients.
 *
 * @author Choonster
 */
public class ShapelessFluidContainerRecipe extends ShapelessRecipe {
	private ShapelessFluidContainerRecipe(final ResourceLocation id, final String group, final ItemStack recipeOutput, final NonNullList<Ingredient> ingredients) {
		super(id, group, recipeOutput, ingredients);
	}

	@Override
	public NonNullList<ItemStack> getRemainingItems(final CraftingContainer inv) {
		final Set<FluidContainerIngredient> fluidContainerIngredients = getIngredients()
				.stream()
				.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
				.map(ingredient -> (FluidContainerIngredient) ingredient)
				.collect(Collectors.toSet());

		final NonNullList<ItemStack> remainingItems = NonNullList.withSize(inv.getContainerSize(), ItemStack.EMPTY);

		for (int i = 0; i < remainingItems.size(); ++i) {
			final ItemStack stack = inv.getItem(i);

			if (stack.isEmpty()) {
				continue;
			}

			final Optional<FluidContainerIngredient> matchingIngredient = fluidContainerIngredients
					.stream()
					.filter(ingredient -> ingredient.test(stack))
					.findFirst();

			if (matchingIngredient.isPresent()) {
				final FluidContainerIngredient ingredient = matchingIngredient.get();
				final FluidActionResult drainResult = ModFluidUtil.drainContainer(stack, ingredient.getFluidStack());

				if (drainResult.isSuccess()) {
					remainingItems.set(i, drainResult.getResult());

					fluidContainerIngredients.remove(ingredient);

					continue;
				}
			}

			remainingItems.set(i, ForgeHooks.getContainerItem(stack));
		}

		return remainingItems;
	}

	public static class Serializer implements RecipeSerializer<ShapelessFluidContainerRecipe> {
		@Override
		public ShapelessFluidContainerRecipe fromJson(final ResourceLocation recipeId, final JsonObject json) {
			final String group = GsonHelper.getAsString(json, "group", "");
			final NonNullList<Ingredient> ingredients = RecipeUtil.parseShapeless(json);
			final ItemStack result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);

			ingredients
					.stream()
					.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
					.findFirst()
					.orElseThrow(() -> new JsonSyntaxException("Recipe must have at least one testmod3:fluid_container ingredient"));

			return new ShapelessFluidContainerRecipe(recipeId, group, result, ingredients);
		}

		@Nullable
		@Override
		public ShapelessFluidContainerRecipe fromNetwork(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
			final String group = buffer.readUtf(Short.MAX_VALUE);
			final int numIngredients = buffer.readVarInt();
			final NonNullList<Ingredient> ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

			for (int j = 0; j < ingredients.size(); ++j) {
				ingredients.set(j, Ingredient.fromNetwork(buffer));
			}

			final ItemStack result = buffer.readItem();

			return new ShapelessFluidContainerRecipe(recipeId, group, result, ingredients);
		}

		@Override
		public void toNetwork(final FriendlyByteBuf buffer, final ShapelessFluidContainerRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (final Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem());
		}
	}
}
