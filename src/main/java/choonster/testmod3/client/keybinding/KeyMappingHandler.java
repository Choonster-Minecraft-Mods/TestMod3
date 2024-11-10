package choonster.testmod3.client.keybinding;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.init.ModKeyMappings;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Handles the effects of this mod's {@link KeyMapping}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class KeyMappingHandler {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	/**
	 * Handle the effects of this mod's {@link KeyMapping}s.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void clientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) {
			return;
		}

		if (ModKeyMappings.PLACE_HELD_BLOCK.isDown()) {
			placeHeldBlock();
		}

		if (ModKeyMappings.PRINT_POTIONS.consumeClick()) {
			printPotions();
		}
	}

	/**
	 * Attempt to place a block from the player's hand below them.
	 * <p>
	 * Adapted from {@link Minecraft}#startUseItem.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2786461-how-to-get-minecraftserver-instance
	 */
	private static void placeHeldBlock() {
		final var clientPlayer = MINECRAFT.player;
		final var gameMode = MINECRAFT.gameMode;

		if (clientPlayer == null || gameMode == null) {
			return;
		}

		for (final var hand : InteractionHand.values()) {
			final var heldItem = clientPlayer.getItemInHand(hand);
			final var heldItemCount = heldItem.getCount();

			final var pos = clientPlayer.blockPosition().below();
			final var rayTraceResult = new BlockHitResult(new Vec3(0, 0, 0), Direction.UP, pos, false);

			final var actionResult = gameMode.useItemOn(clientPlayer, hand, rayTraceResult);

			if (actionResult == InteractionResult.SUCCESS) {
				clientPlayer.swing(hand);

				if (!heldItem.isEmpty() && (heldItem.getCount() != heldItemCount || gameMode.hasInfiniteItems())) {
					MINECRAFT.gameRenderer.itemInHandRenderer.itemUsed(hand);
				}

				return;
			}
		}
	}

	/**
	 * Print the active potion effects on the entity that the player is looking at to the chat.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php?topic=45025.0
	 */
	private static void printPotions() {
		final var clientPlayer = MINECRAFT.player;

		if (clientPlayer == null) {
			return;
		}

		if (MINECRAFT.hitResult instanceof final EntityHitResult hitResult) {
			if (hitResult.getEntity() instanceof final LivingEntity entity) {
				final var activePotionEffects = entity.getActiveEffects();

				if (activePotionEffects.isEmpty()) {
					clientPlayer.displayClientMessage(
							Component.translatable(
									TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_POTIONS.getTranslationKey(),
									hitResult.getEntity().getDisplayName()
							),
							false
					);
				} else {
					clientPlayer.displayClientMessage(
							Component.translatable(
									TestMod3Lang.MESSAGE_PRINT_POTIONS_POTIONS.getTranslationKey(),
									hitResult.getEntity().getDisplayName()
							),
							false
					);

					activePotionEffects.forEach(
							potionEffect -> clientPlayer.displayClientMessage(
									Component.literal(potionEffect.toString()),
									false
							)
					);
				}
			} else {
				clientPlayer.displayClientMessage(
						Component.translatable(
								TestMod3Lang.MESSAGE_PRINT_POTIONS_NOT_LIVING.getTranslationKey(),
								hitResult.getEntity().getDisplayName()
						),
						false
				);
			}
		} else {
			clientPlayer.displayClientMessage(
					Component.translatable(
							TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_ENTITY.getTranslationKey()
					),
					false
			);
		}
	}
}
