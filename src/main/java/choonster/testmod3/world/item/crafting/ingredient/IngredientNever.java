package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import com.mojang.serialization.Codec;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * An {@link Ingredient} that never matches any {@link ItemStack}.
 * <p>
 * Test for this thread:
 * https://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class IngredientNever extends AbstractIngredient {
	public static final IngredientNever INSTANCE = new IngredientNever();

	public static Codec<IngredientNever> CODEC = Codec.unit(INSTANCE);

	private IngredientNever() {
		super(Stream.empty());
	}

	@Override
	public boolean test(@Nullable final ItemStack stack) {
		return false;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> serializer() {
		return ModCrafting.Ingredients.NEVER.get();
	}

	public static class Serializer implements IIngredientSerializer<IngredientNever> {
		@Override
		public Codec<? extends IngredientNever> codec() {
			return CODEC;
		}

		@Override
		public IngredientNever read(final FriendlyByteBuf buffer) {
			return IngredientNever.INSTANCE;
		}

		@Override
		public void write(final FriendlyByteBuf buffer, final IngredientNever ingredient) {
			// No-op
		}
	}
}
