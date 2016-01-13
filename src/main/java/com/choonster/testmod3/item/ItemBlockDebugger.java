package com.choonster.testmod3.item;

import com.choonster.testmod3.Logger;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

/**
 * An Item that prints the current state of a Block and its TileEntity on the client and server when right clicked.
 *
 * @author Choonster
 */
public class ItemBlockDebugger extends ItemTestMod3 {
	public ItemBlockDebugger() {
		super("blockDebugger");
	}

	@Override
	public boolean onItemUse(ItemStack stack, EntityPlayer playerIn, World worldIn, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = worldIn.getBlockState(pos);
		state = state.getBlock().getActualState(state, worldIn, pos);
		Logger.info("Block at %d,%d,%d: %s", pos.getX(), pos.getY(), pos.getZ(), state);

		TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity != null) {
			NBTTagCompound tagCompound = new NBTTagCompound();
			tileEntity.writeToNBT(tagCompound);
			Logger.info("TileEntity data: %s", tagCompound);
		}

		return true;
	}
}
