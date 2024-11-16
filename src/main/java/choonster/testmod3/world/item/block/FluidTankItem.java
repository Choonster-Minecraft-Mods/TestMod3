package choonster.testmod3.world.item.block;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.fluids.FluidStack;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fluid Tank item.
 *
 * @author Choonster
 */
public class FluidTankItem extends BlockItem {
	private final List<ItemStack> tankItems = new ArrayList<>();

	public FluidTankItem(final Block block, final Item.Properties properties) {
		super(block, properties);
	}

	public void addFluid(final FluidStack fluidStack) {
		final var filledTank = new ItemStack(this);

		// TODO: Item capabilities
		/*
		final var fluidHandler = FluidUtil.getFluidHandler(filledTank)
				.orElseThrow(CapabilityNotPresentException::new);

		fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);
		*/

		tankItems.add(filledTank);
	}

	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final List<Component> tooltip, final TooltipFlag flag) {
		// TODO: Item capabilities
		/*
		final var fluidHandler = FluidUtil.getFluidHandler(stack)
				.orElseThrow(CapabilityNotPresentException::new);

		final var fluidTankSnapshots = FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler);
		tooltip.addAll(FluidTankBlock.getFluidDataForDisplay(fluidTankSnapshots));
		*/
	}

	public void fillCreativeModeTab(final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
		final var empty = new ItemStack(this);

		tankItems.forEach(stack ->
				entries.putAfter(empty, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
		);
	}

	// TODO: Item capabilities
	/*
	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		if (ForgeCapabilities.FLUID_HANDLER_ITEM == null) {
			return null;
		}

		return new SerializableCapabilityProvider<>(ForgeCapabilities.FLUID_HANDLER_ITEM, null, new ItemFluidTank(stack, FluidTankBlockEntity.CAPACITY));
	}
	*/
}
