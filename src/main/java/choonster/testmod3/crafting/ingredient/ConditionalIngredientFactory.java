package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;

import javax.annotation.Nonnull;

/**
 * An ingredient factory that produces another {@link Ingredient} type, but only if the
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
 * http://www.minecraftforge.net/forum/topic/59744-112-how-to-disable-some-mod-recipe-files-via-config-file/
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class ConditionalIngredientFactory implements IIngredientFactory {
	@Nonnull
	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		if (CraftingHelper.processConditions(JsonUtils.getJsonArray(json, "conditions"), context)) {
			return CraftingHelper.getIngredient(json.get("ingredient"), context);
		}

		return IngredientNever.INSTANCE;
	}
}
