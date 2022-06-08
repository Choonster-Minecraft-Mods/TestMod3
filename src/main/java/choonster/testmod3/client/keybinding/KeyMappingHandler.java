package choonster.testmod3.client.keybinding;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.init.ModKeyMappings;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

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
		final LocalPlayer clientPlayer = MINECRAFT.player;

		for (final InteractionHand hand : InteractionHand.values()) {
			final ItemStack heldItem = clientPlayer.getItemInHand(hand);
			final int heldItemCount = heldItem.getCount();

			final BlockPos pos = clientPlayer.blockPosition().below();
			final BlockHitResult rayTraceResult = new BlockHitResult(new Vec3(0, 0, 0), Direction.UP, pos, false);

			final InteractionResult actionResult = MINECRAFT.gameMode.useItemOn(clientPlayer, hand, rayTraceResult);

			if (actionResult == InteractionResult.SUCCESS) {
				clientPlayer.swing(hand);

				if (!heldItem.isEmpty() && (heldItem.getCount() != heldItemCount || MINECRAFT.gameMode.hasInfiniteItems())) {
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
		final LocalPlayer clientPlayer = MINECRAFT.player;

		if (MINECRAFT.hitResult.getType() == HitResult.Type.ENTITY) {
			final EntityHitResult rayTraceResult = (EntityHitResult) MINECRAFT.hitResult;
			if (rayTraceResult.getEntity() instanceof LivingEntity) {
				final Collection<MobEffectInstance> activePotionEffects = ((LivingEntity) rayTraceResult.getEntity()).getActiveEffects();

				if (activePotionEffects.isEmpty()) {
					clientPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_POTIONS.getTranslationKey(), rayTraceResult.getEntity().getDisplayName()));
				} else {
					clientPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_PRINT_POTIONS_POTIONS.getTranslationKey(), rayTraceResult.getEntity().getDisplayName()));

					activePotionEffects.forEach(
							potionEffect -> clientPlayer.sendSystemMessage(Component.literal(potionEffect.toString()))
					);
				}
			} else {
				clientPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_PRINT_POTIONS_NOT_LIVING.getTranslationKey(), rayTraceResult.getEntity().getDisplayName()));
			}
		} else {
			clientPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_ENTITY.getTranslationKey()));
		}
	}
}
