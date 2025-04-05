package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
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
		if (!(event.getEntity() instanceof final ServerPlayer player)) {
			return;
		}

		final var entityData = player.getPersistentData();
		final var persistedData = entityData.getCompoundOrEmpty(ServerPlayer.PERSISTED_NBT_TAG);
		entityData.put(ServerPlayer.PERSISTED_NBT_TAG, persistedData);

		final var key = ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "received_items").toString();
		final TestMod3Lang message;

		if (persistedData.getBooleanOr(key, false)) {
			message = TestMod3Lang.MESSAGE_LOGIN_ALREADY_RECEIVED;
		} else {
			persistedData.putBoolean(key, true);

			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.APPLE));

			message = TestMod3Lang.MESSAGE_LOGIN_FREE_APPLE;
		}

		final var textComponent = Component.translatable(message.getTranslationKey());
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
		if (event.getEntity() instanceof final ServerPlayer player && !event.getEntity().level().isClientSide) {
			final var pos = player.blockPosition();
			player.sendSystemMessage(Component.translatable(
					TestMod3Lang.MESSAGE_DEATH_COORDINATES.getTranslationKey(),
					pos.getX(),
					pos.getY(),
					pos.getZ(),
					player.level().dimension()
			));
		}
	}
}
