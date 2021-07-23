package choonster.testmod3.world.item.block;

import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.fluid.FluidTankSnapshot;
import choonster.testmod3.fluid.ItemFluidTank;
import choonster.testmod3.world.level.block.FluidTankBlock;
import choonster.testmod3.world.level.block.entity.FluidTankBlockEntity;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
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
		final ItemStack filledTank = new ItemStack(this);

		FluidUtil.getFluidHandler(filledTank)
				.ifPresent(fluidHandler -> fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE));

		tankItems.add(filledTank);
	}

	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final Level world, final List<Component> tooltip, final TooltipFlag flag) {
		FluidUtil.getFluidHandler(stack).ifPresent(fluidHandler -> {
			final FluidTankSnapshot[] fluidTankSnapshots = FluidTankSnapshot.getSnapshotsFromFluidHandler(fluidHandler);
			tooltip.addAll(FluidTankBlock.getFluidDataForDisplay(fluidTankSnapshots));
		});
	}

	@Override
	public void fillItemCategory(final CreativeModeTab group, final NonNullList<ItemStack> items) {
		super.fillItemCategory(group, items);

		if (allowdedIn(group)) {
			items.addAll(tankItems);
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundTag nbt) {
		if (CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY == null) return null;

		return new SerializableCapabilityProvider<>(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null, new ItemFluidTank(stack, FluidTankBlockEntity.CAPACITY));
	}
}
