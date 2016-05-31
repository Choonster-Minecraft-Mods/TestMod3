package com.choonster.testmod3.network;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.init.ModBlocks;
import com.choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import com.choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Sent by {@link com.choonster.testmod3.client.gui.GuiSurvivalCommandBlock} to save changes made to a Survival Command Block.
 * <p>
 * Imitates the vanilla Command Block's packet, but doesn't check if the player is in Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class MessageSurvivalCommandBlockSaveChanges implements IMessage {

	private SurvivalCommandBlockLogic.Type type;
	private String command;
	private boolean shouldTrackOutput;
	private TileEntityCommandBlock.Mode commandBlockMode;
	private boolean conditional;
	private boolean automatic;

	/**
	 * The {@link SurvivalCommandBlockLogic} instance. Client-only.
	 */
	private SurvivalCommandBlockLogic survivalCommandBlockLogic;

	/**
	 * The Survival Command Block's {@link BlockPos}. {@code null} if {@link #type} is not {@link SurvivalCommandBlockLogic.Type#BLOCK}. Server-only.
	 */
	private BlockPos blockPos;

	/**
	 * The Survival Command Block Minecart's entityID. -1 if {@link #type} is not {@link SurvivalCommandBlockLogic.Type#MINECART}. Server-only.
	 */
	private int minecartEntityID = -1;

	@SuppressWarnings("unused")
	public MessageSurvivalCommandBlockSaveChanges() {
	}


	public MessageSurvivalCommandBlockSaveChanges(SurvivalCommandBlockLogic survivalCommandBlockLogic, String command, TileEntityCommandBlock.Mode commandBlockMode, boolean conditional, boolean automatic) {
		this.survivalCommandBlockLogic = survivalCommandBlockLogic;
		this.type = survivalCommandBlockLogic.getType();
		this.command = command;
		this.shouldTrackOutput = survivalCommandBlockLogic.shouldTrackOutput();
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = SurvivalCommandBlockLogic.Type.values()[buf.readByte()];
		command = ByteBufUtils.readUTF8String(buf);
		shouldTrackOutput = buf.readBoolean();
		commandBlockMode = TileEntityCommandBlock.Mode.valueOf(ByteBufUtils.readUTF8String(buf));
		conditional = buf.readBoolean();
		automatic = buf.readBoolean();


		switch (type) {
			case BLOCK:
				blockPos = new BlockPos(buf.readInt(), buf.readInt(), buf.readInt());
				break;
			case MINECART:
				minecartEntityID = buf.readInt();
				break;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		buf.writeByte(type.ordinal());
		ByteBufUtils.writeUTF8String(buf, command);
		buf.writeBoolean(shouldTrackOutput);
		ByteBufUtils.writeUTF8String(buf, commandBlockMode.name());
		buf.writeBoolean(conditional);
		buf.writeBoolean(automatic);

		survivalCommandBlockLogic.fillInInfo(buf);
	}

	public static class Handler implements IMessageHandler<MessageSurvivalCommandBlockSaveChanges, IMessage> {

		@Override
		public IMessage onMessage(MessageSurvivalCommandBlockSaveChanges message, MessageContext ctx) {
			final EntityPlayer player = ctx.getServerHandler().playerEntity;
			final World world = player.worldObj;

			final IThreadListener mainThread = (WorldServer) world;
			mainThread.addScheduledTask(() -> {
				final MinecraftServer minecraftServer = world.getMinecraftServer();

				if (minecraftServer != null && !minecraftServer.isCommandBlockEnabled()) {
					player.addChatMessage(new TextComponentTranslation("advMode.notEnabled"));
				} else if (!player.canCommandSenderUseCommand(2, "")) {
					player.addChatMessage(new TextComponentTranslation("advMode.notAllowed"));
				} else {
					try {
						SurvivalCommandBlockLogic survivalCommandBlockLogic = null;

						if (message.type == SurvivalCommandBlockLogic.Type.BLOCK) {
							final Block newBlock;
							switch (message.commandBlockMode) {
								case SEQUENCE:
									newBlock = ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK;
									break;
								case AUTO:
									newBlock = ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK;
									break;
								case REDSTONE:
								default:
									newBlock = ModBlocks.SURVIVAL_COMMAND_BLOCK;
									break;
							}

							final TileEntity existingTileEntity = world.getTileEntity(message.blockPos);

							final EnumFacing facing = world.getBlockState(message.blockPos).getValue(BlockCommandBlock.FACING);
							final IBlockState newState = newBlock.getDefaultState().withProperty(BlockCommandBlock.FACING, facing).withProperty(BlockCommandBlock.CONDITIONAL, message.conditional);
							world.setBlockState(message.blockPos, newState);

							final TileEntity newTileEntity = world.getTileEntity(message.blockPos);
							if (existingTileEntity instanceof TileEntitySurvivalCommandBlock && newTileEntity instanceof TileEntitySurvivalCommandBlock) {
								final TileEntitySurvivalCommandBlock newTileEntitySurvivalCommandBlock = (TileEntitySurvivalCommandBlock) newTileEntity;

								newTileEntitySurvivalCommandBlock.deserializeNBT(existingTileEntity.serializeNBT());
								survivalCommandBlockLogic = newTileEntitySurvivalCommandBlock.getCommandBlockLogic();
								newTileEntitySurvivalCommandBlock.setAuto(message.automatic);
							}
						} else if (message.type == SurvivalCommandBlockLogic.Type.MINECART) {
							// no-op
						}

						if (survivalCommandBlockLogic != null) {
							survivalCommandBlockLogic.setCommand(message.command);
							survivalCommandBlockLogic.setTrackOutput(message.shouldTrackOutput);

							if (!message.shouldTrackOutput) {
								survivalCommandBlockLogic.setLastOutput(null);
							}

							survivalCommandBlockLogic.updateCommand();

							if (!StringUtils.isNullOrEmpty(message.command)) {
								player.addChatMessage(new TextComponentTranslation("advMode.setCommand.success", message.command));
							}
						}
					} catch (Exception e) {
						Logger.error(e, "Couldn't set survival command block");
					}
				}

			});

			return null;
		}
	}
}
