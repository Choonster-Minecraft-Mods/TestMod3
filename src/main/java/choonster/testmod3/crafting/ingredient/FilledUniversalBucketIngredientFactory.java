package choonster.testmod3.crafting.ingredient;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.JsonUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.crafting.IIngredientFactory;
import net.minecraftforge.common.crafting.JsonContext;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.UniversalBucket;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

/**
 * An ingredient factory that produces a {@link UniversalBucket} filled with the specified {@link Fluid}.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>item</code> - The registry name of the {@link UniversalBucket} instance to fill</li>
 * <li><code>fluid</code> - The name of the {@link Fluid} to fill the bucket with</li>
 * </ul>
 *
 * @author Choonster
 */
@SuppressWarnings("unused")
public class FilledUniversalBucketIngredientFactory implements IIngredientFactory {

	@Override
	public Ingredient parse(final JsonContext context, final JsonObject json) {
		final String itemName = context.appendModId(JsonUtils.getString(json, "item", ForgeModContainer.getInstance().universalBucket.getRegistryName().toString()));
		final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(itemName));

		if (!(item instanceof UniversalBucket)) {
			throw new JsonSyntaxException("Item '" + itemName + "' (" + item.getClass() + ") is not a UniversalBucket");
		}

		final String fluidName = JsonUtils.getString(json, "fluid");
		final Fluid fluid = FluidRegistry.getFluid(fluidName);

		if (fluid == null) {
			throw new JsonSyntaxException("Unknown fluid '" + fluidName + "'");
		}

		final ItemStack filledBucket = UniversalBucket.getFilledBucket((UniversalBucket) item, fluid);

		return new NBTIngredient(filledBucket);
	}
}
