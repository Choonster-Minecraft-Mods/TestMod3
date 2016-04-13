package com.choonster.testmod3.capability.maxhealth;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import com.choonster.testmod3.capability.SimpleCapabilityProvider;
import com.choonster.testmod3.util.CapabilityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

/**
 * Capability for {@link IMaxHealth}.
 *
 * @author Choonster
 */
public class CapabilityMaxHealth {

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
			public NBTBase writeNBT(Capability<IMaxHealth> capability, IMaxHealth instance, EnumFacing side) {
				return new NBTTagFloat(instance.getBonusMaxHealth());
			}

			@Override
			public void readNBT(Capability<IMaxHealth> capability, IMaxHealth instance, EnumFacing side, NBTBase nbt) {
				instance.setBonusMaxHealth(((NBTTagFloat) nbt).getFloat());
			}
		}, () -> new MaxHealth(null));

		MinecraftForge.EVENT_BUS.register(new EventHandler());
	}

	/**
	 * Get the {@link IMaxHealth} from the specified entity.
	 *
	 * @param entity The entity
	 * @return The IMaxHealth
	 */
	public static IMaxHealth getMaxHealth(EntityLivingBase entity) {
		return CapabilityUtils.getCapability(entity, MAX_HEALTH_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the specified {@link IMaxHealth} instance.
	 *
	 * @param maxHealth The IMaxHealth
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(IMaxHealth maxHealth) {
		return new SimpleCapabilityProvider<>(MAX_HEALTH_CAPABILITY, DEFAULT_FACING, maxHealth);
	}

	/**
	 * Event handler for the {@link IMaxHealth} capability.
	 */
	public static class EventHandler {
		/**
		 * Attach the {@link IMaxHealth} capability to all living entities.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public void attachCapabilities(AttachCapabilitiesEvent.Entity event) {
			if (event.getEntity() instanceof EntityLivingBase) {
				final MaxHealth maxHealth = new MaxHealth((EntityLivingBase) event.getEntity());
				event.addCapability(ID, createProvider(maxHealth));
			}
		}

		/**
		 * Copy the player's bonus max health when they respawn after dying or returning from the end.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public void playerClone(PlayerEvent.Clone event) {
			final IMaxHealth oldMaxHealth = getMaxHealth(event.getOriginal());
			final IMaxHealth newMaxHealth = getMaxHealth(event.getEntityPlayer());

			newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
		}
	}


}
