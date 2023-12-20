package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.serialization.VanillaCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Base class for {@link ShapelessRecipe} serializers.
 * <p>
 * Adapted from {@link ShapelessRecipe.Serializer}.
 *
 * @author Choonster
 */
public class ShapelessRecipeSerializer<T extends ShapelessRecipe> implements RecipeSerializer<T> {
	private static final Field MAX_WIDTH = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_WIDTH");
	private static final Field MAX_HEIGHT = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "MAX_HEIGHT");
	private static final Field RESULT = ObfuscationReflectionHelper.findField(ShapelessRecipe.class, /* result */ "f_44243_");

	private final ShapelessRecipeFactory<T> factory;
	private final Codec<T> codec;

	public ShapelessRecipeSerializer(final ShapelessRecipeFactory<T> factory) {
		this.factory = factory;

		codec = RecordCodecBuilder.create(instance -> instance.group(

				ExtraCodecs.strictOptionalField(Codec.STRING, "group", "")
						.forGetter(ShapelessRecipe::getGroup),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(ShapelessRecipe::category),

				VanillaCodecs.RECIPE_RESULT
						.fieldOf("result")
						.forGetter(ShapelessRecipeSerializer::getResult),

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
								return nonEmptyIngredients.length > (int) MAX_WIDTH.get(null) * (int) MAX_HEIGHT.get(null) ?
										DataResult.error(() -> "Too many ingredients for shapeless recipe") :
										DataResult.success(NonNullList.of(Ingredient.EMPTY, nonEmptyIngredients));
							} catch (final IllegalAccessException e) {
								throw new RuntimeException("Failed to deserialise shapeless recipe", e);
							}
						}, DataResult::success)
						.forGetter(ShapelessRecipe::getIngredients)

		).apply(instance, factory::createRecipe));
	}

	public ShapelessRecipeFactory<T> factory() {
		return factory;
	}

	@Override
	public Codec<T> codec() {
		return codec;
	}

	@Nullable
	@Override
	public T fromNetwork(final FriendlyByteBuf buffer) {
		final var group = buffer.readUtf();
		final var category = buffer.readEnum(CraftingBookCategory.class);
		final var numIngredients = buffer.readVarInt();
		final var ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

		ingredients.replaceAll(ignored -> Ingredient.fromNetwork(buffer));

		final var result = buffer.readItem();

		return factory.createRecipe(group, category, result, ingredients);
	}

	@Override
	public void toNetwork(final FriendlyByteBuf buffer, final T recipe) {
		buffer.writeUtf(recipe.getGroup());
		buffer.writeEnum(recipe.category());
		buffer.writeVarInt(recipe.getIngredients().size());

		recipe.getIngredients()
				.forEach(ingredient -> ingredient.toNetwork(buffer));

		buffer.writeItem(getResult(recipe));
	}

	private static ItemStack getResult(final ShapelessRecipe recipe) {
		try {
			return (ItemStack) RESULT.get(recipe);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get result from shapeless recipe", e);
		}
	}
}
