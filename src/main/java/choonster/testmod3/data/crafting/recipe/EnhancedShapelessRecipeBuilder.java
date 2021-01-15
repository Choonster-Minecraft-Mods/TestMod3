package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapelessRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipeSerializer;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.ShapelessRecipe;
import net.minecraft.tags.ITag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

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
	private static final Method VALIDATE = ObfuscationReflectionHelper.findMethod(ShapelessRecipeBuilder.class, /* validate */ "func_200481_a", ResourceLocation.class);
	private static final Field ADVANCEMENT_BUILDER = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* advancementBuilder */ "field_200497_e");
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* group */ "field_200498_f");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, /* ingredients */ "field_200496_d");

	protected final ItemStack result;
	protected final IRecipeSerializer<? extends RECIPE> serializer;
	protected String itemGroup;

	protected EnhancedShapelessRecipeBuilder(final ItemStack result, final IRecipeSerializer<? extends RECIPE> serializer) {
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
	public BUILDER setItemGroup(final String group) {
		itemGroup = group;
		return (BUILDER) this;
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER addIngredient(final ITag<Item> tagIn) {
		return (BUILDER) super.addIngredient(tagIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER addIngredient(final IItemProvider itemIn) {
		return (BUILDER) super.addIngredient(itemIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER addIngredient(final IItemProvider itemIn, final int quantity) {
		return (BUILDER) super.addIngredient(itemIn, quantity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER addIngredient(final Ingredient ingredientIn) {
		return (BUILDER) super.addIngredient(ingredientIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER addIngredient(final Ingredient ingredientIn, final int quantity) {
		return (BUILDER) super.addIngredient(ingredientIn, quantity);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER addCriterion(final String name, final ICriterionInstance criterionIn) {
		return (BUILDER) super.addCriterion(name, criterionIn);
	}

	@SuppressWarnings("unchecked")
	@Override
	public BUILDER setGroup(final String groupIn) {
		return (BUILDER) super.setGroup(groupIn);
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 *
	 * @param consumer The recipe consumer
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer) {
		build(consumer, RegistryUtil.getRequiredRegistryName(result.getItem()));
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
	 * the result.
	 *
	 * @param consumer The recipe consumer
	 * @param save     The ID to use for the recipe
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final String save) {
		final ResourceLocation resourcelocation = result.getItem().getRegistryName();
		if (new ResourceLocation(save).equals(resourcelocation)) {
			throw new IllegalStateException("Enhanced Shapeless Recipe " + save + " should remove its 'save' argument");
		} else {
			build(consumer, new ResourceLocation(save));
		}
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 *
	 * @param consumer The recipe consumer
	 * @param id       The ID to use for the recipe
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			VALIDATE.invoke(this, id);

			// Perform our validation
			validate(id);

			// We can't call the super method directly because it throws an exception when the result is an item that
			// doesn't belong to an item group (e.g. Mob Spawners).

			final Advancement.Builder advancementBuilder = ((Advancement.Builder) ADVANCEMENT_BUILDER.get(this))
					.withParentId(new ResourceLocation("minecraft", "recipes/root"))
					.withCriterion("has_the_recipe", RecipeUnlockedTrigger.create(id))
					.withRewards(AdvancementRewards.Builder.recipe(id))
					.withRequirementsStrategy(IRequirementsStrategy.OR);

			String group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			final List<Ingredient> ingredients = getIngredients();

			String itemGroupName = itemGroup;
			if (itemGroupName == null) {
				final ItemGroup itemGroup = Preconditions.checkNotNull(result.getItem().getGroup());
				itemGroupName = itemGroup.getPath();
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
		if (itemGroup == null && result.getItem().getGroup() == null) {
			throw new IllegalStateException("Enhanced Shapeless Recipe " + id + " has result " + result + " with no item group - use EnhancedShapedRecipeBuilder.itemGroup to specify one");
		}
	}

	public static class Vanilla extends EnhancedShapelessRecipeBuilder<ShapelessRecipe, Vanilla> {
		private Vanilla(final ItemStack result) {
			super(result, IRecipeSerializer.CRAFTING_SHAPELESS);
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
