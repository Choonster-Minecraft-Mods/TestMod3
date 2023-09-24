package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.TestMod3;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.util.RegistryUtil;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
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
public class FluidContainerIngredient extends Ingredient {
	private static final ResourceLocation TYPE = new ResourceLocation(TestMod3.MODID, "fluid_container");

	public static Codec<FluidContainerIngredient> CODEC = RecordCodecBuilder.<FluidStack>create(instance ->
			VanillaCodecs.fluidStack(instance)
					.and(

							ResourceLocation.CODEC
									.fieldOf("type")
									.forGetter((x) -> TYPE)

					)
					.apply(instance, (fluid, amount, tag, type) -> {
						final var stack = new FluidStack(fluid, amount);
						tag.ifPresent(stack::setTag);
						return stack;
					})
	).xmap(FluidContainerIngredient::new, FluidContainerIngredient::getFluidStack);

	private final FluidStack fluidStack;
	@Nullable
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
	public JsonElement toJson(final boolean allowEmpty) {
		return ModJsonUtil.toJson(CODEC, this);
	}

	public FluidStack getFluidStack() {
		return fluidStack;
	}
}
