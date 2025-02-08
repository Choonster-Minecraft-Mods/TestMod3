package choonster.testmod3.world.item.block;

import choonster.testmod3.capability.SimpleCapabilityProvider;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.fluid.ItemFluidTank;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.world.level.block.FluidTankBlock;
import choonster.testmod3.world.level.block.entity.FluidTankBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;

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

		final var fluidHandler = FluidUtil.getFluidHandler(filledTank)
				.orElseThrow(CapabilityNotPresentException::new);

		fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

		tankItems.add(filledTank);
	}

	@Override
	public void appendHoverText(final ItemStack stack, final TooltipContext context, final List<Component> tooltip, final TooltipFlag flag) {
		final var fluidHandler = FluidUtil.getFluidHandler(stack)
				.orElseThrow(CapabilityNotPresentException::new);

		final var fluidTankSnapshots = FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler);
		tooltip.addAll(FluidTankBlock.getFluidDataForDisplay(fluidTankSnapshots));
	}

	public void fillCreativeModeTab(final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
		final var empty = new ItemStack(this);

		tankItems.forEach(stack ->
				entries.putAfter(empty, stack, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS)
		);
	}

	@Nullable
	@Override
	public ICapabilityProvider getCapabilityProvider(final ItemStack stack) {
		return new SimpleCapabilityProvider<>(
				ForgeCapabilities.FLUID_HANDLER_ITEM,
				null,
				new ItemFluidTank(FluidTankBlockEntity.CAPACITY, stack)
		);
	}
}
