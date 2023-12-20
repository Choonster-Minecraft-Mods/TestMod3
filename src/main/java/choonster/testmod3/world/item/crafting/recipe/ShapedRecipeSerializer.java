package choonster.testmod3.world.item.crafting.recipe;

import choonster.testmod3.serialization.VanillaCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.ShapedRecipe;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;

/**
 * Base class for {@link ShapedRecipe} serializers.
 * <p>
 * Adapted from {@link ShapedRecipe.Serializer}.
 *
 * @author Choonster
 */
public class ShapedRecipeSerializer<T extends ShapedRecipe> implements RecipeSerializer<T> {
	private static final Field PATTERN = ObfuscationReflectionHelper.findField(ShapedRecipe.class, /* pattern */ "f_302516_");
	private static final Field RESULT = ObfuscationReflectionHelper.findField(ShapedRecipe.class, /* result */ "f_44149_");

	private final ShapedRecipeFactory<T> factory;
	private final Codec<T> codec;

	public ShapedRecipeSerializer(final ShapedRecipeFactory<T> factory) {
		this.factory = factory;

		codec = RecordCodecBuilder.create(instance -> instance.group(

				ExtraCodecs.strictOptionalField(Codec.STRING, "group", "")
						.forGetter(ShapedRecipe::getGroup),

				CraftingBookCategory.CODEC
						.fieldOf("category")
						.orElse(CraftingBookCategory.MISC)
						.forGetter(ShapedRecipe::category),

				ShapedRecipePattern.MAP_CODEC
						.forGetter(ShapedRecipeSerializer::getPattern),

				VanillaCodecs.RECIPE_RESULT
						.fieldOf("result")
						.forGetter(ShapedRecipeSerializer::getResult),

				ExtraCodecs.strictOptionalField(Codec.BOOL, "show_notification", true)
						.forGetter(ShapedRecipe::showNotification)

		).apply(instance, factory::createRecipe));
	}

	public ShapedRecipeFactory<T> factory() {
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
		final var pattern = ShapedRecipePattern.fromNetwork(buffer);
		final var result = buffer.readItem();
		final var showNotification = buffer.readBoolean();

		return factory.createRecipe(group, category, pattern, result, showNotification);
	}

	@Override
	public void toNetwork(final FriendlyByteBuf p_44227_, final T p_44228_) {
		p_44227_.writeUtf(p_44228_.getGroup());
		p_44227_.writeEnum(p_44228_.category());
		getPattern(p_44228_).toNetwork(p_44227_);
		p_44227_.writeItem(getResult(p_44228_));
		p_44227_.writeBoolean(p_44228_.showNotification());
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
