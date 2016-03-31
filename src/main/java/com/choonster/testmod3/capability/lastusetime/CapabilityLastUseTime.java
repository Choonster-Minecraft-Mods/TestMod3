package com.choonster.testmod3.capability.lastusetime;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.api.capability.lastusetime.ILastUseTime;
import com.choonster.testmod3.network.MessageUpdateHeldLastUseTime;
import com.choonster.testmod3.util.CapabilityUtils;
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
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

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
		}, LastUseTime::new);

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	/**
	 * Get the {@link ILastUseTime} from the specified {@link ItemStack}'s capabilities, if any.
	 *
	 * @param itemStack The ItemStack
	 * @return The ILastUseTime, or null if there isn't one
	 */
	public static ILastUseTime getLastUseTime(ItemStack itemStack) {
		return CapabilityUtils.getCapability(itemStack, LAST_USE_TIME_CAPABILITY, DEFAULT_FACING);
	}

	// Temporary method to update the player's held ILastUseTime item
	// TODO: Move to EventHandler#playerInteract when PlayerInteractEvent is properly updated
	public static void updateLastUseTime(EntityPlayer player, EnumHand hand) {
		final World world = player.getEntityWorld();
		final ILastUseTime lastUseTime = getLastUseTime(player.getHeldItem(hand));

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
	 * Event handler for the {@link ILastUseTime} capability.
	 */
	public static class EventHandler {
		@SubscribeEvent
		public void playerInteract(PlayerInteractEvent event) {
			if (event.getAction() != PlayerInteractEvent.Action.RIGHT_CLICK_AIR) return;

			final EntityPlayer player = event.getEntityPlayer();

			// TODO: Get the hand from PlayerInteractEvent when it's added
			final EnumHand hand = EnumHand.MAIN_HAND;

			updateLastUseTime(player, hand);
		}
	}

	/**
	 * Provider for the {@link ILastUseTime} capability.
	 */
	public static class Provider implements ICapabilitySerializable<NBTTagLong> {
		private ILastUseTime lastUseTime = new LastUseTime();

		/**
		 * Determines if this object has support for the capability in question on the specific side.
		 * The return value of this MIGHT change during runtime if this object gains or looses support
		 * for a capability.
		 * <p>
		 * Example:
		 * A Pipe getting a cover placed on one side causing it loose the Inventory attachment function for that side.
		 * <p>
		 * This is a light weight version of getCapability, intended for metadata uses.
		 *
		 * @param capability The capability to check
		 * @param facing     The Side to check from:
		 *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
		 * @return True if this object supports the capability.
		 */
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			return capability == LAST_USE_TIME_CAPABILITY;
		}

		/**
		 * Retrieves the handler for the capability requested on the specific side.
		 * The return value CAN be null if the object does not support the capability.
		 * The return value CAN be the same for multiple faces.
		 *
		 * @param capability The capability to check
		 * @param facing     The Side to check from:
		 *                   CAN BE NULL. Null is defined to represent 'internal' or 'self'
		 * @return True if this object supports the capability.
		 */
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if (capability == LAST_USE_TIME_CAPABILITY) {
				return LAST_USE_TIME_CAPABILITY.cast(lastUseTime);
			}

			return null;
		}

		@Override
		public NBTTagLong serializeNBT() {
			return (NBTTagLong) LAST_USE_TIME_CAPABILITY.getStorage().writeNBT(LAST_USE_TIME_CAPABILITY, lastUseTime, DEFAULT_FACING);
		}

		@Override
		public void deserializeNBT(NBTTagLong nbt) {
			LAST_USE_TIME_CAPABILITY.getStorage().readNBT(LAST_USE_TIME_CAPABILITY, lastUseTime, DEFAULT_FACING, nbt);
		}
	}

	/**
	 * {@link IItemPropertyGetter} to get the ticks since the last use of the item.
	 */
	public static class TicksSinceLastUseGetter {
		/**
		 * The ID of this getter.
		 */
		private static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "ticksSinceLastUse");

		/**
		 * The getter.
		 */
		private static final IItemPropertyGetter GETTER = (stack, worldIn, entityIn) -> {
			final ILastUseTime lastUseTime = getLastUseTime(stack);

			return lastUseTime != null ? worldIn.getTotalWorldTime() - lastUseTime.get() : 0;
		};

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
