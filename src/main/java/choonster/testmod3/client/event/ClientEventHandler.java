package choonster.testmod3.client.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.LeftClickEmptyMessage;
import choonster.testmod3.world.item.ILeftClickEmpty;
import choonster.testmod3.world.item.ModBowItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.scores.Team;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class ClientEventHandler {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	@SubscribeEvent
	public static void onFOVUpdate(final ComputeFovModifierEvent event) {
		if (event.getPlayer().isUsingItem() && event.getPlayer().getUseItem().getItem() instanceof ModBowItem) {
			var fovModifier = event.getPlayer().getTicksUsingItem() / 20.0f;

			if (fovModifier > 1.0f) {
				fovModifier = 1.0f;
			} else {
				fovModifier *= fovModifier;
			}

			event.setNewFovModifier(event.getNewFovModifier() * (1.0f - fovModifier * 0.15f));
		}
	}

	/**
	 * Rotate the player every tick while they're standing on a Block of Iron.
	 * <p>
	 * Test for this thread:
	 * https://www.minecraftforge.net/forum/topic/35880-solved189-multiplayer-anti-afk/
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void onClientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase == TickEvent.Phase.END && MINECRAFT.player != null && MINECRAFT.level != null) {
			final Player player = MINECRAFT.player;
			if (MINECRAFT.level.getBlockState(player.blockPosition().below()).getBlock() == Blocks.IRON_BLOCK) {
				player.turn(5, 0);
			}
		}
	}

	/**
	 * When an {@link AbstractMinecart} is spawned on the client side, add it to a {@link Team} and make it glow.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/topic/50836-adding-an-entity-other-than-a-player-to-a-team/
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void entityJoinWorld(final EntityJoinLevelEvent event) {
		final var level = event.getLevel();
		final var entity = event.getEntity();

		if (level.isClientSide && entity instanceof AbstractMinecart) {
			final var scoreboard = level.getScoreboard();

			var team = scoreboard.getPlayerTeam(TestMod3.MODID);
			if (team == null) {
				team = scoreboard.addPlayerTeam(TestMod3.MODID);
				team.setPlayerPrefix(Component.literal("").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
				team.setColor(ChatFormatting.DARK_AQUA);
			}

			scoreboard.addPlayerToTeam(entity.getStringUUID(), team);
			entity.setGlowingTag(true); // TODO: Doesn't look like this will work on the client
		}
	}

	/**
	 * When a {@link ILeftClickEmpty} item is left-clicked on empty space, call {@link ILeftClickEmpty#onLeftClickEmpty}
	 * and send {@link LeftClickEmptyMessage} to the server.
	 *
	 * @param event The event
	 */
	@SuppressWarnings("InstantiationOfUtilityClass")
	@SubscribeEvent
	public static void leftClickEmpty(final PlayerInteractEvent.LeftClickEmpty event) {
		final var player = event.getEntity();
		final var mainHand = player.getMainHandItem();

		if (!(mainHand.getItem() instanceof ILeftClickEmpty leftClickEmpty)) {
			return;
		}

		leftClickEmpty.onLeftClickEmpty(mainHand, player);

		TestMod3.network.sendToServer(new LeftClickEmptyMessage());
	}
}
