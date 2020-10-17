package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class BlockEventHandler {

	/**
	 * Can the tool harvest the block?
	 * <p>
	 * Adapted from {@link ForgeHooks#canToolHarvestBlock}, but has an IBlockState parameter instead of getting the IBlockState
	 * from an IBlockAccess and a BlockPos.
	 *
	 * @param state  The block's state
	 * @param stack  The tool ItemStack
	 * @param player The player harvesting the block
	 * @return True if the tool can harvest the block
	 */
	private static boolean canToolHarvestBlock(final BlockState state, final ItemStack stack, final PlayerEntity player) {
		final ToolType tool = state.getHarvestTool();
		return !stack.isEmpty() && tool != null
				&& stack.getItem().getHarvestLevel(stack, tool, player, state) >= state.getHarvestLevel();
	}

	/**
	 * Is the player harvesting a log block without the correct tool?
	 *
	 * @param state  The block's state
	 * @param player The player harvesting the block
	 * @return True if the block is a log, the player isn't in creative mode and the player doesn't have the correct tool equipped
	 */
	private static boolean isPlayerHarvestingLogWithoutCorrectTool(final BlockState state, final PlayerEntity player) {
		return !player.abilities.isCreativeMode
				&& state.isIn(BlockTags.LOGS)
				&& !canToolHarvestBlock(state, player.getHeldItemMainhand(), player);
	}

	/* TODO: Convert to Global Loot Modifier
	 * If the player harvested leaves, add two sticks to the drops list.
	 *
	 * @param event The event
	 */
//	@SubscribeEvent
//	public static void harvestDrops(final BlockEvent.HarvestDropsEvent event) {
//		if (event.getState().isIn(BlockTags.LEAVES)) {
//			event.getDrops().add(new ItemStack(Items.STICK, 2));
//		}
//	}

	/**
	 * Stop players from breaking logs without the correct tool.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void breakSpeed(final PlayerEvent.BreakSpeed event) {
		if (isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getPlayer())) {
			event.setCanceled(true);
		}
	}

	/**
	 * Stop players from breaking logs without the correct tool.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void breakBlock(final BlockEvent.BreakEvent event) {
		if (isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getPlayer())) {
			event.setCanceled(true);
		}
	}
}
