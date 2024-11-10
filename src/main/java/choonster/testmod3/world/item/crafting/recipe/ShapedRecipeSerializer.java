package choonster.testmod3.world.item.crafting.recipe;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Base class for {@link ShapedRecipe} serializers.
 * <p>
 * Adapted from {@link ShapedRecipe.Serializer}.
 *
 * @author Choonster
 */
public class ShapedRecipeSerializer<T extends ShapedRecipe> implements RecipeSerializer<T> {
	private static final Field PATTERN = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "pattern");
	private static final Field RESULT = ObfuscationReflectionHelper.findField(ShapedRecipe.class, "result");

	private final ShapedRecipeFactory<T> factory;
	private final MapCodec<T> codec;
	private final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

	public ShapedRecipeSerializer(final ShapedRecipeFactory<T> factory) {
		this.factory = factory;

		codec = RecordCodecBuilder.mapCodec(instance -> instance.group(

				Codec.STRING.optionalFieldOf("group", "")
						.forGetter(ShapedRecipe::group),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(ShapedRecipe::category),

				ShapedRecipePattern.MAP_CODEC
						.forGetter(ShapedRecipeSerializer::getPattern),

				ItemStack.STRICT_CODEC
						.fieldOf("result")
						.forGetter(ShapedRecipeSerializer::getResult),

				Codec.BOOL.optionalFieldOf("show_notification", true)
						.forGetter(ShapedRecipe::showNotification)

		).apply(instance, factory::createRecipe));

		streamCodec = StreamCodec.of(this::toNetwork, this::fromNetwork);
	}

	public ShapedRecipeFactory<T> factory() {
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
		final var pattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
		final var result = ItemStack.STREAM_CODEC.decode(buffer);
		final var showNotification = buffer.readBoolean();

		return factory.createRecipe(group, category, pattern, result, showNotification);
	}

	private void toNetwork(final RegistryFriendlyByteBuf buffer, final T recipe) {
		buffer.writeUtf(recipe.group());
		buffer.writeEnum(recipe.category());
		ShapedRecipePattern.STREAM_CODEC.encode(buffer, getPattern(recipe));
		ItemStack.STREAM_CODEC.encode(buffer, getResult(recipe));
		buffer.writeBoolean(recipe.showNotification());
	}

	private static ShapedRecipePattern getPattern(final ShapedRecipe recipe) {
		try {
			return (ShapedRecipePattern) PATTERN.get(recipe);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get pattern from shaped recipe", e);
		}
	}

	private static ItemStack getResult(final ShapedRecipe recipe) {
		try {
			return (ItemStack) RESULT.get(recipe);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get result from shaped recipe", e);
		}
	}
}
