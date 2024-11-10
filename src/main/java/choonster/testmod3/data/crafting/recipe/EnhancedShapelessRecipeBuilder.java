package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeFactory;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeSerializer;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * An extension of {@link ShapelessRecipeBuilder} that allows the recipe result to have components.
 *
 * @author Choonster
 */
public abstract class EnhancedShapelessRecipeBuilder<
		RECIPE extends ShapelessRecipe,
		BUILDER extends EnhancedShapelessRecipeBuilder<RECIPE, BUILDER>
		> implements RecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, /* ensureValid */ "m_126207_", ResourceLocation.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* category */ "f_244182_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* group */ "f_126177_");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* ingredients */ "f_126175_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* criteria */ "f_291209_");

	protected final ShapelessRecipeBuilder innerBuilder;

	protected final ItemStack result;
	protected final ShapelessRecipeFactory<? extends RECIPE> factory;

	protected EnhancedShapelessRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStack result,
			final ShapelessRecipeFactory<? extends RECIPE> factory
	) {
		innerBuilder = ShapelessRecipeBuilder.shapeless(items, category, result);
		this.result = result;
		this.factory = factory;
	}

	protected EnhancedShapelessRecipeBuilder(
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
			// Perform the super class's validation
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

			var group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			final var category = (RecipeCategory) CATEGORY.get(this);

			final var ingredients = getIngredients();

			final var recipe = factory.createRecipe(
					group,
					RecipeBuilder.determineBookCategory(category),
					result,
					ingredients
			);

			output.accept(key, recipe, advancement.build(key.location().withPrefix("recipes/" + category.getFolderName() + "/")));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to save shapeless recipe " + key, e);
		}
	}

	@SuppressWarnings("unchecked")
	protected NonNullList<Ingredient> getIngredients() {
		try {
			return (NonNullList<Ingredient>) INGREDIENTS.get(this);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get shapeless recipe ingredients", e);
		}
	}

	protected void validate(final ResourceKey<Recipe<?>> key) {
	}

	public static class Enhanced extends EnhancedShapelessRecipeBuilder<ShapelessRecipe, Enhanced> {
		private Enhanced(final HolderGetter<Item> items, final RecipeCategory category, final ItemStack result) {
			super(items, category, result, ModCrafting.Recipes.ENHANCED_SHAPELESS.get());
		}

		/**
		 * Creates a new builder for a basic shapeless recipe with NBT.
		 *
		 * @param items  The item registry
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Enhanced shapelessRecipe(final HolderGetter<Item> items, final RecipeCategory category, final ItemStack result) {
			return new Enhanced(items, category, result);
		}

		@Override
		protected void validate(final ResourceKey<Recipe<?>> key) {
			super.validate(key);

			final var allComponentsAreStandard = result.getComponents()
					.stream()
					.allMatch(typedComponent -> typedComponent.value().equals(DataComponents.COMMON_ITEM_COMPONENTS.get(typedComponent.type())));

			if (!allComponentsAreStandard) {
				throw new IllegalStateException("Enhanced shapeless recipe " + key + " has no custom components - use ShapedRecipeBuilder instead");
			}
		}
	}
}
