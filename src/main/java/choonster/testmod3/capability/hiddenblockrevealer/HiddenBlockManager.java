package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;

/**
 * Manages the hidden block state for the client.
 * <p>
 * Based on EnderIO's {@code crazypants.enderio.paint.YetaUtil} class.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(Side.CLIENT)
public class HiddenBlockManager {
	private static volatile boolean lastCheckResult = false;
	private static boolean toggled = false;

	/**
	 * Should either of the player's held items reveal hidden blocks?
	 *
	 * @param player The player
	 * @return Should hidden blocks be revealed?
	 */
	private static boolean shouldHeldItemRevealHiddenBlocks(final EntityPlayer player) {
		for (final EnumHand hand : EnumHand.values()) {
			final IHiddenBlockRevealer hiddenBlockRevealer = CapabilityHiddenBlockRevealer.getHiddenBlockRevealer(player.getHeldItem(hand));
			if (hiddenBlockRevealer != null && hiddenBlockRevealer.revealHiddenBlocks()) {
				return true;
			}
		}

		return false;
	}

	@SubscribeEvent
	public static void clientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		final EntityPlayer player = TestMod3.proxy.getClientPlayer();
		if (player == null) return;

		final boolean checkResult = shouldHeldItemRevealHiddenBlocks(player);
		toggled = lastCheckResult != checkResult;
		lastCheckResult = checkResult;
	}

	/**
	 * Should the client player's held item reveal hidden blocks?
	 * <p>
	 * This method should only be called on the physical client.
	 *
	 * @return Should the client player's held item reveal hidden blocks?
	 */
	public static boolean shouldHeldItemRevealHiddenBlocksClient() {
		return lastCheckResult;
	}

	/**
	 * Update the chunk containing the {@link BlockPos} if the hidden state has changed.
	 *
	 * @param world The world
	 * @param pos   The position of the hidden block to update
	 */
	public static void refresh(final World world, final BlockPos pos) {
		if (toggled) {
			final IBlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
}
