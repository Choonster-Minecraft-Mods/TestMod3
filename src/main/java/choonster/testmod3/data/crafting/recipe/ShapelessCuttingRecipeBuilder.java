package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.crafting.recipe.ShapelessCuttingRecipe;
import choonster.testmod3.init.ModCrafting;
import com.google.common.base.Preconditions;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Builder for {@link ShapelessCuttingRecipe}.
 *
 * @author Choonster
 */
public class ShapelessCuttingRecipeBuilder extends ShapelessRecipeBuilder {
	private final Item result;

	private ShapelessCuttingRecipeBuilder(final IItemProvider result, final int count) {
		super(result, count);
		this.result = result.asItem();
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param resultIn The result item
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(final IItemProvider resultIn) {
		return new ShapelessCuttingRecipeBuilder(resultIn, 1);
	}

	/**
	 * Creates a new builder for a shapeless cutting recipe.
	 *
	 * @param resultIn The result item
	 * @param countIn  The result count
	 * @return The builder
	 */
	public static ShapelessCuttingRecipeBuilder shapelessCuttingRecipe(final IItemProvider resultIn, final int countIn) {
		return new ShapelessCuttingRecipeBuilder(resultIn, countIn);
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 *
	 * @param consumer The recipe consumer
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer) {
		build(consumer, Preconditions.checkNotNull(result.getRegistryName()));
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
	 * the result.
	 *
	 * @param consumer The recipe consumer
	 * @param save     The ID to use for the recipe
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final String save) {
		final ResourceLocation resourcelocation = result.getRegistryName();
		if (new ResourceLocation(save).equals(resourcelocation)) {
			throw new IllegalStateException("Shaped Armour Upgrade Recipe " + save + " should remove its 'save' argument");
		} else {
			build(consumer, new ResourceLocation(save));
		}
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 *
	 * @param consumer The recipe consumer
	 * @param id       The ID to use for the recipe
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final ResourceLocation id) {
		// Use an AtomicReference to capture the IFinishedRecipe created by the super method
		final AtomicReference<IFinishedRecipe> baseResult = new AtomicReference<>();
		super.build(baseResult::set, id);

		consumer.accept(new Result(baseResult.get()));
	}

	public static class Result extends FinishedRecipeDelegate {
		private Result(final IFinishedRecipe baseRecipe) {
			super(baseRecipe);
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModCrafting.Recipes.CUTTING_SHAPELESS;
		}
	}
}
