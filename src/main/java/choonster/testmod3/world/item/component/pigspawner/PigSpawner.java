package choonster.testmod3.world.item.component.pigspawner;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModDataComponents;
import choonster.testmod3.util.ModLogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSource;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Marker;

/**
 * Helper functions for {@link IPigSpawner} components.
 *
 * @author Choonster
 */
public final class PigSpawner {
	public static final Marker LOG_MARKER = ModLogUtils.getMarker("PIG_SPAWNER");

	/**
	 * Event handler for the {@link IPigSpawner} capability.
	 */
	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	private static class EventHandler {
		@Nullable
		private static IPigSpawner getPigSpawner(final ItemStack stack) {
			return stack.get(ModDataComponents.PIG_SPAWNER.get());
		}

		private static void setPigSpawner(final ItemStack stack, final IPigSpawner pigSpawner) {
			stack.set(ModDataComponents.PIG_SPAWNER.get(), pigSpawner);
		}

		/**
		 * Attach the {@link IPigSpawner} capability to vanilla items.
		 *
		 * @param event The event
		 */
		// TODO: Attach component to CLAY_BALL
//		@SubscribeEvent
//		public static void attachCapabilities(final AttachCapabilitiesEvent<ItemStack> event) {
//			if (event.getObject().getItem() == Items.CLAY_BALL) {
//				final var maxNumPigs = 20;
//				final var pigSpawner = FinitePigSpawner.empty(maxNumPigs);
//				final var codec = FinitePigSpawner.codec(maxNumPigs);
//
//				event.addCapability(ID, createProvider(pigSpawner, codec));
//			}
//		}

		/**
		 * Try to spawn a pig at the specified position, if the item has the {@link IPigSpawner} component.
		 * <p>
		 * If there's an {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param stack           The item that optionally has the pig spawner component
		 * @param level           The level
		 * @param x               The x position to spawn the pig at
		 * @param y               The y position to spawn the pig at
		 * @param z               The z position to spawn the pig at
		 * @param interactable    The IPigSpawnerInteractable, if any
		 * @param interactablePos The position of the IPigSpawnerInteractable
		 * @param commandSource   The command source, if any
		 */
		private static void trySpawnPig(
				final ItemStack stack,
				final Level level,
				final double x,
				final double y,
				final double z,
				@Nullable final IPigSpawnerInteractable interactable,
				final BlockPos interactablePos,
				@Nullable final CommandSource commandSource
		) {
			if (level.isClientSide) {
				return;
			}

			final var pigSpawner = getPigSpawner(stack);

			if (pigSpawner == null) {
				return;
			}

			if (interactable != null) {
				final var newPigSpawner = interactable.interact(pigSpawner, level, interactablePos, commandSource);
				if (newPigSpawner != null) {
					setPigSpawner(stack, newPigSpawner);
					return;
				}
			}

			if (pigSpawner.canSpawnPig(level, x, y, z)) {
				final var newPigSpawner = pigSpawner.spawnPig(level, x, y, z);
				if (newPigSpawner != null) {
					setPigSpawner(stack, newPigSpawner);
				}
			}
		}

		/**
		 * Spawn a pig when a player right-clicks a block with an item that has the {@link IPigSpawner} component.
		 * <p>
		 * If the block implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void playerInteract(final PlayerInteractEvent.RightClickBlock event) {
			final var facing = event.getFace();
			assert facing != null;

			final var spawnPos = event.getPos().relative(facing);
			final double x = spawnPos.getX() + 0.5, y = spawnPos.getY() + 0.5, z = spawnPos.getZ() + 0.5;

			final var level = event.getLevel();
			final var block = level.getBlockState(event.getPos()).getBlock();
			final var interactable = block instanceof IPigSpawnerInteractable ? (IPigSpawnerInteractable) block : null;

			final var player = event.getEntity();

			trySpawnPig(event.getItemStack(), level, x, y, z, interactable, event.getPos(), player);
		}

		/**
		 * Spawn a pig when a player right-clicks an entity with an item that has the {@link IPigSpawner} component.
		 * <p>
		 * If the entity implements {@link IPigSpawnerInteractable}, call {@link IPigSpawnerInteractable#interact} on it.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void entityInteract(final PlayerInteractEvent.EntityInteract event) {
			final var level = event.getLevel();

			final var target = event.getTarget();
			final double x = target.getX(), y = target.getY(), z = target.getZ();
			final var interactable = target instanceof IPigSpawnerInteractable ? (IPigSpawnerInteractable) target : null;

			final var hand = event.getHand();
			final var heldItem = event.getEntity().getItemInHand(hand);

			trySpawnPig(heldItem, level, x, y, z, interactable, target.blockPosition(), event.getEntity());
		}

		/**
		 * Add the {@link IPigSpawner}'s tooltip lines to the tooltip if the item has the {@link IPigSpawner} component.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void itemTooltip(final ItemTooltipEvent event) {
			final var pigSpawner = getPigSpawner(event.getItemStack());

			if (pigSpawner == null) {
				return;
			}

			final var style = Style.EMPTY.withColor(ChatFormatting.LIGHT_PURPLE);

			final var tooltipLines = pigSpawner
					.getTooltipLines()
					.stream()
					.map(textComponent -> textComponent.setStyle(style))
					.toList();

			event.getToolTip().add(Component.literal(""));
			event.getToolTip().addAll(tooltipLines);
		}
	}
}
