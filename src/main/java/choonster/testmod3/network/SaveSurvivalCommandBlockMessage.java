package choonster.testmod3.network;

import choonster.testmod3.client.gui.GuiSurvivalCommandBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.tileentity.SurvivalCommandBlockLogic;
import choonster.testmod3.tileentity.SurvivalCommandBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.StringUtils;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.fml.RegistryObject;
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
public class SaveSurvivalCommandBlockMessage {
	private static final Logger LOGGER = LogManager.getLogger();

	private final SurvivalCommandBlockLogic.Type type;
	private final String command;
	private final boolean shouldTrackOutput;
	private final CommandBlockTileEntity.Mode commandBlockMode;
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


	public SaveSurvivalCommandBlockMessage(final SurvivalCommandBlockLogic survivalCommandBlockLogic, final String command, final CommandBlockTileEntity.Mode commandBlockMode, final boolean conditional, final boolean automatic) {
		type = survivalCommandBlockLogic.getType();
		this.command = command;
		shouldTrackOutput = survivalCommandBlockLogic.isTrackOutput();
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
	}

	private SaveSurvivalCommandBlockMessage(final SurvivalCommandBlockLogic.Type type, final BlockPos blockPos, final int minecartEntityID, final String command, final boolean shouldTrackOutput, final CommandBlockTileEntity.Mode commandBlockMode, final boolean conditional, final boolean automatic) {
		this.type = type;
		this.command = command;
		this.shouldTrackOutput = shouldTrackOutput;
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
		this.blockPos = blockPos;
		this.minecartEntityID = minecartEntityID;
	}

	public static SaveSurvivalCommandBlockMessage decode(final PacketBuffer buffer) {
		final SurvivalCommandBlockLogic.Type type = buffer.readEnum(SurvivalCommandBlockLogic.Type.class);

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

		return new SaveSurvivalCommandBlockMessage(
				type,
				blockPos,
				minecartEntityID,
				buffer.readUtf(Short.MAX_VALUE),
				buffer.readBoolean(),
				buffer.readEnum(CommandBlockTileEntity.Mode.class),
				buffer.readBoolean(),
				buffer.readBoolean()
		);
	}

	public static void encode(final SaveSurvivalCommandBlockMessage message, final PacketBuffer buffer) {
		buffer.writeEnum(message.type);

		switch (message.type) {
			case BLOCK:
				buffer.writeBlockPos(message.blockPos);
				break;
			case MINECART:
				buffer.writeInt(message.minecartEntityID);
				break;
		}

		buffer.writeUtf(message.command);
		buffer.writeBoolean(message.shouldTrackOutput);
		buffer.writeEnum(message.commandBlockMode);
		buffer.writeBoolean(message.conditional);
		buffer.writeBoolean(message.automatic);
	}

	public static void handle(final SaveSurvivalCommandBlockMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final PlayerEntity player = ctx.get().getSender();
			final World world = player.level;
			final MinecraftServer minecraftServer = world.getServer();

			if (minecraftServer != null && !minecraftServer.isCommandBlockEnabled()) {
				player.sendMessage(new TranslationTextComponent("advMode.notEnabled"), Util.NIL_UUID);
			} else if (!player.hasPermissions(2)) {
				player.sendMessage(new TranslationTextComponent("advMode.notAllowed"), Util.NIL_UUID);
			} else {
				try {
					SurvivalCommandBlockLogic survivalCommandBlockLogic = null;

					if (message.type == SurvivalCommandBlockLogic.Type.BLOCK) {
						final RegistryObject<? extends Block> newBlock;
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

						final TileEntity existingTileEntity = world.getBlockEntity(message.blockPos);

						final Direction facing = world.getBlockState(message.blockPos).getValue(CommandBlockBlock.FACING);
						final BlockState newState = newBlock.get().defaultBlockState().setValue(CommandBlockBlock.FACING, facing).setValue(CommandBlockBlock.CONDITIONAL, message.conditional);
						world.setBlockAndUpdate(message.blockPos, newState);

						final TileEntity newTileEntity = world.getBlockEntity(message.blockPos);
						if (existingTileEntity instanceof SurvivalCommandBlockTileEntity && newTileEntity instanceof SurvivalCommandBlockTileEntity) {
							final SurvivalCommandBlockTileEntity newSurvivalCommandBlockTileEntity = (SurvivalCommandBlockTileEntity) newTileEntity;

							newSurvivalCommandBlockTileEntity.deserializeNBT(existingTileEntity.serializeNBT());
							survivalCommandBlockLogic = newSurvivalCommandBlockTileEntity.getCommandBlock();
							newSurvivalCommandBlockTileEntity.setAutomatic(message.automatic);
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

						survivalCommandBlockLogic.onUpdated();

						if (!StringUtils.isNullOrEmpty(message.command)) {
							player.sendMessage(new TranslationTextComponent("advMode.setCommand.success", message.command), Util.NIL_UUID);
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
