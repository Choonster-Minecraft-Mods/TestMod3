package choonster.testmod3.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

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
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public ShapelessRecipeSerializer(final ShapelessRecipeFactory<T> factory) {
		this.factory = factory;

		codec = RecordCodecBuilder.mapCodec(instance -> instance.group(

				Codec.STRING.optionalFieldOf("group", "")
						.forGetter(ShapelessRecipe::getGroup),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(ShapelessRecipe::category),

				ItemStack.STRICT_CODEC
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

		streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
	}

	public ShapelessRecipeFactory<T> factory() {
		return factory;
	}

	@Override
	public MapCodec<T> codec() {
		return codec;
	}

	@Override
	public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
		return streamCodec;
	}

	private T fromNetwork(final RegistryFriendlyByteBuf buffer) {
		final var group = buffer.readUtf();
		final var category = buffer.readEnum(CraftingBookCategory.class);
		final var numIngredients = buffer.readVarInt();
		final var ingredients = NonNullList.withSize(numIngredients, Ingredient.EMPTY);

		ingredients.replaceAll(ignored -> Ingredient.CONTENTS_STREAM_CODEC.decode(buffer));

		final var result = ItemStack.STREAM_CODEC.decode(buffer);

		return factory.createRecipe(group, category, result, ingredients);
	}

	private void toNetwork(final RegistryFriendlyByteBuf buffer, final T recipe) {
		buffer.writeUtf(recipe.getGroup());
		buffer.writeEnum(recipe.category());
		buffer.writeVarInt(recipe.getIngredients().size());

		recipe.getIngredients()
				.forEach(ingredient -> Ingredient.CONTENTS_STREAM_CODEC.encode(buffer, ingredient));

		ItemStack.STREAM_CODEC.encode(buffer, getResult(recipe));
	}

	private static ItemStack getResult(final ShapelessRecipe recipe) {
		try {
			return (ItemStack) RESULT.get(recipe);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get result from shapeless recipe", e);
		}
	}
}
