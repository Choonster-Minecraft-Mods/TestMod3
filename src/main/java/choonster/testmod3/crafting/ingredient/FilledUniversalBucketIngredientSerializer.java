package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.fluid.Fluid;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.common.crafting.NBTIngredient;

/**
 * An ingredient factory that produces a {_@link UniversalBucket} filled with the specified {@link Fluid}.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>fluid</code> - The name of the {@link Fluid} to fill the bucket with</li>
 * </ul>
 *
 * @author Choonster
 */
// TODO: Update when fluids are re-implemented in 1.14
public class FilledUniversalBucketIngredientSerializer implements IIngredientSerializer<NBTIngredient> {

	@Override
	public TestMod3NBTIngredient parse(final JsonObject json) {
		/*
		final String fluidName = JsonUtils.getString(json, "fluid");
		final Fluid fluid = FluidRegistry.getFluid(fluidName);

		if (fluid == null) {
			throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
		}

		final ItemStack filledBucket = FluidUtil.getFilledBucket(new FluidStack(fluid, 0));

		if (filledBucket.isEmpty()) {
			throw new JsonSyntaxException("No bucket registered for fluid '" + fluidName + "'");
		}

		return new NBTIngredientTestMod3(filledBucket);
		*/

		throw new JsonSyntaxException("Fluids aren't implemented");
	}

	@Override
	public TestMod3NBTIngredient parse(final PacketBuffer buffer) {
		throw new UnsupportedOperationException("Fluids aren't implemented");
	}

	@Override
	public void write(final PacketBuffer buffer, final NBTIngredient ingredient) {
		throw new UnsupportedOperationException("Fluids aren't implemented");
	}
}
