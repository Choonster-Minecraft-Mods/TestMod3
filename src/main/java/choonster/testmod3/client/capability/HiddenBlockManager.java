package choonster.testmod3.client.capability;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockRevealerCapability;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.InteractionHand;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Manages the hidden block state for the client.
 * <p>
 * Based on EnderIO's {@code crazypants.enderio.paint.YetaUtil} class.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class HiddenBlockManager {
	private static volatile boolean lastCheckResult = false;
	private static boolean toggled = false;

	/**
	 * Should either of the player's held items reveal hidden blocks?
	 *
	 * @param player The player
	 * @return Should hidden blocks be revealed?
	 */
	private static boolean shouldHeldItemRevealHiddenBlocks(final Player player) {
		for (final InteractionHand hand : InteractionHand.values()) {
			final boolean revealHiddenBlocks = HiddenBlockRevealerCapability.getHiddenBlockRevealer(player.getItemInHand(hand))
					.map(IHiddenBlockRevealer::revealHiddenBlocks)
					.orElse(false);

			if (revealHiddenBlocks) {
				return true;
			}
		}

		return false;
	}

	@SubscribeEvent
	public static void clientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		final LocalPlayer player = Minecraft.getInstance().player;
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
	public static void refresh(final Level world, final BlockPos pos) {
		if (toggled) {
			final BlockState state = world.getBlockState(pos);
			world.sendBlockUpdated(pos, state, state, 3);
		}
	}
}
