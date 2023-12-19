package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.EnhancedShapedRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapedRecipeFactory;
import choonster.testmod3.world.item.crafting.recipe.ShapedRecipeSerializer;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * An extension of {@link ShapedRecipeBuilder} that allows the recipe result to have NBT if the recipe type supports it.
 *
 * @author Choonster
 */
public class EnhancedShapedRecipeBuilder<
		RECIPE extends ShapedRecipe,
		BUILDER extends EnhancedShapedRecipeBuilder<RECIPE, BUILDER>
		> extends ShapedRecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapedRecipeBuilder.class, /* ensureValid */ "m_126143_", ResourceLocation.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* category */ "f_243672_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* group */ "f_126111_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* criteria */ "f_291506_");
	private static final Field SHOW_NOTIFICATION = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* showNotification */ "f_271093_");

	protected final ItemStack result;
	protected final ShapedRecipeFactory<? extends RECIPE> factory;

	protected EnhancedShapedRecipeBuilder(
			final RecipeCategory category,
			final ItemStack result,
			final ShapedRecipeFactory<? extends RECIPE> factory
	) {
		super(category, result.getItem(), result.getCount());
		this.result = result;
		this.factory = factory;
	}

	protected EnhancedShapedRecipeBuilder(
			final RecipeCategory category,
			final ItemStack result,
			final ShapedRecipeSerializer<? extends RECIPE> serializer
	) {
		this(category, result, serializer.factory());
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER define(final Character symbol, final TagKey<Item> tagIn) {
		return (BUILDER) super.define(symbol, tagIn);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER define(final Character symbol, final ItemLike itemIn) {
		return (BUILDER) super.define(symbol, itemIn);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER define(final Character symbol, final Ingredient ingredientIn) {
		return (BUILDER) super.define(symbol, ingredientIn);
	}

	/**
	 * Adds a new entry to the patterns for this recipe.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER pattern(final String pattern) {
		return (BUILDER) super.pattern(pattern);
	}

	/**
	 * Adds a criterion needed to unlock the recipe.
	 */
	@SuppressWarnings("unchecked")
	@Override
	public BUILDER unlockedBy(final String name, final Criterion<?> criterion) {
		return (BUILDER) super.unlockedBy(name, criterion);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER group(@Nullable final String group) {
		return (BUILDER) super.group(group);
	}

	/**
	 * Override to validate the recipe's ingredients, result or other conditions.
	 *
	 * @param id The recipe ID
	 */
	protected void ensureValid(final ResourceLocation id) {
	}

	/**
	 * Saves this recipe to the {@link RecipeOutput}.
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			final var pattern = (ShapedRecipePattern) ENSURE_VALID.invoke(this, id);

			// Perform our validation
			ensureValid(id);

			final var advancement = output.advancement()
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

			final var showNotification = (boolean) SHOW_NOTIFICATION.get(this);

			final var recipe = factory.createRecipe(
					group,
					RecipeBuilder.determineBookCategory(category),
					pattern,
					result,
					showNotification
			);

			output.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + category.getFolderName() + "/")));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to save shaped recipe " + id, e);
		}
	}

	public static class Enhanced extends EnhancedShapedRecipeBuilder<EnhancedShapedRecipe, Enhanced> {
		private Enhanced(final RecipeCategory category, final ItemStack result) {
			super(category, result, ModCrafting.Recipes.ENHANCED_SHAPED.get());
		}

		/**
		 * Creates a new builder for a basic shaped recipe with NBT.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Enhanced shapedRecipe(final RecipeCategory category, final ItemStack result) {
			return new Enhanced(category, result);
		}

		@Override
		protected void ensureValid(final ResourceLocation id) {
			super.ensureValid(id);

			if (!result.hasTag()) {
				throw new IllegalStateException("Enhanced shaped recipe " + id + " has no NBT - use ShapedRecipeBuilder instead");
			}
		}
	}
}
