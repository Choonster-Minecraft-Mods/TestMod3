package choonster.testmod3.block;

import choonster.testmod3.tileentity.SurvivalCommandBlockTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CommandBlockBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.CommandBlockLogic;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.GameRules;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Due to anti-cheat restrictions imposed by Minecraft, it's not possible to place or break this block outside of Creative Mode
 * (without reimplementing it to not extend {@link CommandBlockBlock}).
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class SurvivalCommandBlockBlock extends CommandBlockBlock {
	private static final Logger LOGGER = LogManager.getLogger();

	private final CommandBlockTileEntity.Mode commandBlockMode;

	public SurvivalCommandBlockBlock(final CommandBlockTileEntity.Mode commandBlockMode, final Block.Properties properties) {
		super(properties);
		this.commandBlockMode = commandBlockMode;
	}

	@Nullable
	@Override
	public TileEntity createTileEntity(final BlockState state, final IBlockReader world) {
		final SurvivalCommandBlockTileEntity tileEntity = new SurvivalCommandBlockTileEntity();
		tileEntity.setAuto(getCommandBlockMode() == CommandBlockTileEntity.Mode.SEQUENCE);
		return tileEntity;
	}

	@Override
	public boolean onBlockActivated(final BlockState state, final World world, final BlockPos pos, final PlayerEntity player, final Hand hand, final BlockRayTraceResult rayTraceResult) {
		final TileEntity tileEntity = world.getTileEntity(pos);
		return tileEntity instanceof SurvivalCommandBlockTileEntity && ((SurvivalCommandBlockTileEntity) tileEntity).getCommandBlockLogic().tryOpenEditCommandBlock(player);
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final BlockState state, final LivingEntity placer, final ItemStack stack) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof SurvivalCommandBlockTileEntity && !worldIn.isRemote) {
			final SurvivalCommandBlockTileEntity survivalCommandBlockTileEntity = (SurvivalCommandBlockTileEntity) tileEntity;

			final CompoundNBT tagCompound = stack.getTag();
			if (tagCompound == null || !tagCompound.contains("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
				survivalCommandBlockTileEntity.setAuto(getCommandBlockMode() == CommandBlockTileEntity.Mode.SEQUENCE);
			}
		}

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public int quantityDropped(final BlockState state, final Random random) {
		return 1;
	}

	/**
	 * Copy of {@link CommandBlockBlock#tick} that calls {@link SurvivalCommandBlockBlock#execute} and
	 * {@link SurvivalCommandBlockBlock#executeChain} rather than {@link CommandBlockBlock#execute} and
	 * {@link CommandBlockBlock#executeChain}, removing the checks for the vanilla Command Block instances.
	 *
	 * @param world  The World
	 * @param pos    The position
	 * @param state  The block state
	 * @param random The World's RNG
	 */
	@Override
	public void tick(final BlockState state, final World world, final BlockPos pos, final Random random) {
		if (!world.isRemote) {
			final TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof CommandBlockTileEntity) {
				final CommandBlockTileEntity tileEntityCommandBlock = (CommandBlockTileEntity) tileentity;
				final CommandBlockLogic commandBlockLogic = tileEntityCommandBlock.getCommandBlockLogic();
				final boolean hasCommand = !StringUtils.isNullOrEmpty(commandBlockLogic.getCommand());
				final CommandBlockTileEntity.Mode mode = tileEntityCommandBlock.getMode();
				final boolean conditionMet = tileEntityCommandBlock.isConditionMet();

				if (mode == CommandBlockTileEntity.Mode.AUTO) {
					tileEntityCommandBlock.setConditionMet();

					if (conditionMet) {
						execute(state, world, pos, commandBlockLogic, hasCommand);
					} else if (tileEntityCommandBlock.isConditional()) {
						commandBlockLogic.setSuccessCount(0);
					}

					if (tileEntityCommandBlock.isPowered() || tileEntityCommandBlock.isAuto()) {
						world.getPendingBlockTicks().scheduleTick(pos, this, tickRate(world));
					}
				} else if (mode == CommandBlockTileEntity.Mode.REDSTONE) {
					if (conditionMet) {
						execute(state, world, pos, commandBlockLogic, hasCommand);
					} else if (tileEntityCommandBlock.isConditional()) {
						commandBlockLogic.setSuccessCount(0);
					}
				}

				world.updateComparatorOutputLevel(pos, this);
			}
		}
	}

	/**
	 * Trigger the Command Block and propagate the update to neighbouring Command Blocks.
	 * <p>
	 * Copy of {@link CommandBlockBlock#execute} that calls {@link SurvivalCommandBlockBlock#executeChain}
	 * instead of {@link CommandBlockBlock#executeChain}.
	 *
	 * @param state             The block state
	 * @param world             The World
	 * @param pos               The position
	 * @param commandBlockLogic The Command Block Logic
	 * @param hasCommand        Does the Command Block have a command?
	 */
	private void execute(final BlockState state, final World world, final BlockPos pos, final CommandBlockLogic commandBlockLogic, final boolean hasCommand) {
		if (hasCommand) {
			commandBlockLogic.trigger(world);
		} else {
			commandBlockLogic.setSuccessCount(0);
		}

		executeChain(world, pos, state.get(FACING));
	}

	/**
	 * Propagate the update to neighbouring Command Blocks
	 *
	 * @param world  The World
	 * @param pos    This block's position
	 * @param facing The direction of the neighbour to update
	 */
	private static void executeChain(final World world, final BlockPos pos, Direction facing) {
		final BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos(pos);
		final GameRules gameRules = world.getGameRules();

		int i;
		BlockState neighbourState;

		for (i = gameRules.getInt(GameRules.MAX_COMMAND_CHAIN_LENGTH); i-- > 0; facing = neighbourState.get(FACING)) {
			neighbourPos.move(facing);
			neighbourState = world.getBlockState(neighbourPos);

			final Block block = neighbourState.getBlock();
			final TileEntity neighbourTileEntity = world.getTileEntity(neighbourPos);

			if (!(neighbourTileEntity instanceof CommandBlockTileEntity)) {
				break;
			}

			final CommandBlockTileEntity neighbourTileEntityCommandBlock = (CommandBlockTileEntity) neighbourTileEntity;

			if (neighbourTileEntityCommandBlock.getMode() != CommandBlockTileEntity.Mode.SEQUENCE) {
				break;
			}

			if (neighbourTileEntityCommandBlock.isPowered() || neighbourTileEntityCommandBlock.isAuto()) {
				final CommandBlockLogic neighbourCommandBlockLogic = neighbourTileEntityCommandBlock.getCommandBlockLogic();

				if (neighbourTileEntityCommandBlock.setConditionMet()) {
					if (!neighbourCommandBlockLogic.trigger(world)) {
						break;
					}

					world.updateComparatorOutputLevel(neighbourPos, block);
				} else if (neighbourTileEntityCommandBlock.isConditional()) {
					neighbourCommandBlockLogic.setSuccessCount(0);
				}
			}
		}

		if (i <= 0) {
			final int maxCommandChainLength = Math.max(gameRules.getInt(GameRules.MAX_COMMAND_CHAIN_LENGTH), 0);
			LOGGER.warn("Command Block chain tried to execute more than {} steps!", maxCommandChainLength);
		}
	}

	public CommandBlockTileEntity.Mode getCommandBlockMode() {
		return commandBlockMode;
	}
}
