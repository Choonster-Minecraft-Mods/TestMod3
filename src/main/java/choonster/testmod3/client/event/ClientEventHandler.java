package choonster.testmod3.client.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.network.LeftClickEmptyMessage;
import choonster.testmod3.world.item.ILeftClickEmpty;
import choonster.testmod3.world.item.ModBowItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.scores.Team;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ComputeFovModifierEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.network.PacketDistributor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class ClientEventHandler {
	@SuppressWarnings("DataFlowIssue")
	private static final int FLAG_GLOWING = ObfuscationReflectionHelper.getPrivateValue(Entity.class, null, /* FLAG_GLOWING */ "f_146806_");
	private static final Method SET_SHARED_FLAG = ObfuscationReflectionHelper.findMethod(Entity.class, /* setSharedFlag */"m_20115_", int.class, boolean.class);

	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	private static final List<AbstractMinecart> glowingMinecarts = new ArrayList<>();

	@SubscribeEvent
	public static void computeFovModifier(final ComputeFovModifierEvent event) {
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
	public static void clientPostTick(final TickEvent.ClientTickEvent event) {
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
	public static void entityJoinLevel(final EntityJoinLevelEvent event) {
		final var level = event.getLevel();

		if (level.isClientSide && event.getEntity() instanceof final AbstractMinecart minecart) {
			final var scoreboard = level.getScoreboard();

			var team = scoreboard.getPlayerTeam(TestMod3.MODID);
			if (team == null) {
				team = scoreboard.addPlayerTeam(TestMod3.MODID);
				team.setPlayerPrefix(Component.literal("").setStyle(Style.EMPTY.withColor(ChatFormatting.DARK_AQUA)));
				team.setColor(ChatFormatting.DARK_AQUA);
			}

			scoreboard.addPlayerToTeam(minecart.getStringUUID(), team);
			glowingMinecarts.add(minecart);
		}
	}

	/**
	 * When an {@link AbstractMinecart} is despawned on the client side, remove it from the list of glowing minecarts.
	 */
	@SubscribeEvent
	public static void entityLeaveWorld(final EntityLeaveLevelEvent event) {
		final var level = event.getLevel();

		if (level.isClientSide && event.getEntity() instanceof final AbstractMinecart minecart) {
			glowingMinecarts.remove(minecart);
		}
	}

	/**
	 * Makes {@link AbstractMinecart} entities glow on the client.
	 */
	@SubscribeEvent
	public static void clientPreTick(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START) {
			return;
		}

		try {
			for (final var minecart : glowingMinecarts) {
				SET_SHARED_FLAG.invoke(minecart, FLAG_GLOWING, true);
			}
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to set glowing flag for Minecart", e);
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

		if (!(mainHand.getItem() instanceof final ILeftClickEmpty leftClickEmpty)) {
			return;
		}

		leftClickEmpty.onLeftClickEmpty(mainHand, player);

		TestMod3.network.send(new LeftClickEmptyMessage(), PacketDistributor.SERVER.noArg());
	}
}
