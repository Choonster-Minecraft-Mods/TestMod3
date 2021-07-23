package choonster.testmod3.tileentity;

import choonster.testmod3.block.SurvivalCommandBlockBlock;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.init.ModTileEntities;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.block.BlockState;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.Constants;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class SurvivalCommandBlockTileEntity extends CommandBlockTileEntity {

	private final SurvivalCommandBlockLogic survivalCommandBlockLogic = new SurvivalCommandBlockLogic(SurvivalCommandBlockLogic.Type.BLOCK) {
		@Override
		public void setCommand(final String command) {
			super.setCommand(command);
			setChanged();
		}

		@Override
		public void onUpdated() {
			final BlockState state = getLevel().getBlockState(worldPosition);
			getLevel().sendBlockUpdated(worldPosition, state, state, Constants.BlockFlags.DEFAULT);
		}

		@Override
		public ServerWorld getLevel() {
			return (ServerWorld) level;
		}

		@Override
		public ActionResultType usedBy(final PlayerEntity player) {
			if (!player.getCommandSenderWorld().isClientSide) {
				final ServerPlayerEntity playerMP = (ServerPlayerEntity) player;
				NetworkUtil.openClientGui(playerMP, GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, worldPosition);
				sendToClient(playerMP);
			}

			return ActionResultType.SUCCESS;
		}

		@Override
		public Vector3d getPosition() {
			return new Vector3d(worldPosition.getX() + 0.5D, worldPosition.getY() + 0.5D, worldPosition.getZ() + 0.5D);
		}

		@Override
		public CommandSource createCommandSourceStack() {
			return new CommandSource(
					this,
					new Vector3d(worldPosition.getX() + 0.5d, worldPosition.getY() + 0.5d, worldPosition.getZ() + 0.5D),
					Vector2f.ZERO,
					getLevel(),
					2,
					getName().getString(),
					getName(),
					getLevel().getServer(),
					null
			);
		}
	};

	@Override
	public TileEntityType<?> getType() {
		return ModTileEntities.SURVIVAL_COMMAND_BLOCK.get();
	}

	@Override
	public SurvivalCommandBlockLogic getCommandBlock() {
		return survivalCommandBlockLogic;
	}

	@Override
	public void load(final BlockState state, final CompoundNBT nbt) {
		super.load(state, nbt);

		getCommandBlock().load(nbt.getCompound("SurvivalCommandBlockLogic"));
	}

	@Override
	public CompoundNBT save(final CompoundNBT compound) {
		super.save(compound);

		compound.put("SurvivalCommandBlockLogic", getCommandBlock().save(new CompoundNBT()));

		return compound;
	}

	/**
	 * Send an update packet for this command block to the specified player.
	 *
	 * @param player The player.
	 */
	private void sendToClient(final ServerPlayerEntity player) {
		setSendToClient(true);

		final SUpdateTileEntityPacket updatePacket = getUpdatePacket();
		if (updatePacket != null) {
			player.connection.send(updatePacket);
		}
	}

	@Override
	public Mode getMode() {
		return ((SurvivalCommandBlockBlock) getBlockState().getBlock()).getCommandBlockMode();
	}
}
