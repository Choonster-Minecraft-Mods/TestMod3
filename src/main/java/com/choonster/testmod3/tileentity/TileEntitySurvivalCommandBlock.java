package com.choonster.testmod3.tileentity;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.client.gui.GuiIDs;
import com.choonster.testmod3.command.SurvivalCommandBlockLogic;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandResultStats;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 */
public class TileEntitySurvivalCommandBlock extends TileEntityCommandBlock {

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic = new SurvivalCommandBlockLogic(SurvivalCommandBlockLogic.Type.BLOCK) {
		/**
		 * Get the position in the world. <b>{@code null} is not allowed!</b> If you are not an entity in the world,
		 * return the coordinates 0, 0, 0
		 */
		@Override
		public BlockPos getPosition() {
			return TileEntitySurvivalCommandBlock.this.pos;
		}

		/**
		 * Get the position vector. <b>{@code null} is not allowed!</b> If you are not an entity in the world, return
		 * 0.0D, 0.0D, 0.0D
		 */
		@Override
		public Vec3 getPositionVector() {
			return new Vec3(TileEntitySurvivalCommandBlock.this.pos.getX() + 0.5D, TileEntitySurvivalCommandBlock.this.pos.getY() + 0.5D, TileEntitySurvivalCommandBlock.this.pos.getZ() + 0.5D);
		}

		/**
		 * Get the world, if available. <b>{@code null} is not allowed!</b> If you are not an entity in the world,
		 * return the overworld
		 */
		@Override
		public World getEntityWorld() {
			return TileEntitySurvivalCommandBlock.this.getWorld();
		}

		/**
		 * Sets the command.
		 */
		@Override
		public void setCommand(String command) {
			super.setCommand(command);
			TileEntitySurvivalCommandBlock.this.markDirty();
		}

		@Override
		public void func_145756_e() {
			TileEntitySurvivalCommandBlock.this.getWorld().markBlockForUpdate(TileEntitySurvivalCommandBlock.this.pos);
		}

		@Override
		@SideOnly(Side.CLIENT)
		public void func_145757_a(ByteBuf byteBuf) {
			byteBuf.writeInt(TileEntitySurvivalCommandBlock.this.pos.getX());
			byteBuf.writeInt(TileEntitySurvivalCommandBlock.this.pos.getY());
			byteBuf.writeInt(TileEntitySurvivalCommandBlock.this.pos.getZ());
		}

		/**
		 * Called when a player right clicks on the Command Block to open the edit GUI.
		 *
		 * @param player The player
		 * @return Did the player open the edit GUI?
		 */
		@Override
		public boolean func_175574_a(EntityPlayer player) {
			if (player.getEntityWorld().isRemote) {
				BlockPos pos = getPosition();
				player.openGui(TestMod3.instance, GuiIDs.SURVIVAL_COMMAND_BLOCK, player.getEntityWorld(), pos.getX(), pos.getY(), pos.getZ());
			}

			return true;
		}

		/**
		 * Returns the entity associated with the command sender. MAY BE NULL!
		 */
		@Override
		public Entity getCommandSenderEntity() {
			return null;
		}
	};

	@Override
	public SurvivalCommandBlockLogic getCommandBlockLogic() {
		return survivalCommandBlockLogic;
	}

	@Override
	public CommandResultStats getCommandResultStats() {
		return getCommandBlockLogic().getCommandResultStats();
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);

		getCommandBlockLogic().readDataFromNBT(compound.getCompoundTag("SurvivalCommandBlockLogic"));
	}

	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);

		NBTTagCompound survivalCommandBlockLogicTag = new NBTTagCompound();
		getCommandBlockLogic().writeDataToNBT(survivalCommandBlockLogicTag);
		compound.setTag("SurvivalCommandBlockLogic", survivalCommandBlockLogicTag);
	}

	@Override
	public boolean shouldRefresh(World world, BlockPos pos, IBlockState oldState, IBlockState newSate) {
		return oldState.getBlock() != newSate.getBlock();
	}
}
