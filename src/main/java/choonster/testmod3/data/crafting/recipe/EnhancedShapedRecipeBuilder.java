package choonster.testmod3.data.crafting.recipe;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.ICriterionInstance;
import net.minecraft.advancements.IRequirementsStrategy;
import net.minecraft.advancements.criterion.RecipeUnlockedTrigger;
import net.minecraft.data.IFinishedRecipe;
import net.minecraft.data.ShapedRecipeBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.Tag;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * An extension of {@link ShapedRecipeBuilder} that allows the recipe result to have NBT and a custom group name for
 * the recipe advancement.
 *
 * @author Choonster
 */
public class EnhancedShapedRecipeBuilder extends ShapedRecipeBuilder {
	private static final Method VALIDATE = ObfuscationReflectionHelper.findMethod(ShapedRecipeBuilder.class, "func_200463_a" /* validate */, ResourceLocation.class);
	private static final Field ADVANCEMENT_BUILDER = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "field_200479_f" /* advancementBuilder */);
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "field_200480_g" /* group */);
	private static final Field PATTERN = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "field_200477_d" /* pattern */);
	private static final Field KEY = ObfuscationReflectionHelper.findField(ShapedRecipeBuilder.class, "field_200478_e" /* key */);

	private final ItemStack result;
	private String itemGroup;

	private EnhancedShapedRecipeBuilder(final ItemStack result) {
		super(result.getItem(), result.getCount());
		this.result = result;
	}

	/**
	 * Creates a new builder for a shaped recipe with NBT and/or a custom item group.
	 *
	 * @param result The recipe result
	 * @return The builder
	 */
	public static EnhancedShapedRecipeBuilder shapedRecipe(final ItemStack result) {
		return new EnhancedShapedRecipeBuilder(result);
	}

	/**
	 * Sets the item group name to use for the recipe advancement. This allows the result to be an item without an
	 * item group, e.g. minecraft:spawner.
	 *
	 * @param group The group name
	 * @return This builder
	 */
	public EnhancedShapedRecipeBuilder setItemGroup(final String group) {
		itemGroup = group;
		return this;
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@Override
	public EnhancedShapedRecipeBuilder key(final Character symbol, final Tag<Item> tagIn) {
		return (EnhancedShapedRecipeBuilder) super.key(symbol, tagIn);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@Override
	public EnhancedShapedRecipeBuilder key(final Character symbol, final IItemProvider itemIn) {
		return (EnhancedShapedRecipeBuilder) super.key(symbol, itemIn);
	}

	/**
	 * Adds a key to the recipe pattern.
	 */
	@Override
	public EnhancedShapedRecipeBuilder key(final Character symbol, final Ingredient ingredientIn) {
		return (EnhancedShapedRecipeBuilder) super.key(symbol, ingredientIn);
	}

	/**
	 * Adds a new entry to the patterns for this recipe.
	 */
	@Override
	public EnhancedShapedRecipeBuilder patternLine(final String pattern) {
		return (EnhancedShapedRecipeBuilder) super.patternLine(pattern);
	}

	/**
	 * Adds a criterion needed to unlock the recipe.
	 */
	@Override
	public EnhancedShapedRecipeBuilder addCriterion(final String name, final ICriterionInstance criterion) {
		return (EnhancedShapedRecipeBuilder) super.addCriterion(name, criterion);
	}

	@Override
	public EnhancedShapedRecipeBuilder setGroup(final String group) {
		return (EnhancedShapedRecipeBuilder) super.setGroup(group);
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer) {
		build(consumer, result.getItem().getRegistryName());
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}. Use {@link #build(Consumer)} if save is the same as the ID for
	 * the result.
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final String save) {
		final ResourceLocation registryName = result.getItem().getRegistryName();
		if (new ResourceLocation(save).equals(registryName)) {
			throw new IllegalStateException("Shaped Recipe " + save + " should remove its 'save' argument");
		} else {
			build(consumer, new ResourceLocation(save));
		}
	}

	/**
	 * Validates that the recipe result has NBT or a custom group has been specified.
	 *
	 * @param id The recipe ID
	 */
	private void validate(final ResourceLocation id) {
		if (!result.hasTag() && itemGroup == null) {
			throw new IllegalStateException("Enhanced Shaped Recipe " + id + " has no NBT and no custom item group - use ShapedRecipeBuilder instead");
		}

		if (itemGroup == null && result.getItem().getGroup() == null) {
			throw new IllegalStateException("Enhanced Shaped Recipe " + id + " has result " + result + " with no item group - use EnhancedShapedRecipeBuilder.itemGroup to specify one");
		}
	}

	/**
	 * Builds this recipe into an {@link IFinishedRecipe}.
	 */
	@Override
	public void build(final Consumer<IFinishedRecipe> consumer, final ResourceLocation id) {
		try {
			// Perform the super class's validation
			VALIDATE.invoke(this, id);

			// Perform our validation
			validate(id);

			final Advancement.Builder advancementBuilder = ((Advancement.Builder) ADVANCEMENT_BUILDER.get(this))
					.withParentId(new ResourceLocation("minecraft", "recipes/root"))
					.withCriterion("has_the_recipe", new RecipeUnlockedTrigger.Instance(id))
					.withRewards(AdvancementRewards.Builder.recipe(id))
					.withRequirementsStrategy(IRequirementsStrategy.OR);

			String group = (String) GROUP.get(this);
			if (group == null) {
				group = "";
			}

			@SuppressWarnings("unchecked")
			final List<String> pattern = (List<String>) PATTERN.get(this);

			@SuppressWarnings("unchecked")
			final Map<Character, Ingredient> key = (Map<Character, Ingredient>) KEY.get(this);

			String itemGroupName = itemGroup;
			if (itemGroupName == null) {
				final ItemGroup itemGroup = Preconditions.checkNotNull(result.getItem().getGroup());
				itemGroupName = itemGroup.getPath();
			}

			final ResourceLocation advancementID = new ResourceLocation(id.getNamespace(), "recipes/" + itemGroupName + "/" + id.getPath());

			consumer.accept(new Result(id, result, group, pattern, key, advancementBuilder, advancementID));
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to build Enhanced Shaped Recipe " + id, e);
		}
	}

	public class Result extends ShapedRecipeBuilder.Result {
		private final CompoundNBT resultNBT;

		private Result(final ResourceLocation id, final ItemStack result, final String group, final List<String> pattern, final Map<Character, Ingredient> key, final Advancement.Builder advancementBuilder, final ResourceLocation advancementID) {
			super(id, result.getItem(), result.getCount(), group, pattern, key, advancementBuilder, advancementID);
			resultNBT = result.getTag();
		}

		@Override
		public void serialize(final JsonObject json) {
			super.serialize(json);

			if (resultNBT != null) {
				json.getAsJsonObject("result")
						.addProperty("nbt", resultNBT.toString());
			}
		}
	}
}
