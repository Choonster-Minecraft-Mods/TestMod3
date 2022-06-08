package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.advancements.RequirementsStrategy;
import net.minecraft.advancements.critereon.RecipeUnlockedTrigger;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Consumer;

/**
 * An extension of {@link ShapelessRecipeBuilder} that allows the recipe result to have NBT and a custom group name for
 * the recipe advancement.
 *
 * @author Choonster
 */
public class EnhancedShapelessRecipeBuilder<
		RECIPE extends ShapelessRecipe,
		BUILDER extends EnhancedShapelessRecipeBuilder<RECIPE, BUILDER>
		> extends ShapelessRecipeBuilder {
	private static final Method ENSURE_VALID = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, /* ensureValid */ "m_126207_", ResourceLocation.class);
	private static final Field ADVANCEMENT = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* advancement */ "f_126176_");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* group */ "f_126177_");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* ingredients */ "f_126175_");

	protected final ItemStack result;
	protected final RecipeSerializer<? extends RECIPE> serializer;
	protected String itemGroup;

	protected EnhancedShapelessRecipeBuilder(final ItemStack result, final RecipeSerializer<? extends RECIPE> serializer) {
		super(result.getItem(), result.getCount());
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
	public BUILDER unlockedBy(final String name, final CriterionTriggerInstance criterionIn) {
		return (BUILDER) super.unlockedBy(name, criterionIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER group(final String group) {
		return (BUILDER) super.group(group);
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 *
	 * @param consumer The recipe consumer
	 */
	@Override
	public void save(final Consumer<FinishedRecipe> consumer) {
		save(consumer, RegistryUtil.getKey(result.getItem()));
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}. Use {@link #save(Consumer)} if save is the same as the ID for
	 * the result.
	 *
	 * @param consumer The recipe consumer
	 * @param save     The ID to use for the recipe
	 */
	@Override
	public void save(final Consumer<FinishedRecipe> consumer, final String save) {
		final ResourceLocation key = RegistryUtil.getKey(result.getItem());
		if (new ResourceLocation(save).equals(key)) {
			throw new IllegalStateException("Enhanced Shapeless Recipe " + save + " should remove its 'save' argument");
		} else {
			save(consumer, new ResourceLocation(save));
		}
	}

	/**
	 * Builds this recipe into a {@link FinishedRecipe}.
	 *
	 * @param consumer The recipe consumer
	 * @param id       The ID to use for the recipe
	 */
	@Override
	public void save(final Consumer<FinishedRecipe> consumer, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			ENSURE_VALID.invoke(this, id);

			// Perform our validation
			validate(id);

			// We can't call the super method directly because it throws an exception when the result is an item that
			// doesn't belong to an item group (e.g. Mob Spawners).

			final Advancement.Builder advancementBuilder = ((Advancement.Builder) ADVANCEMENT.get(this))
					.parent(new ResourceLocation("minecraft", "recipes/root"))
					.addCriterion("has_the_recipe", RecipeUnlockedTrigger.unlocked(id))
					.rewards(AdvancementRewards.Builder.recipe(id))
					.requirements(RequirementsStrategy.OR);

			String group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			final List<Ingredient> ingredients = getIngredients();

			String itemGroupName = itemGroup;
			if (itemGroupName == null) {
				final CreativeModeTab itemGroup = Preconditions.checkNotNull(result.getItem().getItemCategory());
				itemGroupName = itemGroup.getRecipeFolderName();
			}

			final ResourceLocation advancementID = new ResourceLocation(id.getNamespace(), "recipes/" + itemGroupName + "/" + id.getPath());

			final Result baseRecipe = new Result(id, result.getItem(), result.getCount(), group, ingredients, advancementBuilder, advancementID);

			consumer.accept(new SimpleFinishedRecipe(baseRecipe, result, serializer));
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
		if (itemGroup == null && result.getItem().getItemCategory() == null) {
			throw new IllegalStateException("Enhanced Shapeless Recipe " + id + " has result " + result + " with no item group - use EnhancedShapedRecipeBuilder.itemGroup to specify one");
		}
	}

	public static class Vanilla extends EnhancedShapelessRecipeBuilder<ShapelessRecipe, Vanilla> {
		private Vanilla(final ItemStack result) {
			super(result, RecipeSerializer.SHAPELESS_RECIPE);
		}

		/**
		 * Creates a new builder for a Vanilla shapeless recipe with NBT and/or a custom item group.
		 *
		 * @param result The recipe result
		 * @return The builder
		 */
		public static Vanilla shapelessRecipe(final ItemStack result) {
			return new Vanilla(result);
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
