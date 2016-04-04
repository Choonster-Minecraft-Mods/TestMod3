package com.choonster.testmod3.network;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import com.choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IThreadListener;
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

	public MessageSurvivalCommandBlockSaveChanges(SurvivalCommandBlockLogic survivalCommandBlockLogic, String command) {
		this.survivalCommandBlockLogic = survivalCommandBlockLogic;
		this.type = survivalCommandBlockLogic.getType();
		this.command = command;
		this.shouldTrackOutput = survivalCommandBlockLogic.shouldTrackOutput();
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		type = SurvivalCommandBlockLogic.Type.values()[buf.readByte()];
		command = ByteBufUtils.readUTF8String(buf);
		shouldTrackOutput = buf.readBoolean();

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

		survivalCommandBlockLogic.fillInInfo(buf);
	}

	public static class Handler implements IMessageHandler<MessageSurvivalCommandBlockSaveChanges, IMessage> {

		@Override
		public IMessage onMessage(MessageSurvivalCommandBlockSaveChanges message, MessageContext ctx) {
			final EntityPlayer player = ctx.getServerHandler().playerEntity;
			final World world = player.worldObj;

			final IThreadListener mainThread = (WorldServer) world;
			mainThread.addScheduledTask(() -> {
				if (!world.getMinecraftServer().isCommandBlockEnabled()) {
					player.addChatMessage(new TextComponentTranslation("advMode.notEnabled"));
				} else if (player.canCommandSenderUseCommand(2, "")) {
					try {
						SurvivalCommandBlockLogic survivalCommandBlockLogic = null;

						switch (message.type) {
							case BLOCK:
								TileEntity tileEntity = world.getTileEntity(message.blockPos);
								if (tileEntity instanceof TileEntitySurvivalCommandBlock) {
									survivalCommandBlockLogic = ((TileEntitySurvivalCommandBlock) tileEntity).getCommandBlockLogic();
								}
								break;
							case MINECART:
								// no-op
						}

						if (survivalCommandBlockLogic != null) {
							survivalCommandBlockLogic.setCommand(message.command);
							survivalCommandBlockLogic.setTrackOutput(message.shouldTrackOutput);

							if (!message.shouldTrackOutput) {
								survivalCommandBlockLogic.setLastOutput(null);
							}

							survivalCommandBlockLogic.updateCommand();
							survivalCommandBlockLogic.trigger(world);
							player.addChatMessage(new TextComponentTranslation("advMode.setCommand.success", message.command));
						}
					} catch (Exception e) {
						Logger.error(e, "Couldn't set survival command block");
					}
				} else {
					player.addChatMessage(new TextComponentTranslation("advMode.notAllowed"));
				}
			});

			return null;
		}
	}
}
