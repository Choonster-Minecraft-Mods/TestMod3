package choonster.testmod3.capability.hiddenblockrevealer;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.hiddenblockrevealer.IHiddenBlockRevealer;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
	private static boolean shouldHeldItemRevealHiddenBlocks(final PlayerEntity player) {
		for (final Hand hand : Hand.values()) {
			final boolean revealHiddenBlocks = HiddenBlockRevealerCapability.getHiddenBlockRevealer(player.getHeldItem(hand))
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

		DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> {
			final PlayerEntity player = Minecraft.getInstance().player;
			if (player == null) return;

			final boolean checkResult = shouldHeldItemRevealHiddenBlocks(player);
			toggled = lastCheckResult != checkResult;
			lastCheckResult = checkResult;
		});
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
			final BlockState state = world.getBlockState(pos);
			world.notifyBlockUpdate(pos, state, state, 3);
		}
	}
}
