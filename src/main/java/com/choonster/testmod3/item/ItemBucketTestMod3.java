package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ItemBucketTestMod3 extends ItemFluidContainer {
	public final Set<FluidStack> fluids = new HashSet<>();

	public ItemBucketTestMod3() {
		super(-1, FluidContainerRegistry.BUCKET_VOLUME);
		setContainerItem(Items.bucket);
		setCreativeTab(TestMod3.creativeTab);
		setHasSubtypes(true);
		setUnlocalizedName("bucket.testmod3.empty");
		setMaxStackSize(1);
	}

	public ItemStack addFluid(Fluid fluid) {
		FluidStack fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
		fluids.add(fluidStack);

		ItemStack filledBucket = new ItemStack(this);
		fill(filledBucket, fluidStack, true);

		return filledBucket;
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		FluidStack fluidStack = getFluid(stack);

		if (fluidStack != null) {
			return "item.bucket.testmod3." + fluidStack.getUnlocalizedName().replace("fluid.", "");
		} else {
			return getUnlocalizedName();
		}
	}

	@SuppressWarnings("unchecked")
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(Item itemIn, CreativeTabs tab, List subItems) {
		for (FluidStack fluidStack : fluids) {
			ItemStack stack = new ItemStack(this);
			fill(stack, fluidStack.copy(), true);
			subItems.add(stack);
		}
	}

	public boolean isEmpty(ItemStack stack) {
		return getFluid(stack) == null;
	}

	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		boolean isEmpty = isEmpty(stack);
		MovingObjectPosition movingobjectposition = this.getMovingObjectPositionFromPlayer(world, player, isEmpty);

		if (movingobjectposition == null) {
			return stack;
		} else {
			if (movingobjectposition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
				BlockPos blockpos = movingobjectposition.getBlockPos();

				if (!world.isBlockModifiable(player, blockpos)) {
					return stack;
				}

				if (!isEmpty) {
					BlockPos blockpos1 = blockpos.offset(movingobjectposition.sideHit);

					if (!player.canPlayerEdit(blockpos1, movingobjectposition.sideHit, stack)) {
						return stack;
					}

					if (this.tryPlaceContainedLiquid(stack, world, blockpos1) && !player.capabilities.isCreativeMode) {
						player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(this)]);
						return new ItemStack(Items.bucket);
					}
				}
			}

			return stack;
		}
	}

	public boolean tryPlaceContainedLiquid(ItemStack stack, World worldIn, BlockPos pos) {
		if (isEmpty(stack)) {
			return false;
		} else {

			Material material = worldIn.getBlockState(pos).getBlock().getMaterial();
			boolean flag = !material.isSolid();

			if (!worldIn.isAirBlock(pos) && !flag) {
				return false;
			} else {
				if (!worldIn.isRemote && flag && !material.isLiquid()) {
					worldIn.destroyBlock(pos, true);
				}

				Block block = getFluid(stack).getFluid().getBlock();
				IBlockState state = block.getDefaultState();

				if (block instanceof BlockFluidFinite) {
					state = state.withProperty(BlockFluidBase.LEVEL, 7);
				}

				worldIn.setBlockState(pos, state, 3);

				return true;
			}
		}
	}
}
