package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Handler for player-related events.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class PlayerEventHandler {

	/**
	 * Give the player an Apple when they first log in.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,36355.0.html
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void playerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
		final var player = event.getEntity();

		final var entityData = player.getPersistentData();
		final var persistedData = entityData.getCompound(Player.PERSISTED_NBT_TAG);
		entityData.put(Player.PERSISTED_NBT_TAG, persistedData);

		final var key = Constants.RESOURCE_PREFIX + "ReceivedItems";
		final TestMod3Lang message;

		if (persistedData.getBoolean(key)) {
			message = TestMod3Lang.MESSAGE_LOGIN_ALREADY_RECEIVED;
		} else {
			persistedData.putBoolean(key, true);

			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.APPLE));

			message = TestMod3Lang.MESSAGE_LOGIN_FREE_APPLE;
		}

		final Component textComponent = Component.translatable(message.getTranslationKey());
		textComponent.getStyle().withColor(ChatFormatting.LIGHT_PURPLE);
		player.sendSystemMessage(textComponent);
	}

	/**
	 * When a player dies, tell them their coordinates.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void livingDeath(final LivingDeathEvent event) {
		if (event.getEntity() instanceof final Player player && !event.getEntity().getCommandSenderWorld().isClientSide) {
			final var pos = player.blockPosition();
			player.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_DEATH_COORDINATES.getTranslationKey(), pos.getX(), pos.getY(), pos.getZ(), player.level.dimension()));
		}
	}
}
