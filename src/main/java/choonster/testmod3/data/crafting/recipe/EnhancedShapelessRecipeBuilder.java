package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeFactory;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeSerializer;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.core.NonNullList;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * An extension of {@link ShapelessRecipeBuilder} that allows the recipe result to have NBT if the recipe type supports it.
 *
 * @author Choonster
 */
public abstract class EnhancedShapelessRecipeBuilder<
		RECIPE extends ShapelessRecipe,
		BUILDER extends EnhancedShapelessRecipeBuilder<RECIPE, BUILDER>
		> extends ShapelessRecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, /* ensureValid */ "m_126207_", ResourceLocation.class);
	private static final Field CATEGORY = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* category */ "f_244182_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* group */ "f_126177_");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* ingredients */ "f_126175_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* criteria */ "f_291209_");

	protected final ItemStack result;
	protected final ShapelessRecipeSerializer<? extends RECIPE> serializer;
	protected final ShapelessRecipeFactory<? extends RECIPE> factory;

	protected EnhancedShapelessRecipeBuilder(
			final RecipeCategory category,
			final ItemStack result,
			final ShapelessRecipeSerializer<? extends RECIPE> serializer
	) {
		super(category, result.getItem(), result.getCount());
		this.result = result;
		this.serializer = serializer;
		factory = serializer.factory();
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
	 * Saves this recipe to the {@link RecipeOutput}.
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

			final var recipe = factory.createRecipe(
					group,
					RecipeBuilder.determineBookCategory(category),
					result,
					ingredients
			);

			output.accept(id, recipe, advancement.build(id.withPrefix("recipes/" + category.getFolderName() + "/")));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to save shapeless recipe " + id, e);
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

	protected void validate(final ResourceLocation id) {
	}

	public static class Enhanced extends EnhancedShapelessRecipeBuilder<ShapelessRecipe, Enhanced> {
		private Enhanced(final RecipeCategory category, final ItemStack result) {
			super(category, result, ModCrafting.Recipes.ENHANCED_SHAPELESS.get());
		}

		/**
		 * Creates a new builder for a basic shapeless recipe with NBT.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Enhanced shapelessRecipe(final RecipeCategory category, final ItemStack result) {
			return new Enhanced(category, result);
		}

		@Override
		protected void validate(final ResourceLocation id) {
			super.validate(id);

			if (!result.hasTag()) {
				throw new IllegalStateException("Enhanced shapeless recipe " + id + " has no NBT - use ShapelessRecipeBuilder instead");
			}
		}
	}
}
