package com.choonster.testmod3.item.block;

import com.choonster.testmod3.block.BlockFluidTank;
import com.choonster.testmod3.tileentity.TileEntityFluidTank;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidContainerItem;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemFluidTank extends ItemBlock implements IFluidContainerItem {
	private final List<ItemStack> tankItems = new ArrayList<>();

	public ItemFluidTank(Block block) {
		super(block);
		setHasSubtypes(true);
		setMaxStackSize(1);
	}

	private FluidTank loadTank(ItemStack stack) {
		NBTTagCompound teData = stack.getSubCompound("TankData", true);
		return TileEntityFluidTank.loadTank(teData, null);
	}

	private void saveTank(ItemStack stack, FluidTank tank) {
		NBTTagCompound teData = stack.getSubCompound("TankData", true);
		TileEntityFluidTank.saveTank(teData, tank);
	}

	public ItemStack addFluid(FluidStack fluidStack) {
		ItemStack filledTank = new ItemStack(this);
		tankItems.add(filledTank);

		fill(filledTank, fluidStack, true);

		return filledTank;
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		FluidTankInfo[] fluidTankInfos = new FluidTankInfo[]{loadTank(stack).getInfo()};
		List<String> lines = BlockFluidTank.getFluidDataForDisplay(fluidTankInfos).stream()
				.map(IChatComponent::getFormattedText).collect(Collectors.toList());
		tooltip.addAll(lines);
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		super.getSubItems(itemIn, tab, subItems);
		subItems.addAll(tankItems);
	}

	@Override
	public FluidStack getFluid(ItemStack container) {
		return loadTank(container).getFluid();
	}

	@Override
	public int getCapacity(ItemStack container) {
		return loadTank(container).getCapacity();
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) {
		FluidTank tank = loadTank(container);
		int result = tank.fill(resource, doFill);
		saveTank(container, tank);

		return result;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) {
		FluidTank tank = loadTank(container);
		FluidStack result = tank.drain(maxDrain, doDrain);
		saveTank(container, tank);

		return result;
	}
}
