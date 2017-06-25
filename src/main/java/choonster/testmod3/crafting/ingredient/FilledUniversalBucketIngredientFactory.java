package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.*;

/**
 * An ingredient factory that produces a {@link UniversalBucket} filled with the specified {@link Fluid}.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>fluid</code> - The name of the {@link Fluid} to fill the bucket with</li>
 * </ul>
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class FilledUniversalBucketIngredientFactory implements IIngredientFactory {

	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		final String fluidName = JsonUtils.getString(json, "fluid");
		final Fluid fluid = FluidRegistry.getFluid(fluidName);

		if (fluid == null) {
			throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
		}

		final ItemStack filledBucket = FluidUtil.getFilledBucket(new FluidStack(fluid, 0));

		if (filledBucket.isEmpty()) {
			throw new JsonSyntaxException("No bucket registered for fluid '" + fluidName + "'");
		}

		return new IngredientNBTTestMod3(filledBucket);
	}
}
