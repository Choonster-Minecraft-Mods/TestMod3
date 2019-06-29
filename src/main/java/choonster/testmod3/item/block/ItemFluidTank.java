package choonster.testmod3.item.block;

import choonster.testmod3.block.FluidTankBlock;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.tileentity.FluidTankTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Fluid Tank item.
 *
 * @author Choonster
 */
public class ItemFluidTank extends BlockItem {
	private final List<ItemStack> tankItems = new ArrayList<>();

	public ItemFluidTank(final Block block, final Item.Properties properties) {
		super(block, properties);
	}

	public void addFluid(final FluidStack fluidStack) {
		final ItemStack filledTank = new ItemStack(this);

		FluidUtil.getFluidHandler(filledTank)
				.ifPresent(fluidHandler -> fluidHandler.fill(fluidStack, true));

		tankItems.add(filledTank);
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		FluidUtil.getFluidHandler(stack).ifPresent(fluidHandler -> {
			final IFluidTankProperties[] properties = fluidHandler.getTankProperties();
			tooltip.addAll(FluidTankBlock.getFluidDataForDisplay(properties));
		});
	}

	@Override
	public void fillItemGroup(final ItemGroup group, final NonNullList<ItemStack> items) {
		super.fillItemGroup(group, items);

		if (isInGroup(group)) {
			items.addAll(tankItems);
		}
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final CompoundNBT nbt) {
		return new SerializableCapabilityProvider<>(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null, new choonster.testmod3.fluids.ItemFluidTank(stack, FluidTankTileEntity.CAPACITY));
	}
}
