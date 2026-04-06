package choonster.testmod3.world.item.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapelessRecipe;

/**
 * Codecs for {@link BaseShapelessRecipe} serializers.
 * <p>
 * Adapted from {@link ShapelessRecipe#MAP_CODEC} and {@link ShapelessRecipe#STREAM_CODEC}.
 *
 * @author Choonster
 */
public class ShapelessRecipeCodecs {
	public static <T extends BaseShapelessRecipe> MapCodec<T> mapCodec(final ShapelessRecipeFactory<T> factory) {
		return RecordCodecBuilder.mapCodec(instance -> instance.group(

				Recipe.CommonInfo.MAP_CODEC
						.forGetter(o -> o.commonInfo),

				CraftingRecipe.CraftingBookInfo.MAP_CODEC
						.forGetter(o -> o.bookInfo),

				ItemStackTemplate.CODEC
						.fieldOf("result")
						.forGetter(o -> o.result),

				Ingredient.CODEC.listOf()
						.fieldOf("ingredients")
						.forGetter(o -> o.ingredients)

		).apply(instance, factory::createRecipe));
	}

	public static <T extends BaseShapelessRecipe> StreamCodec<RegistryFriendlyByteBuf, T> streamCodec(final ShapelessRecipeFactory<T> factory) {
		return StreamCodec.composite(
				Recipe.CommonInfo.STREAM_CODEC,
				o -> o.commonInfo,
				CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
				o -> o.bookInfo,
				ItemStackTemplate.STREAM_CODEC,
				o -> o.result,
				Ingredient.CONTENTS_STREAM_CODEC.apply(ByteBufCodecs.list()),
				o -> o.ingredients,
				factory::createRecipe
		);
	}
}
