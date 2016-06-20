package choonster.testmod3.event;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class BlockEventHandler {

	/**
	 * Can the tool harvest the block?
	 * <p>
	 * Adapted from ForgeHooks.canToolHarvestBlock, but has an IBlockState parameter instead of getting the IBlockState
	 * from an IBlockAccess and a BlockPos.
	 *
	 * @param state The block's state
	 * @param stack The tool ItemStack
	 * @return True if the tool can harvest the block
	 */
	private boolean canToolHarvestBlock(IBlockState state, ItemStack stack) {
		final String tool = state.getBlock().getHarvestTool(state);
		return stack != null && tool != null
				&& stack.getItem().getHarvestLevel(stack, tool) >= state.getBlock().getHarvestLevel(state);
	}

	/**
	 * Is the player harvesting a log block without the correct tool?
	 *
	 * @param state       The block's state
	 * @param blockAccess The world that the block is in
	 * @param pos         The position of the block
	 * @param player      The player harvesting the block
	 * @return True if the block is a log, the player isn't in creative mode and the player doesn't have the correct tool equipped
	 */
	private boolean isPlayerHarvestingLogWithoutCorrectTool(IBlockState state, IBlockAccess blockAccess, BlockPos pos, EntityPlayer player) {
		return player != null && !player.capabilities.isCreativeMode
				&& state.getBlock().isWood(blockAccess, pos)
				&& !canToolHarvestBlock(state, player.getHeldItemMainhand());
	}

	/**
	 * If the player harvested leaves, add two sticks to the drops list.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public void harvestDrops(BlockEvent.HarvestDropsEvent event) {
		if (event.getState().getBlock().isLeaves(event.getState(), event.getWorld(), event.getPos())) {
			event.getDrops().add(new ItemStack(Items.STICK, 2));
		}
	}

	/**
	 * Stop players from breaking logs without the correct tool.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public void breakSpeed(PlayerEvent.BreakSpeed event) {
		if (isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getEntityPlayer().getEntityWorld(), event.getPos(), event.getEntityPlayer())) {
			event.setCanceled(true);
		}
	}

	/**
	 * Stop players from breaking logs without the correct tool.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public void breakBlock(BlockEvent.BreakEvent event) {
		if (isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getWorld(), event.getPos(), event.getPlayer())) {
			event.setCanceled(true);
		}
	}
}
