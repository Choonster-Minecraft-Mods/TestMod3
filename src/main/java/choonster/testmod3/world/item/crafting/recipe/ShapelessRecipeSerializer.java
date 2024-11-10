package choonster.testmod3.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Base class for {@link ShapelessRecipe} serializers.
 * <p>
 * Adapted from {@link ShapelessRecipe.Serializer}.
 *
 * @author Choonster
 */
public class ShapelessRecipeSerializer<T extends ShapelessRecipe> implements RecipeSerializer<T> {
	private static final Field INGREDIENTS = ObfuscationReflectionHelper.findField(ShapelessRecipe.class, "ingredients");
	private static final Field RESULT = ObfuscationReflectionHelper.findField(ShapelessRecipe.class, "result");

	private final ShapelessRecipeFactory<T> factory;
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public ShapelessRecipeSerializer(final ShapelessRecipeFactory<T> factory) {
		this.factory = factory;

		codec = RecordCodecBuilder.mapCodec(instance -> instance.group(

				Codec.STRING.optionalFieldOf("group", "")
						.forGetter(ShapelessRecipe::group),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(ShapelessRecipe::category),

				ItemStack.STRICT_CODEC
						.fieldOf("result")
						.forGetter(ShapelessRecipeSerializer::getResult),

				Ingredient.CODEC
						.listOf()
						.fieldOf("ingredients")
						.forGetter(ShapelessRecipeSerializer::getIngredients)

		).apply(instance, factory::createRecipe));

		streamCodec = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				ShapelessRecipe::group,
				CraftingBookCategory.STREAM_CODEC,
				ShapelessRecipe::category,
				ItemStack.STREAM_CODEC,
				ShapelessRecipeSerializer::getResult,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
				ShapelessRecipeSerializer::getIngredients,
				factory::createRecipe
		);
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

	@SuppressWarnings("unchecked")
	private static List<Ingredient> getIngredients(final ShapelessRecipe recipe) {
		try {
			return (List<Ingredient>) INGREDIENTS.get(recipe);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get ingredients from shapeless recipe", e);
		}
	}

	private static ItemStack getResult(final ShapelessRecipe recipe) {
		try {
			return (ItemStack) RESULT.get(recipe);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get result from shapeless recipe", e);
		}
	}
}
