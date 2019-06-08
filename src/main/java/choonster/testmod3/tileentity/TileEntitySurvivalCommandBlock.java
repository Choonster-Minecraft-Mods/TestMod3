package choonster.testmod3.tileentity;

import choonster.testmod3.block.BlockSurvivalCommandBlock;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.init.ModTileEntities;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.block.state.IBlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldServer;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class TileEntitySurvivalCommandBlock extends TileEntityCommandBlock {

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic = new SurvivalCommandBlockLogic(SurvivalCommandBlockLogic.Type.BLOCK) {
		@Override
		public void setCommand(final String command) {
			super.setCommand(command);
			markDirty();
		}

		@Override
		public void updateCommand() {
			final IBlockState state = getWorld().getBlockState(pos);
			getWorld().notifyBlockUpdate(pos, state, state, Constants.BlockFlags.DEFAULT_FLAGS);
		}

		@Override
		public WorldServer getWorld() {
			return (WorldServer) world;
		}

		@Override
		public boolean tryOpenEditCommandBlock(final EntityPlayer player) {
			if (!player.getEntityWorld().isRemote) {
				final EntityPlayerMP playerMP = (EntityPlayerMP) player;
				NetworkUtil.openClientGui(playerMP, GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, pos);
				sendToClient(playerMP);
			}

			return true;
		}

		@Override
		public Vec3d getPositionVector() {
			return new Vec3d(pos.getX() + 0.5D, pos.getY() + 0.5D, pos.getZ() + 0.5D);
		}

		public CommandSource getCommandSource() {
			return new CommandSource(
					this,
					new Vec3d(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5D),
					Vec2f.ZERO,
					getWorld(),
					2,
					getName().getString(),
					getName(),
					getWorld().getServer(),
					null
			);
		}
	};

	@Override
	public TileEntityType<?> getType() {
		return ModTileEntities.SURVIVAL_COMMAND_BLOCK;
	}

	@Override
	public SurvivalCommandBlockLogic getCommandBlockLogic() {
		return survivalCommandBlockLogic;
	}

	@Override
	public void read(final NBTTagCompound compound) {
		super.read(compound);

		getCommandBlockLogic().read(compound.getCompound("SurvivalCommandBlockLogic"));
	}

	@Override
	public NBTTagCompound write(final NBTTagCompound compound) {
		super.write(compound);

		compound.put("SurvivalCommandBlockLogic", getCommandBlockLogic().write(new NBTTagCompound()));

		return compound;
	}

	/**
	 * Send an update packet for this command block to the specified player.
	 *
	 * @param player The player.
	 */
	private void sendToClient(final EntityPlayerMP player) {
		setSendToClient(true);

		final SPacketUpdateTileEntity updatePacket = getUpdatePacket();
		if (updatePacket != null) {
			player.connection.sendPacket(updatePacket);
		}
	}

	@Override
	public Mode getMode() {
		return ((BlockSurvivalCommandBlock) getBlockState().getBlock()).getCommandBlockMode();
	}
}
