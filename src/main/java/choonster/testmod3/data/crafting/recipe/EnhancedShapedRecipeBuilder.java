package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.Criterion;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

/**
 * An extension of {@link ShapedRecipeBuilder} that allows the recipe result to have NBT.
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
	private static final Field ROWS = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* rows */ "f_126108_");
	private static final Field KEY = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* key */ "f_126109_");
	private static final Field CRITERIA = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* criteria */ "f_291506_");
	private static final Field SHOW_NOTIFICATION = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, /* showNotification */ "f_271093_");

	protected final ItemStack result;
	protected final RecipeSerializer<? extends RECIPE> serializer;
	@Nullable
	protected String itemGroup;

	protected EnhancedShapedRecipeBuilder(final RecipeCategory category, final ItemStack result, final RecipeSerializer<? extends RECIPE> serializer) {
		super(category, result.getItem(), result.getCount());
		this.result = result;
		this.serializer = serializer;
	}

	/**
	 * Sets the item group name to use for the recipe advancement. This allows the result to be an item without an
	 * item group, e.g. minecraft:spawner.
	 *
	 * @param group The group name
	 * @return This builder
	 */
	@SuppressWarnings("unchecked")
	public BUILDER itemGroup(final String group) {
		itemGroup = group;
		return (BUILDER) this;
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
	 * Builds this recipe into a {@link FinishedRecipe}.
	 */
	@Override
	public void save(final RecipeOutput output) {
		final var item = result.getItem();
		save(output, RegistryUtil.getKey(item));
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}. Use {@link #save(RecipeOutput)} if {@code save} is the same as the ID for
	 * the result.
	 */
	@Override
	public void save(final RecipeOutput output, final String save) {
		final var key = RegistryUtil.getKey(result.getItem());
		if (new ResourceLocation(save).equals(key)) {
			throw new IllegalStateException("Shaped Recipe " + save + " should remove its 'save' argument");
		} else {
			save(output, new ResourceLocation(save));
		}
	}

	/**
	 * Override to validate the recipe's ingredients, result or other conditions.
	 *
	 * @param id The recipe ID
	 */
	protected void ensureValid(final ResourceLocation id) {
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			ENSURE_VALID.invoke(this, id);

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

			@SuppressWarnings("unchecked") final var rows = (List<String>) ROWS.get(this);

			@SuppressWarnings("unchecked") final var key = (Map<Character, Ingredient>) KEY.get(this);

			final var showNotification = (boolean) SHOW_NOTIFICATION.get(this);

			final var baseRecipe = new Result(id,
					result.getItem(),
					result.getCount(),
					group,
					determineBookCategory(category),
					rows,
					key,
					advancement.build(id.withPrefix("recipes/" + category.getFolderName() + "/")),
					showNotification
			);

			output.accept(new SimpleFinishedRecipe(baseRecipe, result, serializer));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to build Enhanced Shaped Recipe " + id, e);
		}
	}

	public static class Vanilla extends EnhancedShapedRecipeBuilder<ShapedRecipe, Vanilla> {
		private Vanilla(final RecipeCategory category, final ItemStack result) {
			super(category, result, RecipeSerializer.SHAPED_RECIPE);
		}

		/**
		 * Creates a new builder for a Vanilla shaped recipe with NBT and/or a custom item group.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Vanilla shapedRecipe(final RecipeCategory category, final ItemStack result) {
			return new Vanilla(category, result);
		}

		@Override
		protected void ensureValid(final ResourceLocation id) {
			super.ensureValid(id);

			if (!result.hasTag() && itemGroup == null) {
				throw new IllegalStateException("Vanilla Shaped Recipe " + id + " has no NBT and no custom item group - use ShapedRecipeBuilder instead");
			}
		}
	}
}
