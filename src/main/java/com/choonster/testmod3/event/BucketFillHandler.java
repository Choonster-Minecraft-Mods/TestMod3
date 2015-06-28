package com.choonster.testmod3.event;

import com.choonster.testmod3.init.ModItems;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BucketFillHandler {

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event) {
		MovingObjectPosition movingObjectPosition = event.target;

		if (movingObjectPosition.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
			World world = event.world;
			EntityPlayer player = event.entityPlayer;
			ItemStack stack = event.current;
			BlockPos blockpos = event.target.getBlockPos();

			if (!world.isBlockModifiable(player, blockpos)) {
				return;
			}

			if (stack.getItem() == Items.bucket) {
				if (!player.canPlayerEdit(blockpos.offset(movingObjectPosition.sideHit), movingObjectPosition.sideHit, stack)) {
					return;
				}

				IBlockState iblockstate = world.getBlockState(blockpos);

				if (iblockstate.getBlock() instanceof IFluidBlock) {
					IFluidBlock fluidBlock = (IFluidBlock) iblockstate.getBlock();

					if (fluidBlock.canDrain(world, blockpos) && fluidBlock.drain(world, blockpos, false).amount == FluidContainerRegistry.BUCKET_VOLUME) {
						FluidStack fluidStack = fluidBlock.drain(world, blockpos, true);

						ItemStack result = new ItemStack(ModItems.bucket);
						ModItems.bucket.fill(result, fluidStack, true);

						player.triggerAchievement(StatList.objectUseStats[Item.getIdFromItem(Items.bucket)]);

						event.result = result;
						event.setResult(Event.Result.ALLOW);
					}
				}
			}
		}
	}
}
