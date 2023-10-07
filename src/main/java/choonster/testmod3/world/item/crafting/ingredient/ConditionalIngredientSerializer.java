package choonster.testmod3.world.item.crafting.ingredient;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.conditions.ConditionCodec;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

/**
 * An ingredient serializer that produces another {@link Ingredient} type, but only if the
 * specified conditions are met. If they aren't, it produces {@link IngredientNever#INSTANCE} instead.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>conditions</code> - An array of condition objects, using the same format and condition types as the
 * <code>conditions</code> property of the top-level recipe object</li>
 * <li><code>ingredient</code> - An ingredient object or an array of ingredient objects, using the same format and
 * ingredient types as recipes</li>
 * </ul>
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
public class ConditionalIngredientSerializer implements IIngredientSerializer<Ingredient> {
	public static Codec<Ingredient> CODEC = ConditionCodec.checkingDecode(
			Ingredient.CODEC_NONEMPTY
					.fieldOf("ingredient")
					.codec(),
			() -> IngredientNever.INSTANCE
	);

	@Override
	public Codec<? extends Ingredient> codec() {
		return CODEC;
	}

	@Override
	public void write(final FriendlyByteBuf buffer, final Ingredient value) {
		throw new UnsupportedOperationException("Can't write to FriendlyByteBuf, use the Ingredient's own IIngredientSerializer instead");
	}

	@Override
	public Ingredient read(final FriendlyByteBuf buffer) {
		throw new UnsupportedOperationException("Can't read from FriendlyByteBuf, use the Ingredient's own IIngredientSerializer instead");
	}
}
