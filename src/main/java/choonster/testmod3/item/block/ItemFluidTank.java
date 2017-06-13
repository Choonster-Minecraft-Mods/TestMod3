package choonster.testmod3.item.block;

import choonster.testmod3.block.BlockFluidTank;
import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.fluids.FluidTankItem;
import choonster.testmod3.tileentity.TileEntityFluidTank;
import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A Fluid Tank item.
 *
 * @author Choonster
 */
public class ItemFluidTank extends ItemBlock {
	private final List<ItemStack> tankItems = new ArrayList<>();

	public ItemFluidTank(final Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	public void addFluid(final FluidStack fluidStack) {
		final ItemStack filledTank = new ItemStack(this);
		final IFluidHandler fluidHandler = FluidUtil.getFluidHandler(filledTank);
		if (fluidHandler != null) {
			fluidHandler.fill(fluidStack, true);
		}

		tankItems.add(filledTank);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag advanced) {
		final IFluidHandler fluidHandler = FluidUtil.getFluidHandler(stack);
		if (fluidHandler != null) {
			final IFluidTankProperties[] properties = fluidHandler.getTankProperties();
			final List<String> lines = BlockFluidTank.getFluidDataForDisplay(properties).stream()
					.map(ITextComponent::getFormattedText).collect(Collectors.toList());
			tooltip.addAll(lines);
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
		super.getSubItems(tab, subItems);
		subItems.addAll(tankItems);
	}

	@Override
	public ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final NBTTagCompound nbt) {
		return new CapabilityProviderSerializable<>(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null, new FluidTankItem(stack, TileEntityFluidTank.CAPACITY));
	}
}
