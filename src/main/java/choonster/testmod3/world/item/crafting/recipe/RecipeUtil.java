package choonster.testmod3.world.item.crafting.recipe;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.apache.commons.lang3.NotImplementedException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.function.Function;

/**
 * Utility methods for {@link Recipe Recipes}.
 *
 * @author Choonster
 */
public class RecipeUtil {
	private static final Method SHRINK = ObfuscationReflectionHelper.findMethod(ShapedRecipe.class, /* shrink */ "m_44186_", List.class);
	private static final Field MAX_WIDTH = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_WIDTH");
	private static final Field MAX_HEIGHT = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_HEIGHT");

	/**
	 * Creates a {@link Codec} for a {@link ShapedRecipe}.
	 * <p>
	 * Adapted from {@link ShapedRecipe.Serializer}
	 *
	 * @param factory The recipe factory
	 * @param <T>     The recipe type
	 * @return The codec
	 */
	public static <T extends ShapedRecipe> Codec<T> shapedRecipeCodec(final ShapedRecipeFactory<T> factory) {
		return ShapedRecipe.Serializer.RawShapedRecipe.CODEC.flatXmap(rawShapedRecipe -> {
			try {
				final var pattern = (String[]) SHRINK.invoke(null, rawShapedRecipe.pattern());
				final var width = pattern[0].length();
				final var height = pattern.length;
				final var ingredients = NonNullList.withSize(width * height, Ingredient.EMPTY);
				final var symbols = Sets.newHashSet(rawShapedRecipe.key().keySet());

				for (var row = 0; row < pattern.length; ++row) {
					final var patternRow = pattern[row];

					for (var col = 0; col < patternRow.length(); ++col) {
						final var symbol = patternRow.substring(col, col + 1);
						final var ingredient = symbol.equals(" ") ? Ingredient.EMPTY : rawShapedRecipe.key().get(symbol);
						if (ingredient == null) {
							return DataResult.error(() -> "Pattern references symbol '" + symbol + "' but it's not defined in the key");
						}

						symbols.remove(symbol);
						ingredients.set(col + width * row, ingredient);
					}
				}

				if (!symbols.isEmpty()) {
					return DataResult.error(() -> "Key defines symbols that aren't used in pattern: " + symbols);
				}

				final var recipe = factory.createRecipe(
						rawShapedRecipe.group(),
						rawShapedRecipe.category(),
						width,
						height,
						ingredients,
						rawShapedRecipe.result(),
						rawShapedRecipe.showNotification()
				);

				return DataResult.success(recipe);
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Failed to deserialise shaped recipe", e);
			}
		}, recipe -> {
			throw new NotImplementedException("Serialising shaped recipe is not implemented yet.");
		});
	}

	/**
	 * Creates a {@link Codec} for a {@link ShapelessRecipe}.
	 * <p>
	 * Adapted from {@link ShapelessRecipe.Serializer}.
	 *
	 * @param factory      The recipe factory
	 * @param resultGetter A getter for the result stack
	 * @param <T>          The recipe type
	 * @return The codec
	 */
	public static <T extends ShapelessRecipe> Codec<T> shapelessRecipeCodec(
			final ShapelessRecipeFactory<T> factory,
			final Function<T, ItemStack> resultGetter
	) {
		return RecordCodecBuilder.create(builder -> builder.group(

				ExtraCodecs.strictOptionalField(Codec.STRING, "group", "")
						.forGetter(ShapelessRecipe::getGroup),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(ShapelessRecipe::category),

				CraftingRecipeCodecs.ITEMSTACK_OBJECT_CODEC
						.fieldOf("result")
						.forGetter(resultGetter),

				Ingredient.CODEC_NONEMPTY
						.listOf()
						.fieldOf("ingredients")
						.flatXmap(ingredients -> {
							final var nonEmptyIngredients = ingredients
									.stream()
									.filter(ingredient -> !ingredient.isEmpty())
									.toArray(Ingredient[]::new);

							if (nonEmptyIngredients.length == 0) {
								return DataResult.error(() -> "No ingredients for shapeless recipe");
							}

							try {
								return nonEmptyIngredients.length > ((int) MAX_WIDTH.get(null)) * ((int) MAX_HEIGHT.get(null)) ?
										DataResult.error(() -> "Too many ingredients for shapeless recipe") :
										DataResult.success(NonNullList.of(Ingredient.EMPTY, nonEmptyIngredients));
							} catch (final IllegalAccessException e) {
								throw new RuntimeException("Failed to deserialize shapeless recipe", e);
							}
						}, DataResult::success)
						.forGetter(ShapelessRecipe::getIngredients)

		).apply(builder, factory::createRecipe));
	}

	/**
	 * Creates a {@link Codec} for a {@link ShapelessRecipe} with a validation step.
	 * <p>
	 * Adapted from {@link ShapelessRecipe.Serializer}.
	 *
	 * @param factory      The recipe factory
	 * @param resultGetter The getter for the result stack
	 * @param validator    The recipe validator
	 * @param <T>          The recipe type
	 * @return The codec
	 */
	public static <T extends ShapelessRecipe> Codec<T> shapelessRecipeCodec(
			final ShapelessRecipeFactory<T> factory,
			final Function<T, ItemStack> resultGetter,
			final Function<T, DataResult<T>> validator
	) {
		return ExtraCodecs.validate(
				shapelessRecipeCodec(factory, resultGetter),
				validator
		);
	}

	public interface ShapedRecipeFactory<T extends ShapedRecipe> {
		T createRecipe(
				String group,
				CraftingBookCategory category,
				int width,
				int height,
				NonNullList<Ingredient> ingredients,
				ItemStack result,
				boolean showNotification
		);
	}

	public interface ShapelessRecipeFactory<T extends ShapelessRecipe> {
		T createRecipe(
				String group,
				CraftingBookCategory category,
				ItemStack result,
				NonNullList<Ingredient> ingredients
		);
	}
}
