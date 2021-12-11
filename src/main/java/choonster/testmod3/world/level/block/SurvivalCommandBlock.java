package choonster.testmod3.world.level.block;

import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.util.NetworkUtil;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.StringUtil;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BaseCommandBlock;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CommandBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * A Command Block that's accessible outside Creative Mode.
 * <p>
 * Due to anti-cheat restrictions imposed by Minecraft, it's not possible to place or break this block outside of Creative Mode
 * (without reimplementing it to not extend {@link CommandBlock}).
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class SurvivalCommandBlock extends CommandBlock {
	private static final Logger LOGGER = LogManager.getLogger();

	private final CommandBlockEntity.Mode commandBlockMode;

	public SurvivalCommandBlock(final CommandBlockEntity.Mode commandBlockMode, final Block.Properties properties, final boolean automatic) {
		super(properties, automatic);
		this.commandBlockMode = commandBlockMode;
	}

	@Override
	public BlockEntity newBlockEntity(final BlockPos pos, final BlockState state) {
		final SurvivalCommandBlockEntity blockEntity = new SurvivalCommandBlockEntity(pos, state);
		blockEntity.setAutomatic(getCommandBlockMode() == CommandBlockEntity.Mode.SEQUENCE);
		return blockEntity;
	}

	@Override
	public InteractionResult use(final BlockState state, final Level world, final BlockPos pos, final Player player, final InteractionHand hand, final BlockHitResult hit) {
		final BlockEntity blockEntity = world.getBlockEntity(pos);
		if (blockEntity instanceof SurvivalCommandBlockEntity) {
			if (!player.getCommandSenderWorld().isClientSide) {
				final ServerPlayer serverPlayer = (ServerPlayer) player;
				NetworkUtil.openClientGui(serverPlayer, GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, pos);
				serverPlayer.connection.send(ClientboundBlockEntityDataPacket.create(blockEntity, BlockEntity::saveWithoutMetadata));
			}

			return InteractionResult.sidedSuccess(world.isClientSide);
		} else {
			return InteractionResult.PASS;
		}

	}

	@Override
	public void setPlacedBy(final Level level, final BlockPos pos, final BlockState state, final LivingEntity placer, final ItemStack stack) {
		final BlockEntity blockEntity = level.getBlockEntity(pos);
		if (blockEntity instanceof final SurvivalCommandBlockEntity survivalCommandBlockEntity && !level.isClientSide) {
			final CompoundTag tagCompound = stack.getTag();
			if (tagCompound == null || !tagCompound.contains("BlockEntityTag", Tag.TAG_COMPOUND)) {
				survivalCommandBlockEntity.setAutomatic(getCommandBlockMode() == CommandBlockEntity.Mode.SEQUENCE);
			}
		}

		super.setPlacedBy(level, pos, state, placer, stack);
	}

	/**
	 * Copy of {@link CommandBlock#tick} that calls {@link SurvivalCommandBlock}#execute and
	 * {@link SurvivalCommandBlock}#executeChain rather than {@link CommandBlock}#execute and
	 * {@link CommandBlock}#executeChain, removing the checks for the vanilla Command Block instances.
	 *
	 * @param world  The level
	 * @param pos    The position
	 * @param state  The block state
	 * @param random The level's RNG
	 */
	@Override
	public void tick(final BlockState state, final ServerLevel world, final BlockPos pos, final Random random) {
		final BlockEntity blockEntity = world.getBlockEntity(pos);

		if (blockEntity instanceof final CommandBlockEntity commandBlockEntity) {
			final BaseCommandBlock commandBlock = commandBlockEntity.getCommandBlock();
			final boolean hasCommand = !StringUtil.isNullOrEmpty(commandBlock.getCommand());
			final CommandBlockEntity.Mode mode = commandBlockEntity.getMode();
			final boolean conditionMet = commandBlockEntity.wasConditionMet();

			if (mode == CommandBlockEntity.Mode.AUTO) {
				commandBlockEntity.markConditionMet();

				if (conditionMet) {
					execute(state, world, pos, commandBlock, hasCommand);
				} else if (commandBlockEntity.isConditional()) {
					commandBlock.setSuccessCount(0);
				}

				if (commandBlockEntity.isPowered() || commandBlockEntity.isAutomatic()) {
					world.scheduleTick(pos, this, 1);
				}
			} else if (mode == CommandBlockEntity.Mode.REDSTONE) {
				if (conditionMet) {
					execute(state, world, pos, commandBlock, hasCommand);
				} else if (commandBlockEntity.isConditional()) {
					commandBlock.setSuccessCount(0);
				}
			}

			world.updateNeighbourForOutputSignal(pos, this);
		}
	}

	/**
	 * Trigger the Command Block and propagate the update to neighbouring Command Blocks.
	 * <p>
	 * Copy of {@link CommandBlock}#execute that calls {@link SurvivalCommandBlock}#executeChain
	 * instead of {@link CommandBlock}#executeChain.
	 *
	 * @param state             The block state
	 * @param world             The level
	 * @param pos               The position
	 * @param commandBlockLogic The Command Block Logic
	 * @param canTrigger        Can the Command Block trigger?
	 */
	private void execute(final BlockState state, final Level world, final BlockPos pos, final BaseCommandBlock commandBlockLogic, final boolean canTrigger) {
		if (canTrigger) {
			commandBlockLogic.performCommand(world);
		} else {
			commandBlockLogic.setSuccessCount(0);
		}

		executeChain(world, pos, state.getValue(FACING));
	}

	/**
	 * Propagate the update to neighbouring Command Blocks
	 *
	 * @param world  The level
	 * @param pos    This block's position
	 * @param facing The direction of the neighbour to update
	 */
	private static void executeChain(final Level world, final BlockPos pos, Direction facing) {
		final BlockPos.MutableBlockPos neighbourPos = pos.mutable();
		final GameRules gameRules = world.getGameRules();

		int i;
		BlockState neighbourState;

		for (i = gameRules.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH); i-- > 0; facing = neighbourState.getValue(FACING)) {
			neighbourPos.move(facing);
			neighbourState = world.getBlockState(neighbourPos);

			final Block block = neighbourState.getBlock();
			final BlockEntity neighbourBlockEntity = world.getBlockEntity(neighbourPos);

			if (!(neighbourBlockEntity instanceof final CommandBlockEntity neighbourCommandBlockEntity)) {
				break;
			}

			if (neighbourCommandBlockEntity.getMode() != CommandBlockEntity.Mode.SEQUENCE) {
				break;
			}

			if (neighbourCommandBlockEntity.isPowered() || neighbourCommandBlockEntity.isAutomatic()) {
				final BaseCommandBlock neighbourCommandBlockLogic = neighbourCommandBlockEntity.getCommandBlock();

				if (neighbourCommandBlockEntity.markConditionMet()) {
					if (!neighbourCommandBlockLogic.performCommand(world)) {
						break;
					}

					world.updateNeighbourForOutputSignal(neighbourPos, block);
				} else if (neighbourCommandBlockEntity.isConditional()) {
					neighbourCommandBlockLogic.setSuccessCount(0);
				}
			}
		}

		if (i <= 0) {
			final int maxCommandChainLength = Math.max(gameRules.getInt(GameRules.RULE_MAX_COMMAND_CHAIN_LENGTH), 0);
			LOGGER.warn("Command Block chain tried to execute more than {} steps!", maxCommandChainLength);
		}
	}

	public CommandBlockEntity.Mode getCommandBlockMode() {
		return commandBlockMode;
	}
}
