package choonster.testmod3.capability.maxhealth;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.maxhealth.IMaxHealth;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.util.ModLogUtils;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Marker;

/**
 * Capability for {@link IMaxHealth}.
 *
 * @author Choonster
 */
public final class MaxHealthCapability {
	/**
	 * The {@link Capability} instance.
	 */
	public static final Capability<IMaxHealth> MAX_HEALTH_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of this capability.
	 */
	public static final Identifier ID = Identifier.fromNamespaceAndPath(TestMod3.MODID, "max_health");

	public static final Marker LOG_MARKER = ModLogUtils.getMarker("MaxHealth");

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
	 * Format a max health value.
	 *
	 * @param maxHealth The max health value
	 * @return The formatted text.
	 */
	public static String formatMaxHealth(final float maxHealth) {
		return ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(maxHealth);
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
		public static void attachCapabilities(final AttachCapabilitiesEvent.Entities event) {
			if (event.getObject() instanceof final LivingEntity entity) {
				final var maxHealth = MaxHealth.empty(entity);
				final var codec = MaxHealth.codec(entity);

				event.addCapability(ID, new SerializableCapabilityProvider<>(MAX_HEALTH_CAPABILITY, DEFAULT_FACING, maxHealth, codec));
			}
		}

		/**
		 * Copy the player's bonus max health when they respawn after dying or returning from the end.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerClone(final PlayerEvent.Clone event) {
			final var original = event.getOriginal();
			original.reviveCaps();

			final var oldMaxHealth = getMaxHealth(original).orElseThrow(CapabilityNotPresentException::new);
			final var newMaxHealth = getMaxHealth(event.getEntity()).orElseThrow(CapabilityNotPresentException::new);

			newMaxHealth.setBonusMaxHealth(oldMaxHealth.getBonusMaxHealth());

			original.invalidateCaps();
		}

		/**
		 * Synchronise a player's max health to watching clients when they change dimensions.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerChangeDimension(final PlayerEvent.PlayerChangedDimensionEvent event) {
			getMaxHealth(event.getEntity())
					.orElseThrow(CapabilityNotPresentException::new)
					.synchronise();
		}
	}
}
