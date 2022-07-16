package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.util.CapabilityNotPresentException;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Capability for {@link ILastUseTime}.
 *
 * @author Choonster
 */
public final class LastUseTimeCapability {
	/**
	 * The {@link Capability} instance.
	 */
	public static final Capability<ILastUseTime> LAST_USE_TIME_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "last_use_time");

	public static void register(final RegisterCapabilitiesEvent event) {
		event.register(ILastUseTime.class);

		CapabilityContainerListenerManager.registerListenerFactory(LastUseTimeContainerListener::new);
	}

	/**
	 * Get the {@link ILastUseTime} from the specified {@link ItemStack}'s capabilities, if any.
	 *
	 * @param itemStack The ItemStack
	 * @return A lazy optional containing the ILastUseTime, if any
	 */
	public static LazyOptional<ILastUseTime> getLastUseTime(final ItemStack itemStack) {
		return itemStack.getCapability(LAST_USE_TIME_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Update the last use time of the player's held item.
	 *
	 * @param player    The player
	 * @param itemStack The held ItemStack
	 */
	public static void updateLastUseTime(final Player player, final ItemStack itemStack) {
		getLastUseTime(itemStack)
				.orElseThrow(CapabilityNotPresentException::new)
				.set(player.getCommandSenderWorld().getGameTime());
	}

	/**
	 * Create a provider for the specified {@link ILastUseTime} instance.
	 *
	 * @param lastUseTime The ILastUseTime
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(final ILastUseTime lastUseTime) {
		return new SerializableCapabilityProvider<>(LAST_USE_TIME_CAPABILITY, DEFAULT_FACING, lastUseTime);
	}

	/**
	 * Event handler for the {@link ILastUseTime} capability.
	 */
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class EventHandler {
		/**
		 * Update the {@link ILastUseTime} of the player's held item when they right-click.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(final PlayerInteractEvent.RightClickItem event) {
			final var itemStack = event.getItemStack();

			getLastUseTime(itemStack).ifPresent(lastUseTime -> {
				if (lastUseTime.automaticUpdates()) {
					updateLastUseTime(event.getEntity(), itemStack);
				}
			});
		}
	}

}
