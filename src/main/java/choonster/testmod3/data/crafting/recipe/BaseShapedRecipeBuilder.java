package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.world.item.crafting.recipe.ShapedRecipeFactory;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.common.crafting.IShapedRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 *
 * An extensible wrapper of {@link ShapedRecipeBuilder} for {@link choonster.testmod3.world.item.crafting.recipe.BaseShapedRecipe} classes.
 *
 * @author Choonster
 */
public class BaseShapedRecipeBuilder<
		RECIPE extends IShapedRecipe<?>,
		BUILDER extends BaseShapedRecipeBuilder<RECIPE, BUILDER>
		> implements RecipeBuilder {
	private static final Field ROWS = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "rows");
	private static final Field KEY = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "key");
	private static final Field ADVANCEMENT_BUILDER = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "advancementBuilder");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "group");
	private static final Field SHOW_NOTIFICATION = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "showNotification");
	protected final ShapedRecipeBuilder innerBuilder;

	protected final ItemStackTemplate result;
	protected final RecipeCategory category;
	protected final ShapedRecipeFactory<? extends RECIPE> factory;

	protected BaseShapedRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result,
			final ShapedRecipeFactory<? extends RECIPE> factory
	) {
		innerBuilder = ShapedRecipeBuilder.shaped(items, category, result.item().get(), result.count());

		this.category = category;
		this.result = result;
		this.factory = factory;
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public BUILDER define(final Character symbol, final TagKey<Item> tag) {
		innerBuilder.define(symbol, tag);

		return builder();
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public BUILDER define(final Character symbol, final ItemLike item) {
		innerBuilder.define(symbol, item);

		return builder();
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	public BUILDER define(final Character symbol, final Ingredient ingredient) {
		innerBuilder.define(symbol, ingredient);

		return builder();
	}

	/**
	 * Adds a new entry to the patterns for this recipe.
	 */
	public BUILDER pattern(final String pattern) {
		innerBuilder.pattern(pattern);

		return builder();
	}

	/**
	 * Adds a criterion needed to unlock the recipe.
	 */
	@Override
	public BUILDER unlockedBy(final String name, final Criterion<?> criterion) {
		innerBuilder.unlockedBy(name, criterion);

		return builder();
	}

	@Override
	public BUILDER group(@Nullable final String group) {
		innerBuilder.group(group);

		return builder();
	}

	@Override
	public ResourceKey<Recipe<?>> defaultId() {
		return RecipeBuilder.getDefaultRecipeId(result);
	}

	/**
	 * Override to validate the recipe's ingredients, result or other conditions.
	 *
	 * @param key The recipe ID
	 */
	protected void ensureValid(final ResourceKey<Recipe<?>> key) {
	}

	@SuppressWarnings("unchecked")
	private BUILDER builder() {
		return (BUILDER) this;
	}

	/**
	 * Saves this recipe to the {@link RecipeOutput}.
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceKey<Recipe<?>> id) {
		try {
			// Perform our validation
			ensureValid(id);

			final var showNotification = (boolean) SHOW_NOTIFICATION.get(innerBuilder);
			final var commonInfo = RecipeBuilder.createCraftingCommonInfo(showNotification);

			final var group = (String) GROUP.get(innerBuilder);
			final var bookInfo = RecipeBuilder.createCraftingBookInfo(category, group);

			@SuppressWarnings("unchecked") final var key = (Map<Character, Ingredient>) KEY.get(innerBuilder);
			@SuppressWarnings("unchecked") final var rows = (List<String>) ROWS.get(innerBuilder);
			final var pattern = ShapedRecipePattern.of(key, rows);

			final var recipe = factory.createRecipe(commonInfo, bookInfo, pattern, result);

			final var advancementBuilder = (RecipeUnlockAdvancementBuilder) ADVANCEMENT_BUILDER.get(innerBuilder);

			output.accept(
					id,
					recipe,
					advancementBuilder.build(output, id, category)
			);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to save shaped recipe " + id, e);
		}
	}

	/**
	 * An extension of {@link ShapedRecipeBuilder} that allows the recipe result to have components.
	 */
	public static class Shaped extends BaseShapedRecipeBuilder<ShapedRecipe, Shaped> {
		private Shaped(final HolderGetter<Item> items, final RecipeCategory category, final ItemStackTemplate result) {
			super(items, category, result, ShapedRecipe::new);
		}

		/**
		 * Creates a new builder for a basic shaped recipe with components.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Shaped shapedRecipe(
				final HolderGetter<Item> items,
				final RecipeCategory category,
				final ItemStackTemplate result
		) {
			return new Shaped(items, category, result);
		}

		@Override
		protected void ensureValid(final ResourceKey<Recipe<?>> key) {
			super.ensureValid(key);

			if (result.components().isEmpty()) {
				throw new IllegalStateException(
						"Enhanced shaped recipe " + key + " has no custom components - use ShapedRecipeBuilder instead"
				);
			}
		}
	}
}
