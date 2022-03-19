package choonster.testmod3.capability.pigspawner;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.pigspawner.IPigSpawner;
import choonster.testmod3.api.capability.pigspawner.IPigSpawnerInteractable;
import choonster.testmod3.capability.CapabilityContainerListenerManager;
import choonster.testmod3.capability.SerializableCapabilityProvider;
import choonster.testmod3.util.ModLogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Marker;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Capability for {@link IPigSpawner}.
 *
 * @author Choonster
 */
public final class PigSpawnerCapability {
	/**
	 * The {@link Capability} instance.
	 */
	public static final Capability<IPigSpawner> PIG_SPAWNER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	/**
	 * The default {@link Direction} to use for this capability.
	 */
	public static final Direction DEFAULT_FACING = null;

	/**
	 * The ID of the capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "pig_spawner");

	public static final Marker LOG_MARKER = ModLogUtils.getMarker("PIG_SPAWNER");

	/**
	 * Register the capability.
	 */
	public static void register(final RegisterCapabilitiesEvent event) {
		event.register(IPigSpawner.class);

		CapabilityContainerListenerManager.registerListenerFactory(FinitePigSpawnerContainerListener::new);
	}

	/**
	 * Get the {@link IPigSpawner} from the specified {@link ItemStack}'s capabilities, if any.
	 *
	 * @param itemStack The ItemStack
	 * @return A lazy optional containing the IPigSpawner, if any
	 */
	public static LazyOptional<IPigSpawner> getPigSpawner(final ItemStack itemStack) {
		return itemStack.getCapability(PIG_SPAWNER_CAPABILITY, DEFAULT_FACING);
	}

	/**
	 * Create a provider for the specified {@link IPigSpawner} instance.
	 *
	 * @param pigSpawner The IPigSpawner
	 * @return The provider
	 */
	public static ICapabilityProvider createProvider(final IPigSpawner pigSpawner) {
		return new SerializableCapabilityProvider<>(PIG_SPAWNER_CAPABILITY, DEFAULT_FACING, pigSpawner);
	}

	/**
	 * Event handler for the {@link IPigSpawner} capability.
	 */
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	private static class EventHandler {

		/**
		 * Attach the {@link IPigSpawner} capability to vanilla items.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> event) {
			if (event.getObject().getItem() == Items.CLAY_BALL) {
				event.addCapability(ID, createProvider(new FinitePigSpawner(20)));
			}
		}

		/**
		 * Spawn a pig at the specified position.
		 * <p>
		 * If there's an {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param pigSpawner      The pig spawner
		 * @param world           The level
		 * @param x               The x position to spawn the pig at
		 * @param y               The y position to spawn the pig at
		 * @param z               The z position to spawn the pig at
		 * @param interactable    The IPigSpawnerInteractable, if any
		 * @param interactablePos The position of the IPigSpawnerInteractable
		 * @param commandSource   The command source, if any
		 */
		private static void trySpawnPig(final IPigSpawner pigSpawner, final Level world, final double x, final double y, final double z, @Nullable final IPigSpawnerInteractable interactable, final BlockPos interactablePos, @Nullable final CommandSource commandSource) {
			if (world.isClientSide) {
				return;
			}

			boolean shouldSpawnPig = true;

			if (interactable != null) {
				shouldSpawnPig = !interactable.interact(pigSpawner, world, interactablePos, commandSource);
			}

			if (shouldSpawnPig && pigSpawner.canSpawnPig(world, x, y, z)) {
				pigSpawner.spawnPig(world, x, y, z);
			}
		}

		/**
		 * Spawn a pig when a player right-clicks a block with an item that has the {@link IPigSpawner} capability.
		 * <p>
		 * If the block implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(final PlayerInteractEvent.RightClickBlock event) {
			final Direction facing = event.getFace();
			assert facing != null;

			final BlockPos spawnPos = event.getPos().relative(facing);
			final double x = spawnPos.getX() + 0.5, y = spawnPos.getY() + 0.5, z = spawnPos.getZ() + 0.5;

			final Level world = event.getWorld();
			final Block block = world.getBlockState(event.getPos()).getBlock();
			final IPigSpawnerInteractable interactable = block instanceof IPigSpawnerInteractable ? (IPigSpawnerInteractable) block : null;

			final Player player = event.getPlayer();

			getPigSpawner(event.getItemStack())
					.ifPresent(pigSpawner -> trySpawnPig(pigSpawner, world, x, y, z, interactable, event.getPos(), player));
		}

		/**
		 * Spawn a pig when a player right-clicks an entity with an item that has the {@link IPigSpawner} capability.
		 * <p>
		 * If the entity implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void entityInteract(final PlayerInteractEvent.EntityInteract event) {
			final Level world = event.getPlayer().getCommandSenderWorld();

			final Entity target = event.getTarget();
			final double x = target.getX(), y = target.getY(), z = target.getZ();
			final IPigSpawnerInteractable interactable = target instanceof IPigSpawnerInteractable ? (IPigSpawnerInteractable) target : null;

			final InteractionHand hand = event.getHand();

			getPigSpawner(event.getPlayer().getItemInHand(hand))
					.ifPresent(pigSpawner -> trySpawnPig(pigSpawner, world, x, y, z, interactable, target.blockPosition(), event.getPlayer()));
		}

	}

	@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
	private static class ClientEventHandler {
		/**
		 * Add the {@link IPigSpawner}'s tooltip lines to the tooltip if the item has the {@link IPigSpawner} capability
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void itemTooltip(final ItemTooltipEvent event) {
			getPigSpawner(event.getItemStack()).ifPresent(pigSpawner -> {
				final Style style = Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);

				final List<Component> tooltipLines = pigSpawner.getTooltipLines().stream()
						.map(textComponent -> textComponent.setStyle(style))
						.collect(Collectors.toList());

				event.getToolTip().add(new TextComponent(""));
				event.getToolTip().addAll(tooltipLines);

			});
		}
	}
}
