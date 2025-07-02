package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class BlockEventHandler {

	/**
	 * Is the player harvesting a log block without the correct tool?
	 *
	 * @param state  The block's state
	 * @param player The player harvesting the block
	 * @return True if the block is a log, the player isn't in creative mode and the player doesn't have the correct tool equipped
	 */
	private static boolean isPlayerHarvestingLogWithoutCorrectTool(final BlockState state, final Player player) {
		return !player.getAbilities().instabuild
				&& state.is(BlockTags.LOGS)
				&& !ForgeHooks.isCorrectToolForDrops(state, player);
	}

	/**
	 * Stop players from breaking logs without the correct tool.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static boolean breakSpeed(final PlayerEvent.BreakSpeed event) {
		return isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getEntity());
	}

	/**
	 * Stop players from breaking logs without the correct tool.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static boolean breakBlock(final BlockEvent.BreakEvent event) {
		return isPlayerHarvestingLogWithoutCorrectTool(event.getState(), event.getPlayer());
	}
}
