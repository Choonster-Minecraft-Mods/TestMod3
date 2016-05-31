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
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * A Fluid Tank block.
 *
 * @author Choonster
 */
public class BlockFluidTank extends BlockTestMod3 {
	public BlockFluidTank() {
		super(Material.GLASS, "fluidTank");
		setSoundType(SoundType.GLASS);
	}

	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		final ItemStack stack = new ItemStack(this);

		final TileEntityFluidTank tileEntity = getTileEntity(world, pos);
		if (tileEntity != null) {
			final NBTTagCompound tankData = stack.getSubCompound("TankData", true);
			tileEntity.writeTankData(tankData);
		}

		final List<ItemStack> drops = new ArrayList<>();
		drops.add(stack);

		return drops;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer, ItemStack stack) {
		final NBTTagCompound tankData = stack.getSubCompound("TankData", false);

		final TileEntityFluidTank tileEntity = getTileEntity(worldIn, pos);
		if (tankData != null && tileEntity != null) {
			tileEntity.readTankData(tankData);
		}
	}

	@Override
	public boolean removedByPlayer(IBlockState state, World world, BlockPos pos, EntityPlayer player, boolean willHarvest) {
		// If it will harvest, delay deletion of the block until after getDrops
		return willHarvest || super.removedByPlayer(state, world, pos, player, false);
	}

	@Override
	public void harvestBlock(World world, EntityPlayer player, BlockPos pos, IBlockState state, @Nullable TileEntity te, @Nullable ItemStack stack) {
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

	@Nullable
	protected TileEntityFluidTank getTileEntity(IBlockAccess world, BlockPos pos) {
		return (TileEntityFluidTank) world.getTileEntity(pos);
	}

	public static List<ITextComponent> getFluidDataForDisplay(FluidTankInfo[] infos) {
		final List<ITextComponent> data = new ArrayList<>();

		boolean hasFluid = false;

		for (final FluidTankInfo fluidTankInfo : infos) {
			final FluidStack fluidStack = fluidTankInfo.fluid;

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
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
		final TileEntityFluidTank tileEntityFluidTank = getTileEntity(worldIn, pos);

		if (tileEntityFluidTank != null) {
			final boolean result = FluidUtil.interactWithTank(heldItem, playerIn, tileEntityFluidTank, side);

			if (!worldIn.isRemote) {
				getFluidDataForDisplay(tileEntityFluidTank.getTankInfo(side)).forEach(playerIn::addChatComponentMessage);
			}

			return result;
		}

		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullyOpaque(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
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
