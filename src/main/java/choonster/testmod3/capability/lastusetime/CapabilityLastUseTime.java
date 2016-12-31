package choonster.testmod3.capability.lastusetime;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.item.IItemPropertyGetterFix;
import choonster.testmod3.network.MessageUpdateHeldLastUseTime;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import javax.annotation.Nullable;

/**
 * Capability for {@link ILastUseTime}.
 *
 * @author Choonster
 */
public class CapabilityLastUseTime {
	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(ILastUseTime.class)
	public static final Capability<ILastUseTime> LAST_USE_TIME_CAPABILITY = null;

	/**
	 * The default {@link EnumFacing} to use for this capability.
	 */
	public static final EnumFacing DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "LastUseTime");

	public static void register() {
		CapabilityManager.INSTANCE.register(ILastUseTime.class, new Capability.IStorage<ILastUseTime>() {
			@Override
			public NBTBase writeNBT(Capability<ILastUseTime> capability, ILastUseTime instance, EnumFacing side) {
				return new NBTTagLong(instance.get());
			}

			@Override
			public void readNBT(Capability<ILastUseTime> capability, ILastUseTime instance, EnumFacing side, NBTBase nbt) {
				instance.set(((NBTTagLong) nbt).getLong());
			}
		}, () -> new LastUseTime(true));
	}

	/**
	 * Get the {@link ILastUseTime} from the specified {@link ItemStack}'s capabilities, if any.
	 *
	 * @param itemStack The ItemStack
	 * @return The ILastUseTime, or null if there isn't one
	 */
	@Nullable
	public static ILastUseTime getLastUseTime(ItemStack itemStack) {
		return CapabilityUtils.getCapability(itemStack, LAST_USE_TIME_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Update the last use time of the player's held item.
	 *
	 * @param player    The player
	 * @param itemStack The held ItemStack
	 * @param hand      The hand holding the item
	 */
	public static void updateLastUseTime(EntityPlayer player, ItemStack itemStack, EnumHand hand) {
		final ILastUseTime lastUseTime = getLastUseTime(itemStack);
		if (lastUseTime == null) return;

		final World world = player.getEntityWorld();

		lastUseTime.set(world.getTotalWorldTime());
		if (!world.isRemote) {
			sendToPlayer(lastUseTime, (EntityPlayerMP) player, hand);
		}
	}

	/**
	 * Sync the {@link ILastUseTime} for the player's held item.
	 *
	 * @param lastUseTime The ILastUseTime
	 * @param player      The player
	 * @param hand        The hand holding the ILastUseTime item
	 */
	private static void sendToPlayer(ILastUseTime lastUseTime, EntityPlayerMP player, EnumHand hand) {
		TestMod3.network.sendTo(new MessageUpdateHeldLastUseTime(lastUseTime, hand), player);
	}

	/**
	 * Create a provider for the default {@link ILastUseTime} instance.
	 *
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider() {
		return new CapabilityProviderSerializable<>(LAST_USE_TIME_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the specified {@link ILastUseTime} instance.
	 *
	 * @param lastUseTime The ILastUseTime
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(ILastUseTime lastUseTime) {
		return new CapabilityProviderSerializable<>(LAST_USE_TIME_CAPABILITY, DEFAULT_FACING, lastUseTime);
	}

	/**
	 * Event handler for the {@link ILastUseTime} capability.
	 */
	@Mod.EventBusSubscriber
	public static class EventHandler {
		/**
		 * Update the {@link ILastUseTime} of the player's held item when they right click.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(PlayerInteractEvent.RightClickItem event) {
			final ItemStack itemStack = event.getItemStack();
			final ILastUseTime lastUseTime = getLastUseTime(itemStack);
			if (lastUseTime != null && lastUseTime.automaticUpdates()) {
				updateLastUseTime(event.getEntityPlayer(), itemStack, event.getHand());
			}
		}
	}

	/**
	 * {@link IItemPropertyGetter} to get the ticks since the last use of the item. Returns {@link Float#MAX_VALUE} if the required information isn't available.
	 */
	public static class TicksSinceLastUseGetter {
		/**
		 * The ID of this getter.
		 */
		private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "ticksSinceLastUse");

		/**
		 * The getter.
		 */
		private static final IItemPropertyGetter GETTER = IItemPropertyGetterFix.create((stack, worldIn, entityIn) -> {
			final ILastUseTime lastUseTime = getLastUseTime(stack);

			final World world = worldIn != null ? worldIn : entityIn != null ? entityIn.getEntityWorld() : null;

			return lastUseTime != null && world != null ? world.getTotalWorldTime() - lastUseTime.get() : Float.MAX_VALUE;
		});

		/**
		 * Add this getter to an {@link Item}.
		 *
		 * @param item The item
		 */
		public static void addToItem(Item item) {
			item.addPropertyOverride(ID, GETTER);
		}
	}
}
