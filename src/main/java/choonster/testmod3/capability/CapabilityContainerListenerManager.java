package choonster.testmod3.capability;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

/**
 * Manages the {@link IContainerListener}s that handle syncing of each item capability.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber
public class CapabilityContainerListenerManager {
	/**
	 * The {@link CapabilityContainerListener} factories.
	 */
	private static final Set<Function<EntityPlayerMP, CapabilityContainerListener<?>>> containerListenerFactories = new HashSet<>();

	/**
	 * Register a factory for a {@link CapabilityContainerListener}.
	 *
	 * @param factory The factory
	 */
	public static void registerListenerFactory(Function<EntityPlayerMP, CapabilityContainerListener<?>> factory) {
		containerListenerFactories.add(factory);
	}

	@Mod.EventBusSubscriber
	@SuppressWarnings("unused")
	private static class EventHandler {

		/**
		 * Add the listeners to a {@link Container}.
		 *
		 * @param player    The player
		 * @param container The Container
		 */
		private static void addListeners(EntityPlayerMP player, Container container) {
			containerListenerFactories.forEach(
					factory -> container.addListener(factory.apply(player))
			);
		}

		/**
		 * Add the listeners to {@link EntityPlayer#inventoryContainer} when an {@link EntityPlayerMP} logs in.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerLoggedIn(final PlayerLoggedInEvent event) {
			if (event.player instanceof EntityPlayerMP) {
				final EntityPlayerMP player = (EntityPlayerMP) event.player;
				addListeners(player, player.inventoryContainer);
			}
		}

		/**
		 * Add the listeners to {@link EntityPlayer#inventoryContainer} when an {@link EntityPlayerMP} is cloned.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerClone(final PlayerEvent.Clone event) {
			if (event.getEntityPlayer() instanceof EntityPlayerMP) {
				final EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
				addListeners(player, player.inventoryContainer);
			}
		}

		/**
		 * Add the listeners to a {@link Container} when it's opened by an {@link EntityPlayerMP}.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void containerOpen(final PlayerContainerEvent.Open event) {
			if (event.getEntityPlayer() instanceof EntityPlayerMP) {
				final EntityPlayerMP player = (EntityPlayerMP) event.getEntityPlayer();
				addListeners(player, event.getContainer());
			}
		}
	}
}
