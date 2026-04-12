package choonster.testmod3.world.item.block;

import choonster.testmod3.capability.SimpleCapabilityProvider;
import choonster.testmod3.fluid.ItemFluidTank;
import choonster.testmod3.world.level.block.entity.FluidTankBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.MutableHashedLinkedMap;
import net.minecraftforge.fluids.FluidStack;
import org.jspecify.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * A Fluid Tank item.
 *
 * @author Choonster
 */
public class FluidTankItem extends BlockItem {
	private final List<FluidStack> creativeModeTabFluids = new ArrayList<>();

	public FluidTankItem(final Block block, final Item.Properties properties) {
		super(block, properties);
	}

	public void addFluid(final FluidStack fluidStack) {
		creativeModeTabFluids.add(fluidStack);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void appendHoverText(
			final ItemStack stack,
			final TooltipContext context,
			final TooltipDisplay display,
			final Consumer<Component> tooltip,
			final TooltipFlag flag
	) {
		// TODO: Uncomment when Forge reimplements IFluidHandler - https://github.com/MinecraftForge/MinecraftForge/issues/10408
		/*
		final var fluidHandler = FluidUtil.getFluidHandler(stack)
				.orElseThrow(CapabilityNotPresentException::new);

		final var fluidTankSnapshots = FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler);
		FluidTankBlock.getFluidDataForDisplay(fluidTankSnapshots).forEach(tooltip);
		*/
	}

	public void fillCreativeModeTab(final MutableHashedLinkedMap<ItemStack, CreativeModeTab.TabVisibility> entries) {
		final var empty = new ItemStack(this);

		creativeModeTabFluids.forEach(fluidStack -> {
			final var filledTank = new ItemStack(this);

			// TODO: Uncomment when Forge reimplements IFluidHandler - https://github.com/MinecraftForge/MinecraftForge/issues/10408
//		final var fluidHandler = FluidUtil.getFluidHandler(filledTank)
//				.orElseThrow(CapabilityNotPresentException::new);
//
//		fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

			entries.putAfter(empty, filledTank, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
		});
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
