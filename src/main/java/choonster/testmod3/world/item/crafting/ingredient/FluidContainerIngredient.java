package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.util.RegistryUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.crafting.AbstractIngredient;
import net.minecraftforge.common.crafting.IIngredientSerializer;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.stream.Stream;

/**
 * An ingredient that matches any fluid container filled with the specified {@link FluidStack}.
 * <p>
 * JSON Properties:
 * <ul>
 * <li><code>fluid</code> - The registry name of the fluid</li>
 * <li><code>amount</code> - The minimum amount of fluid</li>
 * <li><code>nbt</code> (optional) - The compound tag of the FluidStack. Can be an object or a string.</li>
 * </ul>
 *
 * @author Choonster
 */
public class FluidContainerIngredient extends AbstractIngredient {
	private final FluidStack fluidStack;
	private ItemStack[] matchingStacks;

	protected FluidContainerIngredient(final FluidStack fluidStack) {
		super(Stream.empty());
		this.fluidStack = fluidStack;
	}

	public static FluidContainerIngredient fromFluidStack(final FluidStack fluidStack) {
		return new FluidContainerIngredient(fluidStack);
	}

	@Override
	public boolean isSimple() {
		return false;
	}

	@Override
	public boolean isEmpty() {
		return getItems().length == 0;
	}

	@Override
	public ItemStack[] getItems() {
		if (matchingStacks == null) {
			matchingStacks = RegistryUtil.stream(ForgeRegistries.ITEMS)
					.map(ItemStack::new)
					.filter(stack -> FluidUtil.getFluidHandler(stack).isPresent())
					.map(stack -> ModFluidUtil.fillContainer(stack, fluidStack))
					.filter(FluidActionResult::isSuccess)
					.map(FluidActionResult::getResult)
					.toArray(ItemStack[]::new);
		}

		return matchingStacks;
	}

	@Override
	public IntList getStackingIds() {
		getItems();

		return super.getStackingIds();
	}

	@Override
	protected void invalidate() {
		matchingStacks = null;

		super.invalidate();
	}

	@Override
	public boolean test(@Nullable final ItemStack stack) {
		if (stack == null) {
			return false;
		}

		return FluidUtil.getFluidContained(stack)
				.filter(fluidStack -> fluidStack.isFluidEqual(this.fluidStack))
				.filter(fluidStack -> fluidStack.getAmount() >= this.fluidStack.getAmount())
				.isPresent();
	}

	@Override
	public JsonElement toJson() {
		final JsonObject object = new JsonObject();

		object.addProperty("type", new ResourceLocation(TestMod3.MODID, "fluid_container").toString());
		object.addProperty("fluid", RegistryUtil.getRequiredRegistryName(fluidStack.getFluid()).toString());
		object.addProperty("amount", fluidStack.getAmount());

		if (fluidStack.hasTag()) {
			ModJsonUtil.setCompoundTag(object, "nbt", fluidStack.getTag());
		}

		return object;
	}

	public FluidStack getFluidStack() {
		return fluidStack;
	}

	@Override
	public IIngredientSerializer<? extends Ingredient> getSerializer() {
		return ModCrafting.Ingredients.FLUID_CONTAINER;
	}

	public static class Serializer implements IIngredientSerializer<FluidContainerIngredient> {
		@Override
		public FluidContainerIngredient parse(final JsonObject json) {
			final Fluid fluid = ModJsonUtil.getFluid(json, "fluid");

			final int amount = GsonHelper.getAsInt(json, "amount");

			if (amount <= 0) {
				throw new JsonSyntaxException("amount must be positive");
			}

			final CompoundTag nbt = ModJsonUtil.getCompoundTag(json, "nbt");

			return new FluidContainerIngredient(new FluidStack(fluid, amount, nbt));
		}

		@Override
		public FluidContainerIngredient parse(final FriendlyByteBuf buffer) {
			final FluidStack fluidStack = buffer.readFluidStack();

			return new FluidContainerIngredient(fluidStack);
		}

		@Override
		public void write(final FriendlyByteBuf buffer, final FluidContainerIngredient ingredient) {
			buffer.writeFluidStack(ingredient.fluidStack);
		}
	}
}
