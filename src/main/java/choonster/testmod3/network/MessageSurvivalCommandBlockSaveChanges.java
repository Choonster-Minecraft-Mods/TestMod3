package choonster.testmod3.network;

import choonster.testmod3.client.gui.GuiSurvivalCommandBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.function.Supplier;

/**
 * Sent by {@link GuiSurvivalCommandBlock} to save changes made to a Survival Command Block.
 * <p>
 * Imitates the vanilla Command Block's packet, but doesn't check if the player is in Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class MessageSurvivalCommandBlockSaveChanges {
	private static final Logger LOGGER = LogManager.getLogger();

	private final SurvivalCommandBlockLogic.Type type;
	private final String command;
	private final boolean shouldTrackOutput;
	private final TileEntityCommandBlock.Mode commandBlockMode;
	private final boolean conditional;
	private final boolean automatic;

	/**
	 * The Survival Command Block's {@link BlockPos}. {@code null} if {@link #type} is not {@link SurvivalCommandBlockLogic.Type#BLOCK}.
	 */
	private BlockPos blockPos;

	/**
	 * The Survival Command Block Minecart's entityID. -1 if {@link #type} is not {@link SurvivalCommandBlockLogic.Type#MINECART}.
	 */
	private int minecartEntityID = -1;


	public MessageSurvivalCommandBlockSaveChanges(final SurvivalCommandBlockLogic survivalCommandBlockLogic, final String command, final TileEntityCommandBlock.Mode commandBlockMode, final boolean conditional, final boolean automatic) {
		type = survivalCommandBlockLogic.getType();
		this.command = command;
		shouldTrackOutput = survivalCommandBlockLogic.shouldTrackOutput();
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
	}

	private MessageSurvivalCommandBlockSaveChanges(final SurvivalCommandBlockLogic.Type type, final BlockPos blockPos, final int minecartEntityID, final String command, final boolean shouldTrackOutput, final TileEntityCommandBlock.Mode commandBlockMode, final boolean conditional, final boolean automatic) {
		this.type = type;
		this.command = command;
		this.shouldTrackOutput = shouldTrackOutput;
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
		this.blockPos = blockPos;
		this.minecartEntityID = minecartEntityID;
	}

	public static MessageSurvivalCommandBlockSaveChanges decode(final PacketBuffer buffer) {
		final SurvivalCommandBlockLogic.Type type = buffer.readEnumValue(SurvivalCommandBlockLogic.Type.class);

		BlockPos blockPos = null;
		int minecartEntityID = -1;

		switch (type) {
			case BLOCK:
				blockPos = buffer.readBlockPos();
				break;
			case MINECART:
				minecartEntityID = buffer.readInt();
				break;
		}

		return new MessageSurvivalCommandBlockSaveChanges(
				type,
				blockPos,
				minecartEntityID,
				buffer.readString(Short.MAX_VALUE),
				buffer.readBoolean(),
				buffer.readEnumValue(TileEntityCommandBlock.Mode.class),
				buffer.readBoolean(),
				buffer.readBoolean()
		);
	}

	public static void encode(final MessageSurvivalCommandBlockSaveChanges message, final PacketBuffer buffer) {
		buffer.writeEnumValue(message.type);

		switch (message.type) {
			case BLOCK:
				buffer.writeBlockPos(message.blockPos);
				break;
			case MINECART:
				buffer.writeInt(message.minecartEntityID);
				break;
		}

		buffer.writeString(message.command);
		buffer.writeBoolean(message.shouldTrackOutput);
		buffer.writeEnumValue(message.commandBlockMode);
		buffer.writeBoolean(message.conditional);
		buffer.writeBoolean(message.automatic);
	}

	public static void handle(final MessageSurvivalCommandBlockSaveChanges message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final EntityPlayer player = ctx.get().getSender();
			final World world = player.world;
			final MinecraftServer minecraftServer = world.getServer();

			if (minecraftServer != null && !minecraftServer.isCommandBlockEnabled()) {
				player.sendMessage(new TextComponentTranslation("advMode.notEnabled"));
			} else if (!player.hasPermissionLevel(2)) {
				player.sendMessage(new TextComponentTranslation("advMode.notAllowed"));
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

						final EnumFacing facing = world.getBlockState(message.blockPos).get(BlockCommandBlock.FACING);
						final IBlockState newState = newBlock.getDefaultState().with(BlockCommandBlock.FACING, facing).with(BlockCommandBlock.CONDITIONAL, message.conditional);
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
							player.sendMessage(new TextComponentTranslation("advMode.setCommand.success", message.command));
						}
					}
				} catch (final Exception e) {
					LOGGER.error("Couldn't set survival command block", e);
				}
			}
		});

		ctx.get().setPacketHandled(true);
	}
}
