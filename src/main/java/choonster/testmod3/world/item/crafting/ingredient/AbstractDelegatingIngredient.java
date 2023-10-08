package choonster.testmod3.world.item.crafting.ingredient;

import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

import javax.annotation.Nullable;

/**
 * Base class for an ingredient that can be serialized during data generation and deserializes to another ingredient
 * type at runtime.
 *
 * @author Choonster
 */
public abstract class AbstractDelegatingIngredient extends AbstractIngredient {
	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean test(@Nullable final ItemStack stack) {
		throw new UnsupportedOperationException("Can't use in recipes at runtime, use the nested Ingredient instead");
	}

	/**
	 * An ingredient serializer that can serialize to and deserialize from JSON but can't write to
	 * or read from the network.
	 *
	 * @author Choonster
	 */
	public record Serializer(Codec<Ingredient> codec) implements IIngredientSerializer<Ingredient> {
		@Override
		public Codec<Ingredient> codec() {
			return codec;
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
}
