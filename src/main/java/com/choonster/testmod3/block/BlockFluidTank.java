package com.choonster.testmod3.block;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.tileentity.TileEntityFluidTank;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.*;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

public class BlockFluidTank extends Block {
	public BlockFluidTank() {
		super(Material.glass);
		setStepSound(Block.soundTypeGlass);
		setUnlocalizedName("fluidTank");
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		ItemStack stack = new ItemStack(this);
		NBTTagCompound tankData = stack.getSubCompound("TankData", true);

		TileEntityFluidTank tileEntity = getTileEntity(world, pos);
		tileEntity.writeTankData(tankData);

		List<ItemStack> drops = new ArrayList<>();
		drops.add(stack);

		return drops;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		NBTTagCompound tankData = stack.getSubCompound("TankData", false);

		if (tankData != null) {
			TileEntityFluidTank tileEntity = getTileEntity(worldIn, pos);
			tileEntity.readTankData(tankData);
		}
	}

	@Override
	public boolean removedByPlayer(World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		// If it will harvest, delay deletion of the block until after getDrops
		return willHarvest || super.removedByPlayer(world, pos, player, false);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te) {
		super.harvestBlock(world, player, pos, state, te);
		world.setBlockToAir(pos);
	}

	@Override
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return new TileEntityFluidTank();
	}

	protected TileEntityFluidTank getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TileEntityFluidTank) world.getTileEntity(pos);
	}

	public static List<IChatComponent> getFluidDataForDisplay(FluidTankInfo[] infos) {
		List<IChatComponent> data = new ArrayList<>();

		boolean hasFluid = false;

		for (FluidTankInfo fluidTankInfo : infos) {
			FluidStack fluidStack = fluidTankInfo.fluid;

			if (fluidStack != null && fluidStack.amount > 0) {
				hasFluid = true;
				data.add(new ChatComponentTranslation("tile.fluidTank.desc.fluid", fluidStack.getLocalizedName(), fluidStack.amount, fluidTankInfo.capacity));
			}
		}

		if (!hasFluid) {
			data.add(new ChatComponentTranslation("tile.fluidTank.desc.empty"));
		}

		return data;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityFluidTank tileEntityFluidTank = getTileEntity(worldIn, pos);
		ItemStack heldItem = playerIn.getHeldItem();
		if (heldItem != null) {
			ItemStack container = tileEntityFluidTank.tryUseFluidContainer(heldItem, side);
			if (container != null) {
				if (!playerIn.capabilities.isCreativeMode) {
					heldItem.stackSize--;
					if (heldItem.stackSize <= 0) {
						playerIn.destroyCurrentEquippedItem(); // Destroy the current held item
						playerIn.setCurrentItemOrArmor(0, container); // Replace it with the container
					} else if (!playerIn.inventory.addItemStackToInventory(container)) {
						playerIn.dropPlayerItemWithRandomChoice(container, false);
					}
				}

				playerIn.inventoryContainer.detectAndSendChanges();

				return true;
			}
		}

		if (!worldIn.isRemote) {
			getFluidDataForDisplay(tileEntityFluidTank.getTankInfo(EnumFacing.NORTH)).forEach(playerIn::addChatComponentMessage);
		}

		return true;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public EnumWorldBlockLayer getBlockLayer() {
		return EnumWorldBlockLayer.CUTOUT;
	}
}
