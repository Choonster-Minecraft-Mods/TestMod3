package choonster.testmod3.crafting.recipe;

import choonster.testmod3.crafting.ingredient.FluidContainerIngredient;
import choonster.testmod3.util.ModFluidUtil;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
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
	public NonNullList<ItemStack> getRemainingItems(final CraftingInventory inv) {
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

	public static class Serializer extends ForgeRegistryEntry<IRecipeSerializer<?>> implements IRecipeSerializer<ShapelessFluidContainerRecipe> {
		@Override
		public ShapelessFluidContainerRecipe fromJson(final ResourceLocation recipeId, final JsonObject json) {
			final String group = JSONUtils.getAsString(json, "group", "");
			final NonNullList<Ingredient> ingredients = RecipeUtil.parseShapeless(json);
			final ItemStack result = CraftingHelper.getItemStack(JSONUtils.getAsJsonObject(json, "result"), true);

			ingredients
					.stream()
					.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
					.findFirst()
					.orElseThrow(() -> new JsonSyntaxException("Recipe must have at least one testmod3:fluid_container ingredient"));

			return new ShapelessFluidContainerRecipe(recipeId, group, result, ingredients);
		}

		@Nullable
		@Override
		public ShapelessFluidContainerRecipe fromNetwork(final ResourceLocation recipeId, final PacketBuffer buffer) {
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
		public void toNetwork(final PacketBuffer buffer, final ShapelessFluidContainerRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (final Ingredient ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.getResultItem());
		}
	}
}
