package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
				&& !ForgeHooks.canHarvestBlock(state, player, player.level, player.blockPosition());
	}

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
