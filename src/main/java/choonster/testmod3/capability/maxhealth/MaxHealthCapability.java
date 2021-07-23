package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.util.LogUtil;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability for {@link IMaxHealth}.
 *
 * @author Choonster
 */
public final class MaxHealthCapability {

	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(IMaxHealth.class)
	public static final Capability<IMaxHealth> MAX_HEALTH_CAPABILITY = Null();

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "max_health");

	public static final Marker LOG_MARKER = MarkerManager.getMarker("MaxHealth").addParents(LogUtil.MOD_MARKER);

	public static void register() {
		CapabilityManager.INSTANCE.register(IMaxHealth.class);
	}

	/**
	 * Get the {@link IMaxHealth} from the specified entity.
	 *
	 * @param entity The entity
	 * @return A lazy optional containing the IMaxHealth, if any
	 */
	public static LazyOptional<IMaxHealth> getMaxHealth(final LivingEntity entity) {
		return entity.getCapability(MAX_HEALTH_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the specified {@link IMaxHealth} instance.
	 *
	 * @param maxHealth The IMaxHealth
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(final IMaxHealth maxHealth) {
		return new SerializableCapabilityProvider<>(MAX_HEALTH_CAPABILITY, DEFAULT_FACING, maxHealth);
	}

	/**
	 * Format a max health value.
	 *
	 * @param maxHealth The max health value
	 * @return The formatted text.
	 */
	public static String formatMaxHealth(final float maxHealth) {
		return ItemStack.ATTRIBUTE_MODIFIER_FORMAT.format(maxHealth);
	}

	/**
	 * Event handler for the {@link IMaxHealth} capability.
	 */
	@SuppressWarnings("unused")
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	private static class EventHandler {

		/**
		 * Attach the {@link IMaxHealth} capability to all living entities.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void attachCapabilities(final AttachCapabilitiesEvent<Entity> event) {
			if (event.getObject() instanceof LivingEntity) {
				final MaxHealth maxHealth = new MaxHealth((LivingEntity) event.getObject());
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
			getMaxHealth(event.getOriginal()).ifPresent(oldMaxHealth -> {
				getMaxHealth(event.getPlayer()).ifPresent(newMaxHealth -> {
					newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());
				});
			});
		}

		/**
		 * Synchronise a player's max health to watching clients when they change dimensions.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerChangeDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
			getMaxHealth(event.getPlayer())
					.ifPresent(IMaxHealth::synchronise);
		}
	}
}
