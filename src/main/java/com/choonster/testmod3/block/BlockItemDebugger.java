package com.choonster.testmod3.block;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.pigspawner.CapabilityPigSpawner;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;

/**
 * A Block that prints the current state of the player's held ItemStack on the client and server when right clicked.
 *
 * @author Choonster
 */
public class BlockItemDebugger extends BlockTestMod3 {
	public BlockItemDebugger() {
		super(Material.iron, "itemDebugger");
		setBlockUnbreakable();
	}

	private void logItem(EntityPlayer playerIn) {
		final ItemStack stack = playerIn.getHeldItem();
		if (stack != null) {
			Logger.info("ItemStack: %s", stack.serializeNBT());
			logCapability(stack, CapabilityPigSpawner.PIG_SPAWNER_CAPABILITY, EnumFacing.NORTH);
		}
	}

	private <T> void logCapability(ItemStack stack, Capability<T> capability, EnumFacing facing) {
		if (stack.hasCapability(capability, facing)) {
			T instance = stack.getCapability(capability, facing);
			Logger.info("Capability: %s - %s", capability.getName(), instance);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ) {
		logItem(playerIn);

		return true;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		logItem(playerIn);
	}
}
