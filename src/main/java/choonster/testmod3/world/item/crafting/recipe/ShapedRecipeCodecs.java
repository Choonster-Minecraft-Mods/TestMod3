package choonster.testmod3.world.item.crafting.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStackTemplate;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;

/**
 * Codecs for {@link BaseShapedRecipe} classes.
 * <p>
 * Adapted from {@link ShapedRecipe#MAP_CODEC} and {@link ShapedRecipe#STREAM_CODEC}.
 *
 * @author Choonster
 */
public class ShapedRecipeCodecs {
	public static <T extends BaseShapedRecipe> MapCodec<T> mapCodec(final ShapedRecipeFactory<T> factory) {
		return RecordCodecBuilder.mapCodec(
				i -> i.group(
								Recipe.CommonInfo.MAP_CODEC.forGetter(o -> o.commonInfo),
								CraftingRecipe.CraftingBookInfo.MAP_CODEC.forGetter(o -> o.bookInfo),
								ShapedRecipePattern.MAP_CODEC.forGetter(o -> o.pattern),
								ItemStackTemplate.CODEC.fieldOf("result").forGetter(o -> o.result)
						)
						.apply(i, factory::createRecipe)
		);
	}

	public static <T extends BaseShapedRecipe> StreamCodec<RegistryFriendlyByteBuf, T> streamCodec(final ShapedRecipeFactory<T> factory) {
		return StreamCodec.composite(
				Recipe.CommonInfo.STREAM_CODEC,
				o -> o.commonInfo,
				CraftingRecipe.CraftingBookInfo.STREAM_CODEC,
				o -> o.bookInfo,
				ShapedRecipePattern.STREAM_CODEC,
				o -> o.pattern,
				ItemStackTemplate.STREAM_CODEC,
				o -> o.result,
				factory::createRecipe
		);
	}
}
