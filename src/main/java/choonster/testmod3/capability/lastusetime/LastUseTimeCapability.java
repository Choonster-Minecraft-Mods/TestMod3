package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.LongNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability for {@link ILastUseTime}.
 *
 * @author Choonster
 */
public final class LastUseTimeCapability {
	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(ILastUseTime.class)
	public static final Capability<ILastUseTime> LAST_USE_TIME_CAPABILITY = Null();

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "last_use_time");

	public static void register() {
		CapabilityManager.INSTANCE.register(ILastUseTime.class, new Capability.IStorage<ILastUseTime>() {
			@Override
			public INBT writeNBT(final Capability<ILastUseTime> capability, final ILastUseTime instance, final Direction side) {
				return new LongNBT(instance.get());
			}

			@Override
			public void readNBT(final Capability<ILastUseTime> capability, final ILastUseTime instance, final Direction side, final INBT nbt) {
				instance.set(((LongNBT) nbt).getLong());
			}
		}, () -> new LastUseTime(true));

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
	public static void updateLastUseTime(final PlayerEntity player, final ItemStack itemStack) {
		getLastUseTime(itemStack).ifPresent((lastUseTime) -> {
			final World world = player.getEntityWorld();

			lastUseTime.set(world.getGameTime());
		});
	}

	/**
	 * Create a provider for the default {@link ILastUseTime} instance.
	 *
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider() {
		return new SerializableCapabilityProvider<>(LAST_USE_TIME_CAPABILITY, DEFAULT_FACING);
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
		 * Update the {@link ILastUseTime} of the player's held item when they right click.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(final PlayerInteractEvent.RightClickItem event) {
			final ItemStack itemStack = event.getItemStack();

			getLastUseTime(itemStack).ifPresent(lastUseTime -> {
				if (lastUseTime.automaticUpdates()) {
					updateLastUseTime(event.getPlayer(), itemStack);
				}
			});
		}
	}

	/**
	 * {@link IItemPropertyGetter} to get the ticks since the last use of the item. Returns {@link Float#MAX_VALUE} if the required information isn't available.
	 */
	public static class TicksSinceLastUseGetter {
		/**
		 * The ID of this getter.
		 */
		public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "ticks_since_last_use");

		/**
		 * The getter.
		 */
		private static final IItemPropertyGetter GETTER = (stack, worldIn, entityIn) ->
		{
			final World world = worldIn != null ? worldIn : entityIn != null ? entityIn.getEntityWorld() : null;

			if (world == null) {
				return Float.MAX_VALUE;
			}

			return getLastUseTime(stack)
					.map(lastUseTime -> (float) world.getGameTime() - lastUseTime.get())
					.orElse(Float.MAX_VALUE);
		};

		/**
		 * Add this getter to an {@link Item}.
		 *
		 * @param item The item
		 */
		public static void addToItem(final Item item) {
			item.addPropertyOverride(ID, GETTER);
		}
	}
}
