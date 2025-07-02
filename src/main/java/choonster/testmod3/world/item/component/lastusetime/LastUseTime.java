package choonster.testmod3.world.item.component.lastusetime;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModDataComponents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;

/**
 * Helpers for {@link LastUseTimeProperties} components.
 *
 * @author Choonster
 */
public class LastUseTime {
	/**
	 * Update the last use time of the player's held item.
	 *
	 * @param player    The player
	 * @param itemStack The held item
	 */
	public static void updateLastUseTime(final Player player, final ItemStack itemStack) {
		final var currentProperties = Objects.requireNonNull(itemStack.get(ModDataComponents.LAST_USE_TIME_PROPERTIES.get()));

		updateLastUseTime(player, itemStack, currentProperties);
	}

	/**
	 * Update the last use time of the player's held item.
	 *
	 * @param player            The player
	 * @param itemStack         The held item
	 * @param currentProperties The current properties of the item
	 */
	public static void updateLastUseTime(final Player player, final ItemStack itemStack, final LastUseTimeProperties currentProperties) {
		final var level = Objects.requireNonNull(player.level());
		final var newProperties = new LastUseTimeProperties(level.getGameTime(), currentProperties.automaticUpdates());

		itemStack.set(ModDataComponents.LAST_USE_TIME_PROPERTIES.get(), newProperties);
	}

	/**
	 * Event handler for the {@link LastUseTimeProperties} component.
	 */
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class EventHandlerLUT {
		/**
		 * Update the {@link LastUseTimeProperties} of the player's held item when they right-click.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(final PlayerInteractEvent.RightClickItem event) {
			final var itemStack = event.getItemStack();

			final var properties = itemStack.get(ModDataComponents.LAST_USE_TIME_PROPERTIES.get());

			if (properties != null && properties.automaticUpdates()) {
				LastUseTime.updateLastUseTime(event.getEntity(), itemStack, properties);
			}
		}
	}
}
