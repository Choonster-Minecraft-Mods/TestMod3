package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.EnhancedShapedRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapedRecipeFactory;
import choonster.testmod3.world.item.crafting.recipe.ShapedRecipeSerializer;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
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
 * An extension of {@link ShapedRecipeBuilder} that allows the recipe result to have components.
 *
 * @author Choonster
 */
public class EnhancedShapedRecipeBuilder<
		RECIPE extends ShapedRecipe,
		BUILDER extends EnhancedShapedRecipeBuilder<RECIPE, BUILDER>
		> implements RecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapedRecipeBuilder.class, /* ensureValid */ "m_126143_", ResourceLocation.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* category */ "f_243672_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* group */ "f_126111_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* criteria */ "f_291506_");
	private static final Field SHOW_NOTIFICATION = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* showNotification */ "f_271093_");

	protected final ShapedRecipeBuilder innerBuilder;

	protected final ItemStack result;
	protected final ShapedRecipeFactory<? extends RECIPE> factory;

	protected EnhancedShapedRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStack result,
			final ShapedRecipeFactory<? extends RECIPE> factory
	) {
		innerBuilder = ShapedRecipeBuilder.shaped(items, category, result.getItem(), result.getCount());
		this.result = result;
		this.factory = factory;
	}

	protected EnhancedShapedRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStack result,
			final ShapedRecipeSerializer<? extends RECIPE> serializer
	) {
		this(items, category, result, serializer.factory());
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
	public Item getResult() {
		return innerBuilder.getResult();
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
			// Perform the Vanilla class's validation
			final var pattern = (ShapedRecipePattern) ENSURE_VALID.invoke(innerBuilder, id);

			// Perform our validation
			ensureValid(id);

			final var advancement = output.advancement()
					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
					.rewards(AdvancementRewards.Builder.recipe(id))
					.requirements(AdvancementRequirements.Strategy.OR);

			@SuppressWarnings("unchecked") final var criteria = (Map<String, Criterion<?>>) CRITERIA.get(innerBuilder);
			criteria.forEach(advancement::addCriterion);

			var group = (String) GROUP.get(innerBuilder);
			if (group == null) {
				group = "";
			}

			final var category = (RecipeCategory) CATEGORY.get(innerBuilder);

			final var showNotification = (boolean) SHOW_NOTIFICATION.get(innerBuilder);

			final var recipe = factory.createRecipe(
					group,
					RecipeBuilder.determineBookCategory(category),
					pattern,
					result,
					showNotification
			);

			output.accept(
					id,
					recipe,
					advancement.build(id.location().withPrefix("recipes/" + category.getFolderName() + "/"))
			);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to save shaped recipe " + id, e);
		}
	}

	public static class Enhanced extends EnhancedShapedRecipeBuilder<EnhancedShapedRecipe, Enhanced> {
		private Enhanced(final HolderGetter<Item> items, final RecipeCategory category, final ItemStack result) {
			super(items, category, result, ModCrafting.Recipes.ENHANCED_SHAPED.get());
		}

		/**
		 * Creates a new builder for a basic shaped recipe with NBT.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Enhanced shapedRecipe(final HolderGetter<Item> items, final RecipeCategory category, final ItemStack result) {
			return new Enhanced(items, category, result);
		}

		@Override
		protected void ensureValid(final ResourceKey<Recipe<?>> key) {
			super.ensureValid(key);

			final var allComponentsAreStandard = result.getComponents()
					.stream()
					.allMatch(typedComponent -> typedComponent.value().equals(DataComponents.COMMON_ITEM_COMPONENTS.get(typedComponent.type())));

			if (!allComponentsAreStandard) {
				throw new IllegalStateException("Enhanced shaped recipe " + key + " has no custom components - use ShapedRecipeBuilder instead");
			}
		}
	}
}
