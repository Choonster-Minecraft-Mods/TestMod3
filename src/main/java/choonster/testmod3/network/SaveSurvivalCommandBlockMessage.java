package choonster.testmod3.network;

import choonster.testmod3.client.gui.SurvivalCommandBlockEditScreen;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlock;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import com.mojang.datafixers.util.Either;
import com.mojang.logging.LogUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringUtil;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraftforge.event.network.CustomPayloadEvent;
import org.apache.commons.lang3.NotImplementedException;
import org.slf4j.Logger;

import java.util.Objects;

/**
 * Sent by {@link SurvivalCommandBlockEditScreen} to save changes made to a Survival Command Block.
 * <p>
 * Imitates the vanilla Command Block's packet, but doesn't check if the player is in Creative Mode.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @param blockPosOrMinecartEntityId The Survival Command Block's {@link BlockPos} if {@link #type} is
 *                                   {@link SurvivalCommandBlock.Type#BLOCK}, or the Survival Command Block Minecart's
 *                                   entityID if {@link #type} is {@link SurvivalCommandBlock.Type#MINECART}.
 * @author Choonster
 */
public record SaveSurvivalCommandBlockMessage(
		SurvivalCommandBlock.Type type,
		Either<BlockPos, Integer> blockPosOrMinecartEntityId,
		String command,
		boolean shouldTrackOutput,
		CommandBlockEntity.Mode commandBlockMode,
		boolean conditional,
		boolean automatic
) {
	private static final Logger LOGGER = LogUtils.getLogger();

	public static final StreamCodec<RegistryFriendlyByteBuf, SaveSurvivalCommandBlockMessage> STREAM_CODEC = VanillaCodecs.compositeStreamCodec(
			SurvivalCommandBlock.Type.STREAM_CODEC,
			SaveSurvivalCommandBlockMessage::type,
			ByteBufCodecs.either(BlockPos.STREAM_CODEC, ByteBufCodecs.VAR_INT),
			SaveSurvivalCommandBlockMessage::blockPosOrMinecartEntityId,
			ByteBufCodecs.STRING_UTF8,
			SaveSurvivalCommandBlockMessage::command,
			ByteBufCodecs.BOOL,
			SaveSurvivalCommandBlockMessage::shouldTrackOutput,
			VanillaCodecs.COMMAND_BLOCK_MODE_STREAM_CODEC,
			SaveSurvivalCommandBlockMessage::commandBlockMode,
			ByteBufCodecs.BOOL,
			SaveSurvivalCommandBlockMessage::conditional,
			ByteBufCodecs.BOOL,
			SaveSurvivalCommandBlockMessage::automatic,
			SaveSurvivalCommandBlockMessage::new
	);

	public SaveSurvivalCommandBlockMessage {
		if (type == SurvivalCommandBlock.Type.BLOCK && blockPosOrMinecartEntityId.left().isEmpty()) {
			throw new IllegalArgumentException("Type.BLOCK requires a BlockPos");
		} else if (type == SurvivalCommandBlock.Type.MINECART && blockPosOrMinecartEntityId.right().isEmpty()) {
			throw new IllegalArgumentException("Type.MINECART requires an Entity ID");
		}
	}

	public SaveSurvivalCommandBlockMessage(
			final SurvivalCommandBlockEntity survivalCommandBlockEntity,
			final String command,
			final CommandBlockEntity.Mode commandBlockMode,
			final boolean conditional,
			final boolean automatic
	) {
		this(
				survivalCommandBlockEntity.getCommandBlock().getType(),
				Either.left(survivalCommandBlockEntity.getBlockPos()),
				command,
				survivalCommandBlockEntity.getCommandBlock().isTrackOutput(),
				commandBlockMode,
				conditional,
				automatic
		);
	}

	@SuppressWarnings("deprecation")
	public static void handle(final SaveSurvivalCommandBlockMessage message, final CustomPayloadEvent.Context ctx) {
		final var player = Objects.requireNonNull(ctx.getSender());
		final var level = player.level();

		if (message.type() == SurvivalCommandBlock.Type.BLOCK) {
			SurvivalCommandBlock survivalCommandBlock = null;
			SurvivalCommandBlockEntity survivalCommandBlockEntity = null;

			final var pos = message.blockPosOrMinecartEntityId().orThrow();
			final var blockEntity = level.getBlockEntity(pos);

			if (blockEntity instanceof final SurvivalCommandBlockEntity be) {
				survivalCommandBlockEntity = be;
				survivalCommandBlock = be.getCommandBlock();
			}

			final var command = message.command();
			final var shouldTrackOutput = message.shouldTrackOutput();

			if (survivalCommandBlock != null) {
				final var mode = survivalCommandBlockEntity.getMode();
				final var state = level.getBlockState(pos);
				final var facing = state.getValue(CommandBlock.FACING);

				final var newBlock = switch (message.commandBlockMode()) {
					case SEQUENCE -> ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK;
					case AUTO -> ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK;
					default -> ModBlocks.SURVIVAL_COMMAND_BLOCK;
				};

				final var newState = newBlock.get()
						.defaultBlockState()
						.setValue(CommandBlock.FACING, facing)
						.setValue(CommandBlock.CONDITIONAL, message.conditional());

				if (newState != state) {
					level.setBlock(pos, newState, Block.UPDATE_CLIENTS);
					blockEntity.setBlockState(newState);
					level.getChunkAt(pos).setBlockEntity(blockEntity);
				}

				survivalCommandBlock.setCommand(command);
				survivalCommandBlock.setTrackOutput(shouldTrackOutput);
				if (!shouldTrackOutput) {
					survivalCommandBlock.setLastOutput(null);
				}

				survivalCommandBlockEntity.setAutomatic(message.automatic());
				if (mode != message.commandBlockMode()) {
					survivalCommandBlockEntity.onModeSwitch();
				}

				if (level.isCommandBlockEnabled()) {
					survivalCommandBlock.onUpdated(level);
				}

				if (!StringUtil.isNullOrEmpty(command)) {
					player.sendSystemMessage(Component.translatable(
							level.isCommandBlockEnabled()
									? "advMode.setCommand.success"
									: "advMode.setCommand.disabled",
							command
					));
				}
			}
		} else if (message.type == SurvivalCommandBlock.Type.MINECART) {
			throw new NotImplementedException();
		}
	}
}
