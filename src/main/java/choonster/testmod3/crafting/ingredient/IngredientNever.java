package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * An {@link Ingredient} that never matches any {@link ItemStack}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class IngredientNever extends Ingredient {
	public static IngredientNever INSTANCE = new IngredientNever();

	private IngredientNever() {
		super(0);
	}

	@Override
	public boolean apply(@Nullable final ItemStack p_apply_1_) {
		return false;
	}

	public static class Factory implements IIngredientFactory {

		@Nonnull
		@Override
		public Ingredient parse(final JsonContext context, final JsonObject json) {
			return IngredientNever.INSTANCE;
		}
	}
}
