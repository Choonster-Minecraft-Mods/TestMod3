package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.init.ModCrafting;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ingredients.AbstractIngredient;
import net.minecraftforge.common.crafting.ingredients.IIngredientSerializer;
import net.minecraftforge.fluids.FluidActionResult;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nullable;
import java.util.List;

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
	public static final MapCodec<FluidContainerIngredient> CODEC = RecordCodecBuilder.<FluidStack>mapCodec(instance ->

			VanillaCodecs.fluidStack(instance)
					.apply(instance, (fluid, amount, tag) -> {
						final var stack = new FluidStack(fluid, amount);
						tag.ifPresent(stack::setTag);
						return stack;
					})

	).xmap(FluidContainerIngredient::new, FluidContainerIngredient::getFluidStack);

	private final FluidStack fluidStack;
	@Nullable
	private List<Holder<Item>> items;

	protected FluidContainerIngredient(final FluidStack fluidStack) {
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
	public IIngredientSerializer<? extends Ingredient> serializer() {
		return ModCrafting.Ingredients.FLUID_CONTAINER.get();
	}

	@Override
	public List<Holder<Item>> items() {
		if (items == null) {
			items = RegistryUtil.stream(ForgeRegistries.ITEMS)
					.map(ItemStack::new)
					.filter(stack -> FluidUtil.getFluidHandler(stack).isPresent())
					.map(stack -> ModFluidUtil.fillContainer(stack, fluidStack))
					.filter(FluidActionResult::isSuccess)
					.map(FluidActionResult::getResult)
					.map(ItemStack::getItemHolder)
					.toList();
		}

		return items;
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

	public FluidStack getFluidStack() {
		return fluidStack;
	}

	public static class Serializer implements IIngredientSerializer<FluidContainerIngredient> {
		@Override
		public MapCodec<? extends FluidContainerIngredient> codec() {
			return CODEC;
		}

		@Override
		public void write(final RegistryFriendlyByteBuf buffer, final FluidContainerIngredient value) {
			VanillaCodecs.FLUID_STACK_STREAM_CODEC.encode(buffer, value.fluidStack);
		}

		@Override
		public FluidContainerIngredient read(final RegistryFriendlyByteBuf buffer) {
			final var fluidStack = VanillaCodecs.FLUID_STACK_STREAM_CODEC.decode(buffer);

			return new FluidContainerIngredient(fluidStack);
		}
	}
}
