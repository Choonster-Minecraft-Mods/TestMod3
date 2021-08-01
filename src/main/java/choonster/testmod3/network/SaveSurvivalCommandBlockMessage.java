package choonster.testmod3.network;

import choonster.testmod3.client.gui.SurvivalCommandBlockEditScreen;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlock;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.fmllegacy.network.NetworkEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * Sent by {@link SurvivalCommandBlockEditScreen} to save changes made to a Survival Command Block.
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

	private final SurvivalCommandBlock.Type type;
	private final String command;
	private final boolean shouldTrackOutput;
	private final CommandBlockEntity.Mode commandBlockMode;
	private final boolean conditional;
	private final boolean automatic;

	/**
	 * The Survival Command Block's {@link BlockPos}. {@code null} if {@link #type} is not {@link SurvivalCommandBlock.Type#BLOCK}.
	 */
	private BlockPos blockPos;

	/**
	 * The Survival Command Block Minecart's entityID. -1 if {@link #type} is not {@link SurvivalCommandBlock.Type#MINECART}.
	 */
	private int minecartEntityID = -1;


	public SaveSurvivalCommandBlockMessage(final SurvivalCommandBlock survivalCommandBlock, final String command, final CommandBlockEntity.Mode commandBlockMode, final boolean conditional, final boolean automatic) {
		type = survivalCommandBlock.getType();
		this.command = command;
		shouldTrackOutput = survivalCommandBlock.isTrackOutput();
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
	}

	private SaveSurvivalCommandBlockMessage(final SurvivalCommandBlock.Type type, @Nullable final BlockPos blockPos, final int minecartEntityID, final String command, final boolean shouldTrackOutput, final CommandBlockEntity.Mode commandBlockMode, final boolean conditional, final boolean automatic) {
		this.type = type;
		this.command = command;
		this.shouldTrackOutput = shouldTrackOutput;
		this.commandBlockMode = commandBlockMode;
		this.conditional = conditional;
		this.automatic = automatic;
		this.blockPos = blockPos;
		this.minecartEntityID = minecartEntityID;
	}

	public static SaveSurvivalCommandBlockMessage decode(final FriendlyByteBuf buffer) {
		final SurvivalCommandBlock.Type type = buffer.readEnum(SurvivalCommandBlock.Type.class);

		BlockPos blockPos = null;
		int minecartEntityID = -1;

		switch (type) {
			case BLOCK -> blockPos = buffer.readBlockPos();
			case MINECART -> minecartEntityID = buffer.readInt();
		}

		return new SaveSurvivalCommandBlockMessage(
				type,
				blockPos,
				minecartEntityID,
				buffer.readUtf(Short.MAX_VALUE),
				buffer.readBoolean(),
				buffer.readEnum(CommandBlockEntity.Mode.class),
				buffer.readBoolean(),
				buffer.readBoolean()
		);
	}

	public static void encode(final SaveSurvivalCommandBlockMessage message, final FriendlyByteBuf buffer) {
		buffer.writeEnum(message.type);

		switch (message.type) {
			case BLOCK -> buffer.writeBlockPos(message.blockPos);
			case MINECART -> buffer.writeInt(message.minecartEntityID);
		}

		buffer.writeUtf(message.command);
		buffer.writeBoolean(message.shouldTrackOutput);
		buffer.writeEnum(message.commandBlockMode);
		buffer.writeBoolean(message.conditional);
		buffer.writeBoolean(message.automatic);
	}

	public static void handle(final SaveSurvivalCommandBlockMessage message, final Supplier<NetworkEvent.Context> ctx) {
		ctx.get().enqueueWork(() -> {
			final Player player = ctx.get().getSender();
			final Level world = player.level;
			final MinecraftServer minecraftServer = world.getServer();

			if (minecraftServer != null && !minecraftServer.isCommandBlockEnabled()) {
				player.sendMessage(new TranslatableComponent("advMode.notEnabled"), Util.NIL_UUID);
			} else if (!player.hasPermissions(2)) {
				player.sendMessage(new TranslatableComponent("advMode.notAllowed"), Util.NIL_UUID);
			} else {
				try {
					SurvivalCommandBlock survivalCommandBlock = null;

					if (message.type == SurvivalCommandBlock.Type.BLOCK) {
						final RegistryObject<? extends Block> newBlock = switch (message.commandBlockMode) {
							case SEQUENCE -> ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK;
							case AUTO -> ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK;
							default -> ModBlocks.SURVIVAL_COMMAND_BLOCK;
						};

						final BlockEntity existingBlockEntity = world.getBlockEntity(message.blockPos);

						final Direction facing = world.getBlockState(message.blockPos).getValue(CommandBlock.FACING);
						final BlockState newState = newBlock.get().defaultBlockState().setValue(CommandBlock.FACING, facing).setValue(CommandBlock.CONDITIONAL, message.conditional);
						world.setBlockAndUpdate(message.blockPos, newState);

						final BlockEntity newBlockEntity = world.getBlockEntity(message.blockPos);
						if (
								existingBlockEntity instanceof SurvivalCommandBlockEntity &&
										newBlockEntity instanceof final SurvivalCommandBlockEntity newSurvivalCommandBlockEntity
						) {
							newSurvivalCommandBlockEntity.deserializeNBT(existingBlockEntity.serializeNBT());
							survivalCommandBlock = newSurvivalCommandBlockEntity.getCommandBlock();
							newSurvivalCommandBlockEntity.setAutomatic(message.automatic);
						}
					} else if (message.type == SurvivalCommandBlock.Type.MINECART) {
						// no-op
					}

					if (survivalCommandBlock != null) {
						survivalCommandBlock.setCommand(message.command);
						survivalCommandBlock.setTrackOutput(message.shouldTrackOutput);

						if (!message.shouldTrackOutput) {
							survivalCommandBlock.setLastOutput(null);
						}

						survivalCommandBlock.onUpdated();

						if (!StringUtil.isNullOrEmpty(message.command)) {
							player.sendMessage(new TranslatableComponent("advMode.setCommand.success", message.command), Util.NIL_UUID);
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
