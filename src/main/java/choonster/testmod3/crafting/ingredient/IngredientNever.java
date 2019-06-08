package choonster.testmod3.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.IIngredientSerializer;

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
public class IngredientNever extends Ingredient {
	public static final IngredientNever INSTANCE = new IngredientNever();

	private IngredientNever() {
		super(Stream.empty());
	}

	@Override
	public boolean test(@Nullable final ItemStack p_test_1_) {
		return false;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return ModCrafting.Ingredients.NEVER;
	}

	public static class Serializer implements IIngredientSerializer<IngredientNever> {

		@Override
		public IngredientNever parse(final JsonObject json) {
			return IngredientNever.INSTANCE;
		}

		@Override
		public IngredientNever parse(final PacketBuffer buffer) {
			return IngredientNever.INSTANCE;
		}

		@Override
		public void write(final PacketBuffer buffer, final IngredientNever ingredient) {
			// No-op
		}
	}
}
