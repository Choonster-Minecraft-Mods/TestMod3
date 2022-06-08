package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;

import org.jetbrains.annotations.Nullable;
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

	private IngredientNever() {
		super(Stream.empty());
	}

	@Override
	public boolean test(@Nullable final ItemStack p_test_1_) {
		return false;
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public JsonElement toJson() {
		return new JsonArray();
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
		public IngredientNever parse(final FriendlyByteBuf buffer) {
			return IngredientNever.INSTANCE;
		}

		@Override
		public void write(final FriendlyByteBuf buffer, final IngredientNever ingredient) {
			// No-op
		}
	}
}
