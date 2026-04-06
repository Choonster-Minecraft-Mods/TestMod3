package choonster.testmod3.data.crafting.recipe;

import choonster.testmod3.world.item.crafting.recipe.BaseShapelessRecipe;
import choonster.testmod3.world.item.crafting.recipe.ShapelessRecipeFactory;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jspecify.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.List;

/**
 * An extensible wrapper of {@link ShapelessRecipeBuilder} for {@link BaseShapelessRecipe} classes.
 *
 * @author Choonster
 */
public abstract class BaseShapelessRecipeBuilder<
		RECIPE extends BaseShapelessRecipe,
		BUILDER extends BaseShapelessRecipeBuilder<RECIPE, BUILDER>
		> implements RecipeBuilder {
	private static final Field GROUP = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "group");
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "ingredients");
	private static final Field ADVANCEMENT_BUILDER = ObfuscationReflectionHelper.findField(ShapelessRecipeBuilder.class, "advancementBuilder");

	protected final ShapelessRecipeBuilder innerBuilder;

	protected final RecipeCategory category;
	protected final ItemStackTemplate result;
	protected final ShapelessRecipeFactory<? extends RECIPE> factory;

	protected BaseShapelessRecipeBuilder(
			final HolderGetter<Item> items,
			final RecipeCategory category,
			final ItemStackTemplate result,
			final ShapelessRecipeFactory<? extends RECIPE> factory
	) {
		innerBuilder = ShapelessRecipeBuilder.shapeless(items, category, result);

		this.category = category;
		this.result = result;
		this.factory = factory;
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
	public ResourceKey<Recipe<?>> defaultId() {
		return RecipeBuilder.getDefaultRecipeId(result);
	}

	@SuppressWarnings("unchecked")
	private BUILDER builder() {
		return (BUILDER) this;
	}

	/**
	 * Saves this recipe to the {@link RecipeOutput}.
	 *
	 * @param output The recipe output
	 * @param id     The ID to use for the recipe
	 */
	@Override
	public void save(final RecipeOutput output, final ResourceKey<Recipe<?>> id) {
		try {
			// Perform our validation
			validate(id);

			final var group = (String) GROUP.get(innerBuilder);

			final var ingredients = getIngredients();

			final var craftingBookInfo = RecipeBuilder.createCraftingBookInfo(category, group);

			final var recipe = factory.createRecipe(
					RecipeBuilder.createCraftingCommonInfo(true),
					craftingBookInfo,
					result,
					ingredients
			);

			final var advancementBuilder = (RecipeUnlockAdvancementBuilder) ADVANCEMENT_BUILDER.get(innerBuilder);

			output.accept(id, recipe, advancementBuilder.build(output, id, category));
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to save shapeless recipe " + id, e);
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
