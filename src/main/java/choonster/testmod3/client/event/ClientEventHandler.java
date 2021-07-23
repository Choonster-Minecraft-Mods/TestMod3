package choonster.testmod3.client.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.item.ModBowItem;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.minecart.AbstractMinecartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.scoreboard.ScorePlayerTeam;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.Team;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class ClientEventHandler {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	@SubscribeEvent
	public static void onFOVUpdate(final FOVUpdateEvent event) {
		if (event.getEntity().isUsingItem() && event.getEntity().getUseItem().getItem() instanceof ModBowItem) {
			float fovModifier = event.getEntity().getTicksUsingItem() / 20.0f;

			if (fovModifier > 1.0f) {
				fovModifier = 1.0f;
			} else {
				fovModifier *= fovModifier;
			}

			event.setNewfov(event.getFov() * (1.0f - fovModifier * 0.15f));
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
			final PlayerEntity player = MINECRAFT.player;
			if (MINECRAFT.level.getBlockState(player.blockPosition().below()).getBlock() == Blocks.IRON_BLOCK) {
				player.turn(5, 0);
			}
		}
	}

	/**
	 * When an {@link AbstractMinecartEntity} is spawned on the client side, add it to a {@link Team} and make it glow.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/topic/50836-adding-an-entity-other-than-a-player-to-a-team/
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void entityJoinWorld(final EntityJoinWorldEvent event) {
		final World world = event.getWorld();
		final Entity entity = event.getEntity();

		if (world.isClientSide && entity instanceof AbstractMinecartEntity) {
			final Scoreboard scoreboard = world.getScoreboard();

			ScorePlayerTeam team = scoreboard.getPlayerTeam(TestMod3.MODID);
			if (team == null) {
				team = scoreboard.addPlayerTeam(TestMod3.MODID);
				team.setPlayerPrefix(new StringTextComponent("").setStyle(Style.EMPTY.withColor(TextFormatting.DARK_AQUA)));
				team.setColor(TextFormatting.DARK_AQUA);
			}

			scoreboard.addPlayerToTeam(entity.getStringUUID(), team);
			entity.setGlowing(true);
		}
	}
}
