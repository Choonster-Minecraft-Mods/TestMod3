package choonster.testmod3.client.gui;

import choonster.testmod3.client.gui.inventory.GuiModChest;
import choonster.testmod3.tileentity.TileEntityModChest;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

import static choonster.testmod3.client.gui.GuiIDs.*;

public class GuiHandler implements IGuiHandler {
	@Override
	@Nullable
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID) {
			case SURVIVAL_COMMAND_BLOCK:
			case SURVIVAL_COMMAND_BLOCK_MINECART:
				return null;

			case MOD_CHEST:
				if (tileEntity != null) {
					return ((TileEntityModChest) tileEntity).createContainer(player);
				}

			default:
				return null;
		}
	}

	@Override
	@Nullable
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));

		switch (ID) {
			case SURVIVAL_COMMAND_BLOCK:
				if (tileEntity != null) {
					return new GuiSurvivalCommandBlock((TileEntitySurvivalCommandBlock) tileEntity);
				}

			case SURVIVAL_COMMAND_BLOCK_MINECART:
				// Get SurvivalCommandBlockLogic from Minecart using x as entityID (NYI)
				return null;

			case MOD_CHEST:
				if (tileEntity != null) {
					return new GuiModChest(((TileEntityModChest) tileEntity).createContainer(player));
				}

			default:
				return null;
		}
	}
}
