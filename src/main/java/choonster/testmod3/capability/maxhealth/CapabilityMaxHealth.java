package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.Logger;
import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.CapabilityProviderSerializable;
import choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerChangedDimensionEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import javax.annotation.Nullable;

/**
 * Capability for {@link IMaxHealth}.
 *
 * @author Choonster
 */
public final class CapabilityMaxHealth {

	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(IMaxHealth.class)
	public static final Capability<IMaxHealth> MAX_HEALTH_CAPABILITY = null;

	/**
	 * The default {@link EnumFacing} to use for this capability.
	 */
	public static final EnumFacing DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "MaxHealth");

	public static final Marker LOG_MARKER = MarkerManager.getMarker("MaxHealth", Logger.MOD_MARKER);

	public static void register() {
		CapabilityManager.INSTANCE.register(IMaxHealth.class, new Capability.IStorage<IMaxHealth>() {
			@Override
			public NBTBase writeNBT(final Capability<IMaxHealth> capability, final IMaxHealth instance, final EnumFacing side) {
				return new NBTTagFloat(instance.getBonusMaxHealth());
			}

			@Override
			public void readNBT(final Capability<IMaxHealth> capability, final IMaxHealth instance, final EnumFacing side, final NBTBase nbt) {
				instance.setBonusMaxHealth(((NBTTagFloat) nbt).getFloat());
			}
		}, () -> new MaxHealth(null));
	}

	/**
	 * Get the {@link IMaxHealth} from the specified entity.
	 *
	 * @param entity The entity
	 * @return The IMaxHealth
	 */
	@Nullable
	public static IMaxHealth getMaxHealth(final EntityLivingBase entity) {
		return CapabilityUtils.getCapability(entity, MAX_HEALTH_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the specified {@link IMaxHealth} instance.
	 *
	 * @param maxHealth The IMaxHealth
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(final IMaxHealth maxHealth) {
		return new CapabilityProviderSerializable<>(MAX_HEALTH_CAPABILITY, DEFAULT_FACING, maxHealth);
	}

	/**
	 * Format a max health value.
	 *
	 * @param maxHealth The max health value
	 * @return The formatted text.
	 */
	public static String formatMaxHealth(final float maxHealth) {
		return ItemStack.DECIMALFORMAT.format(maxHealth);
	}

	/**
	 * Event handler for the {@link IMaxHealth} capability.
	 */
	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber
	private static class EventHandler {

		/**
		 * Attach the {@link IMaxHealth} capability to all living entities.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof EntityLivingBase) {
				final MaxHealth maxHealth = new MaxHealth((EntityLivingBase) event.getObject());
				event.addCapability(ID, createProvider(maxHealth));
			}
		}

		/**
		 * Copy the player's bonus max health when they respawn after dying or returning from the end.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerClone(final PlayerEvent.Clone event) {
			final IMaxHealth oldMaxHealth = getMaxHealth(event.getOriginal());
			final IMaxHealth newMaxHealth = getMaxHealth(event.getEntityPlayer());

			if (newMaxHealth != null && oldMaxHealth != null) {
				newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
			}
		}

		/**
		 * Synchronise a player's max health to watching clients when they change dimensions.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerChangeDimension(final PlayerChangedDimensionEvent event) {
			final IMaxHealth maxHealth = getMaxHealth(event.player);

			if (maxHealth != null) {
				maxHealth.synchronise();
			}
		}
	}
}
