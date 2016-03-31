package com.choonster.testmod3.block;

import com.choonster.testmod3.tileentity.TileEntityFluidTank;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;

/**
 * A Fluid Tank block.
 *
 * @author Choonster
 */
public class BlockFluidTank extends BlockTestMod3 {
	public BlockFluidTank() {
		super(Material.glass, "fluidTank");
		setSoundType(SoundType.GLASS);
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
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		// If it will harvest, delay deletion of the block until after getDrops
		return willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, TileEntity te, ItemStack stack) {
		super.harvestBlock(world, player, pos, state, te, stack);
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

	public static List<ITextComponent> getFluidDataForDisplay(FluidTankInfo[] infos) {
		List<ITextComponent> data = new ArrayList<>();

		boolean hasFluid = false;

		for (FluidTankInfo fluidTankInfo : infos) {
			FluidStack fluidStack = fluidTankInfo.fluid;

			if (fluidStack != null && fluidStack.amount > 0) {
				hasFluid = true;
				data.add(new TextComponentTranslation("tile.testmod3:fluidTank.fluid.desc", fluidStack.getLocalizedName(), fluidStack.amount, fluidTankInfo.capacity));
			}
		}

		if (!hasFluid) {
			data.add(new TextComponentTranslation("tile.testmod3:fluidTank.empty.desc"));
		}

		return data;
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		TileEntityFluidTank tileEntityFluidTank = getTileEntity(worldIn, pos);
		if (heldItem != null) {
			ItemStack container = tileEntityFluidTank.tryUseFluidContainer(heldItem, side);
			if (container != null) {
				if (!playerIn.capabilities.isCreativeMode) {
					heldItem.stackSize--;
					if (heldItem.stackSize <= 0) {
						playerIn.setHeldItem(hand, container); // Replace the player's held item with the container
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
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side) {
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT;
	}
}
