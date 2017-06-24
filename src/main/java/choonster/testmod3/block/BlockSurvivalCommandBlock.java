package choonster.testmod3.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.CommandBlockBaseLogic;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.StringUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.GameRules;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * A Command Block that's accessible outside of Creative Mode.
 * <p>
 * Due to anti-cheat restrictions imposed by Minecraft, it's not possible to place or break this block outside of Creative Mode
 * (without reimplementing it to not extend {@link BlockCommandBlock}).
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2540671-command-block-replica-issue
 *
 * @author Choonster
 */
public class BlockSurvivalCommandBlock extends BlockCommandBlock {
	private static final Logger LOGGER = LogManager.getLogger();

	private final TileEntityCommandBlock.Mode commandBlockMode;

	public BlockSurvivalCommandBlock(final TileEntityCommandBlock.Mode commandBlockMode, final String name) {
		super(MapColor.BROWN);
		this.commandBlockMode = commandBlockMode;
		setCreativeTab(TestMod3.creativeTab);
		BlockTestMod3.setBlockName(this, name);
		setHardness(5);
	}

	@Override
	public TileEntity createNewTileEntity(final World worldIn, final int meta) {
		final TileEntitySurvivalCommandBlock tileEntity = new TileEntitySurvivalCommandBlock();
		tileEntity.setAuto(getCommandBlockMode() == TileEntityCommandBlock.Mode.SEQUENCE);
		return tileEntity;
	}

	@Override
	public boolean onBlockActivated(final World worldIn, final BlockPos pos, final IBlockState state, final EntityPlayer playerIn, final EnumHand hand, final EnumFacing side, final float hitX, final float hitY, final float hitZ) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		return tileEntity instanceof TileEntitySurvivalCommandBlock && ((TileEntitySurvivalCommandBlock) tileEntity).getCommandBlockLogic().tryOpenEditCommandBlock(playerIn);
	}

	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack) {
		final TileEntity tileEntity = worldIn.getTileEntity(pos);
		if (tileEntity instanceof TileEntitySurvivalCommandBlock && !worldIn.isRemote) {
			final TileEntitySurvivalCommandBlock tileEntitySurvivalCommandBlock = (TileEntitySurvivalCommandBlock) tileEntity;

			final NBTTagCompound tagCompound = stack.getTagCompound();
			if (tagCompound == null || !tagCompound.hasKey("BlockEntityTag", Constants.NBT.TAG_COMPOUND)) {
				tileEntitySurvivalCommandBlock.setAuto(getCommandBlockMode() == TileEntityCommandBlock.Mode.SEQUENCE);
			}
		}

		super.onBlockPlacedBy(worldIn, pos, state, placer, stack);
	}

	@Override
	public int quantityDropped(final Random random) {
		return 1;
	}

	/**
	 * Copy of {@link BlockCommandBlock#updateTick} that calls {@link BlockSurvivalCommandBlock#execute} and
	 * {@link BlockSurvivalCommandBlock#executeChain} rather than {@link BlockCommandBlock#execute} and
	 * {@link BlockCommandBlock#executeChain}, removing the checks for the vanilla Command Block instances.
	 *
	 * @param world The World
	 * @param pos   The position
	 * @param state The block state
	 * @param rand  The World's RNG
	 */
	@Override
	public void updateTick(final World world, final BlockPos pos, final IBlockState state, final Random rand) {
		if (!world.isRemote) {
			final TileEntity tileentity = world.getTileEntity(pos);

			if (tileentity instanceof TileEntityCommandBlock) {
				final TileEntityCommandBlock tileEntityCommandBlock = (TileEntityCommandBlock) tileentity;
				final CommandBlockBaseLogic commandBlockLogic = tileEntityCommandBlock.getCommandBlockLogic();
				final boolean hasCommand = !StringUtils.isNullOrEmpty(commandBlockLogic.getCommand());
				final TileEntityCommandBlock.Mode mode = tileEntityCommandBlock.getMode();
				final boolean conditionMet = tileEntityCommandBlock.isConditionMet();

				if (mode == TileEntityCommandBlock.Mode.AUTO) {
					tileEntityCommandBlock.setConditionMet();

					if (conditionMet) {
						this.execute(state, world, pos, commandBlockLogic, hasCommand);
					} else if (tileEntityCommandBlock.isConditional()) {
						commandBlockLogic.setSuccessCount(0);
					}

					if (tileEntityCommandBlock.isPowered() || tileEntityCommandBlock.isAuto()) {
						world.scheduleUpdate(pos, this, this.tickRate(world));
					}
				} else if (mode == TileEntityCommandBlock.Mode.REDSTONE) {
					if (conditionMet) {
						this.execute(state, world, pos, commandBlockLogic, hasCommand);
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
	 * Copy of {@link BlockCommandBlock#execute} that calls {@link BlockSurvivalCommandBlock#executeChain}
	 * instead of {@link BlockCommandBlock#executeChain}.
	 *
	 * @param state             The block state
	 * @param world             The World
	 * @param pos               The position
	 * @param commandBlockLogic The Command Block Logic
	 * @param hasCommand        Does the Command Block have a command?
	 */
	private void execute(final IBlockState state, final World world, final BlockPos pos, final CommandBlockBaseLogic commandBlockLogic, final boolean hasCommand) {
		if (hasCommand) {
			commandBlockLogic.trigger(world);
		} else {
			commandBlockLogic.setSuccessCount(0);
		}

		executeChain(world, pos, state.getValue(FACING));
	}

	/**
	 * Propagate the update to neighbouring Command Blocks
	 *
	 * @param world  The World
	 * @param pos    This block's position
	 * @param facing The direction of the neighbour to update
	 */
	private static void executeChain(final World world, final BlockPos pos, EnumFacing facing) {
		final BlockPos.MutableBlockPos neighbourPos = new BlockPos.MutableBlockPos(pos);
		final GameRules gameRules = world.getGameRules();

		int i;
		IBlockState neighbourState;

		for (i = gameRules.getInt("maxCommandChainLength"); i-- > 0; facing = neighbourState.getValue(FACING)) {
			neighbourPos.move(facing);
			neighbourState = world.getBlockState(neighbourPos);

			final Block block = neighbourState.getBlock();
			final TileEntity neighbourTileEntity = world.getTileEntity(neighbourPos);

			if (!(neighbourTileEntity instanceof TileEntityCommandBlock)) {
				break;
			}

			final TileEntityCommandBlock neighbourTileEntityCommandBlock = (TileEntityCommandBlock) neighbourTileEntity;

			if (neighbourTileEntityCommandBlock.getMode() != TileEntityCommandBlock.Mode.SEQUENCE) {
				break;
			}

			if (neighbourTileEntityCommandBlock.isPowered() || neighbourTileEntityCommandBlock.isAuto()) {
				final CommandBlockBaseLogic neighbourCommandBlockLogic = neighbourTileEntityCommandBlock.getCommandBlockLogic();

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
			final int maxCommandChainLength = Math.max(gameRules.getInt("maxCommandChainLength"), 0);
			LOGGER.warn("Command Block chain tried to execute more than {} steps!", maxCommandChainLength);
		}
	}

	public TileEntityCommandBlock.Mode getCommandBlockMode() {
		return commandBlockMode;
	}
}
