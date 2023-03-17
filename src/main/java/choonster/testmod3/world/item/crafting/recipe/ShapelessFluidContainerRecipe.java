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
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.crafting.CraftingHelper;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

/**
 * A shapeless recipe that drains fluids from any {@link FluidContainerIngredient}s.
 * <p>
 * The recipe must have at least one of these ingredients.
 *
 * @author Choonster
 */
public class ShapelessFluidContainerRecipe extends ShapelessRecipe {
	private final ItemStack result;

	private ShapelessFluidContainerRecipe(
			final ResourceLocation id,
			final String group,
			final CraftingBookCategory category,
			final ItemStack result,
			final NonNullList<Ingredient> ingredients
	) {
		super(id, group, category, result, ingredients);
		this.result = result;
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

	public static class Serializer implements RecipeSerializer<ShapelessFluidContainerRecipe> {
		@Override
		public ShapelessFluidContainerRecipe fromJson(final ResourceLocation recipeId, final JsonObject json) {
			final var group = GsonHelper.getAsString(json, "group", "");
			final var category = CraftingBookCategory.CODEC.byName(GsonHelper.getAsString(json, "category", null), CraftingBookCategory.MISC);
			final var ingredients = RecipeUtil.parseShapeless(json);
			final var result = CraftingHelper.getItemStack(GsonHelper.getAsJsonObject(json, "result"), true);

			ingredients
					.stream()
					.filter(ingredient -> ingredient instanceof FluidContainerIngredient)
					.findFirst()
					.orElseThrow(() -> new JsonSyntaxException("Recipe must have at least one testmod3:fluid_container ingredient"));

			return new ShapelessFluidContainerRecipe(recipeId, group, category, result, ingredients);
		}

		@Nullable
		@Override
		public ShapelessFluidContainerRecipe fromNetwork(final ResourceLocation recipeId, final FriendlyByteBuf buffer) {
			final var group = buffer.readUtf(Short.MAX_VALUE);
			final var category = buffer.readEnum(CraftingBookCategory.class);
			final var numIngredients = buffer.readVarInt();
			final var ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

			ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

			final var result = buffer.readItem();

			return new ShapelessFluidContainerRecipe(recipeId, group, category, result, ingredients);
		}

		@Override
		public void toNetwork(final FriendlyByteBuf buffer, final ShapelessFluidContainerRecipe recipe) {
			buffer.writeUtf(recipe.getGroup());
			buffer.writeEnum(recipe.category());
			buffer.writeVarInt(recipe.getIngredients().size());

			for (final var ingredient : recipe.getIngredients()) {
				ingredient.toNetwork(buffer);
			}

			buffer.writeItem(recipe.result);
		}
	}
}
