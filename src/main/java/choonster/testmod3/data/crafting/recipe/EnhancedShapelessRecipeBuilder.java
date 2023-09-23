package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * An extension of {@link ShapelessRecipeBuilder} that allows the recipe result to have NBT.
 *
 * @author Choonster
 */
public class EnhancedShapelessRecipeBuilder<
		RECIPE extends ShapelessRecipe,
		BUILDER extends EnhancedShapelessRecipeBuilder<RECIPE, BUILDER>
		> extends ShapelessRecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, /* ensureValid */ "m_126207_", ResourceLocation.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* category */ "f_244182_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* group */ "f_126177_");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* ingredients */ "f_126175_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* criteria */ "f_291209_");

	protected final ItemStack result;
	protected final RecipeSerializer<? extends RECIPE> serializer;
	@Nullable
	protected String itemGroup;

	protected EnhancedShapelessRecipeBuilder(final RecipeCategory category, final ItemStack result, final RecipeSerializer<? extends RECIPE> serializer) {
		super(category, result.getItem(), result.getCount());
		this.result = result;
		this.serializer = serializer;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final TagKey<Item> tagIn) {
		return (BUILDER) super.requires(tagIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final ItemLike itemIn) {
		return (BUILDER) super.requires(itemIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final ItemLike itemIn, final int quantity) {
		return (BUILDER) super.requires(itemIn, quantity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final Ingredient ingredientIn) {
		return (BUILDER) super.requires(ingredientIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER requires(final Ingredient ingredientIn, final int quantity) {
		return (BUILDER) super.requires(ingredientIn, quantity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER unlockedBy(final String name, final Criterion<?> criterionIn) {
		return (BUILDER) super.unlockedBy(name, criterionIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER group(@Nullable final String group) {
		return (BUILDER) super.group(group);
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 *
	 * @param output The recipe output
	 */
	@Override
	public void save(final RecipeOutput output) {
		save(output, RegistryUtil.getKey(result.getItem()));
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}. Use {@link #save(RecipeOutput)} if save is the same as the ID for
	 * the result.
	 *
	 * @param output The recipe output
	 * @param save   The ID to use for the recipe
	 */
	@Override
	public void save(final RecipeOutput output, final String save) {
		final var key = RegistryUtil.getKey(result.getItem());
		if (new ResourceLocation(save).equals(key)) {
			throw new IllegalStateException("Enhanced Shapeless Recipe " + save + " should remove its 'save' argument");
		} else {
			save(output, new ResourceLocation(save));
		}
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 *
	 * @param output The recipe output
	 * @param id     The ID to use for the recipe
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			ENSURE_VALID.invoke(this, id);

			// Perform our validation
			validate(id);

			final var advancement = output
					.advancement()
					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
					.rewards(AdvancementRewards.Builder.recipe(id))
					.requirements(AdvancementRequirements.Strategy.OR);

			@SuppressWarnings("unchecked") final var criteria = (Map<String, Criterion<?>>) CRITERIA.get(this);
			criteria.forEach(advancement::addCriterion);

			var group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			final var category = (RecipeCategory) CATEGORY.get(this);

			final var ingredients = getIngredients();

			final var baseRecipe = new Result(
					id,
					result.getItem(),
					result.getCount(),
					group,
					determineBookCategory(category),
					ingredients,
					advancement.build(id.withPrefix("recipes/" + category.getFolderName() + "/"))
			);

			output.accept(new SimpleFinishedRecipe(baseRecipe, result, serializer));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to build Enhanced Shapeless Recipe " + id, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Ingredient> getIngredients() {
		try {
			return (List<Ingredient>) INGREDIENTS.get(this);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get shapeless recipe ingredients", e);
		}
	}

	protected void validate(final ResourceLocation id) {
	}

	public static class Vanilla extends EnhancedShapelessRecipeBuilder<ShapelessRecipe, Vanilla> {
		private Vanilla(final RecipeCategory category, final ItemStack result) {
			super(category, result, RecipeSerializer.SHAPELESS_RECIPE);
		}

		/**
		 * Creates a new builder for a Vanilla shapeless recipe with NBT and/or a custom item group.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Vanilla shapelessRecipe(final RecipeCategory category, final ItemStack result) {
			return new Vanilla(category, result);
		}

		@Override
		protected void validate(final ResourceLocation id) {
			super.validate(id);

			if (!result.hasTag() && itemGroup == null) {
				throw new IllegalStateException("Vanilla Shapeless Recipe " + id + " has no NBT and no custom item group - use ShapelessRecipeBuilder instead");
			}
		}
	}
}
