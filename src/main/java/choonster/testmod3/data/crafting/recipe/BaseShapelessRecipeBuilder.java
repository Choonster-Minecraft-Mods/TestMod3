package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.world.item.crafting.recipe.BaseShapelessRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeFactory;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeSerializer;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * An extensible wrapper of {@link ShapelessRecipeBuilder} for {@link BaseShapelessRecipe} classes.
 *
 * @author Choonster
 */
public abstract class BaseShapelessRecipeBuilder<
		RECIPE extends BaseShapelessRecipe,
		BUILDER extends BaseShapelessRecipeBuilder<RECIPE, BUILDER>
		> implements RecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, "ensureValid", ResourceKey.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "category");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "group");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "ingredients");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "criteria");

	protected final ShapelessRecipeBuilder innerBuilder;

	protected final ItemStack result;
	protected final ShapelessRecipeFactory<? extends RECIPE> factory;

	protected BaseShapelessRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStack result,
			final ShapelessRecipeFactory<? extends RECIPE> factory
	) {
		innerBuilder = ShapelessRecipeBuilder.shapeless(items, category, result);
		this.result = result;
		this.factory = factory;
	}

	protected BaseShapelessRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStack result,
			final ShapelessRecipeSerializer<? extends RECIPE> serializer
	) {
		this(items, category, result, serializer.factory());
	}

	public BUILDER requires(final TagKey<Item> tag) {
		innerBuilder.requires(tag);

		return builder();
	}

	public BUILDER requires(final ItemLike item) {
		innerBuilder.requires(item);

		return builder();
	}

	public BUILDER requires(final ItemLike item, final int quantity) {
		innerBuilder.requires(item, quantity);

		return builder();
	}

	public BUILDER requires(final Ingredient ingredient) {
		innerBuilder.requires(ingredient);

		return builder();
	}

	public BUILDER requires(final Ingredient ingredientIn, final int quantity) {
		innerBuilder.requires(ingredientIn, quantity);

		return builder();
	}

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
	public Item getResult() {
		return innerBuilder.getResult();
	}

	@SuppressWarnings("unchecked")
	private BUILDER builder() {
		return (BUILDER) this;
	}

	/**
	 * Saves this recipe to the {@link RecipeOutput}.
	 *
	 * @param output The recipe output
	 * @param key    The ID to use for the recipe
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceKey<Recipe<?>> key) {
		try {
			// Perform the Vanilla class's validation
			ENSURE_VALID.invoke(innerBuilder, key);

			// Perform our validation
			validate(key);

			final var advancement = output
					.advancement()
					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(key))
					.rewards(AdvancementRewards.Builder.recipe(key))
					.requirements(AdvancementRequirements.Strategy.OR);

			@SuppressWarnings("unchecked") final var criteria = (Map<String, Criterion<?>>) CRITERIA.get(innerBuilder);
			criteria.forEach(advancement::addCriterion);

			var group = (String) GROUP.get(innerBuilder);
			if (group == null) {
				group = "";
			}

			final var category = (RecipeCategory) CATEGORY.get(innerBuilder);

			final var ingredients = getIngredients();

			final var recipe = factory.createRecipe(
					group,
					RecipeBuilder.determineBookCategory(category),
					result,
					ingredients
			);

			output.accept(key, recipe, advancement.build(key.identifier().withPrefix("recipes/" + category.getFolderName() + "/")));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to save shapeless recipe " + key, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected List<Ingredient> getIngredients() {
		try {
			return (List<Ingredient>) INGREDIENTS.get(innerBuilder);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get shapeless recipe ingredients", e);
		}
	}

	protected void validate(final ResourceKey<Recipe<?>> key) {
	}
}
