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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemBucketTestMod3 extends Item {
	private final List<FluidStack> fluids = new ArrayList<>();

	public ItemBucketTestMod3() {
		setContainerItem(Items.bucket);
		setCreativeTab(TestMod3.creativeTab);
		setHasSubtypes(true);
		setUnlocalizedName("bucket.testmod3.empty");
		setMaxStackSize(1);
	}

	/**
	 * Gets a read-only list of the fluids that have had buckets registered
	 *
	 * @return The registered fluid list
	 */
	public List<FluidStack> getRegisteredFluids() {
		return Collections.unmodifiableList(fluids);
	}

	/**
	 * Register a bucket for the specified {@link Fluid}.
	 *
	 * @param fluid The fluid to register a bucket for
	 * @return The filled bucket
	 */
	public ItemStack registerBucketForFluid(Fluid fluid) {
		if (fluids.stream().anyMatch(fluidStack -> fluidStack.getFluid() == fluid)) {
			throw new IllegalArgumentException("Fluid has already had a bucket registered");
		}

		FluidStack fluidStack = new FluidStack(fluid, FluidContainerRegistry.BUCKET_VOLUME);
		fluids.add(fluidStack);

		return new ItemStack(this, 1, fluids.size() - 1);
	}

	public FluidStack getFluid(ItemStack stack) {
		return fluids.get(stack.getItemDamage());
	}

	/**
	 * Get an {@link ItemStack} of the bucket filled with the specified {@link FluidStack}.
	 * Returns {@code null} if the specified fluid hasn't been registered with {@link #registerBucketForFluid(Fluid)}.
	 *
	 * @param fluidStackToFill The {@link FluidStack} to fill the bucket with
	 * @return The filled bucket
	 */
	public ItemStack fill(FluidStack fluidStackToFill) {
		for (int meta = 0; meta < fluids.size(); meta++) {
			FluidStack fluidStack = fluids.get(meta);
			if (fluidStack.isFluidEqual(fluidStackToFill)) {
				return new ItemStack(this, 1, meta);
			}
		}

		return null;
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
		for (int meta = 0; meta < fluids.size(); meta++) {
			subItems.add(new ItemStack(this, 1, meta));
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
						return FluidContainerRegistry.EMPTY_BUCKET.copy();
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
