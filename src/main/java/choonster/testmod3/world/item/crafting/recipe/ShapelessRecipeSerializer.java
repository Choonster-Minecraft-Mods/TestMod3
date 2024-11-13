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

import java.util.List;

/**
 * Base class for {@link BaseShapelessRecipe} serializers.
 * <p>
 * Adapted from {@link ShapelessRecipe.Serializer}.
 *
 * @author Choonster
 */
public class ShapelessRecipeSerializer<T extends BaseShapelessRecipe> implements RecipeSerializer<T> {
	private final ShapelessRecipeFactory<T> factory;
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public ShapelessRecipeSerializer(final ShapelessRecipeFactory<T> factory) {
		this.factory = factory;

		codec = RecordCodecBuilder.mapCodec(instance -> instance.group(

				Codec.STRING.optionalFieldOf("group", "")
						.forGetter(BaseShapelessRecipe::group),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(BaseShapelessRecipe::category),

				ItemStack.STRICT_CODEC
						.fieldOf("result")
						.forGetter(ShapelessRecipeSerializer::result),

				Ingredient.CODEC
						.listOf()
						.fieldOf("ingredients")
						.forGetter(ShapelessRecipeSerializer::ingredients)

		).apply(instance, factory::createRecipe));

		streamCodec = StreamCodec.composite(
				ByteBufCodecs.STRING_UTF8,
				BaseShapelessRecipe::group,
				CraftingBookCategory.STREAM_CODEC,
				BaseShapelessRecipe::category,
				ItemStack.STREAM_CODEC,
				ShapelessRecipeSerializer::result,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
				ShapelessRecipeSerializer::ingredients,
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

	private static ItemStack result(final BaseShapelessRecipe recipe) {
		return recipe.result;
	}

	private static List<Ingredient> ingredients(final BaseShapelessRecipe recipe) {
		return recipe.ingredients;
	}
}
