package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.crafting.recipe.ShapedArmourUpgradeRecipe;
import choonster.testmod3.init.ModCrafting;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

/**
 * Builder for {@link ShapedArmourUpgradeRecipe}.
 *
 * @author Choonster
 */
public class ShapedArmourUpgradeRecipeBuilder extends ShapedRecipeBuilder {
	private final Item result;

	private ShapedArmourUpgradeRecipeBuilder(final IItemProvider result, final int count) {
		super(result, count);
		this.result = result.asItem();
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param result The recipe result
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(final IItemProvider result) {
		return shapedArmourUpgradeRecipe(result, 1);
	}

	/**
	 * Creates a new builder for a shaped armour upgrade recipe.
	 *
	 * @param result The recipe result
	 * @param count  The recipe result count
	 * @return The builder
	 */
	public static ShapedArmourUpgradeRecipeBuilder shapedArmourUpgradeRecipe(final IItemProvider result, final int count) {
		return new ShapedArmourUpgradeRecipeBuilder(result, count);
	}


	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer) {
		build(consumer, result.getRegistryName());
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
	 * the result.
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
	 * Validates that the recipe result is damageable.
	 *
	 * @param id The recipe ID
	 */
	private void validate(final ResourceLocation id) {
		if (!result.isDamageable()) {
			throw new IllegalStateException("Shaped Armour Upgrade Recipe " + id + " must have damageable result");
		}
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final ResourceLocation id) {
		// Use an AtomicReference to capture the IFinishedRecipe created by the super method
		final AtomicReference<IFinishedRecipe> baseResult = new AtomicReference<>();
		super.build(baseResult::set, id);

		validate(id);
		consumer.accept(new Result(baseResult.get()));
	}

	public static class Result extends FinishedRecipeDelegate {
		private Result(final IFinishedRecipe baseResult) {
			super(baseResult);
		}

		@Override
		public IRecipeSerializer<?> getSerializer() {
			return ModCrafting.Recipes.ARMOUR_UPGRADE_SHAPED;
		}
	}
}
