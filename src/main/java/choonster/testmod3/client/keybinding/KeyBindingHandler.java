package choonster.testmod3.client.keybinding;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.init.ModKeyBindings;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

/**
 * Handles the effects of this mod's {@link KeyBinding}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(value = Dist.CLIENT, modid = TestMod3.MODID)
public class KeyBindingHandler {
	private static final Minecraft MINECRAFT = Minecraft.getInstance();

	/**
	 * Handle the effects of this mod's {@link KeyBinding}s.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void clientTick(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;

		if (ModKeyBindings.PLACE_HELD_BLOCK.isKeyDown()) {
			placeHeldBlock();
		}

		if (ModKeyBindings.PRINT_POTIONS.isPressed()) {
			printPotions();
		}
	}

	/**
	 * Attempt to place a block from the player's hand below them.
	 * <p>
	 * Adapted from {@link Minecraft#rightClickMouse}.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2786461-how-to-get-minecraftserver-instance
	 */
	private static void placeHeldBlock() {
		final ClientPlayerEntity clientPlayer = MINECRAFT.player;

		for (final Hand hand : Hand.values()) {
			final ItemStack heldItem = clientPlayer.getHeldItem(hand);
			final int heldItemCount = heldItem.getCount();

			final BlockPos pos = clientPlayer.getPosition().down();
			final BlockRayTraceResult rayTraceResult = new BlockRayTraceResult(new Vector3d(0, 0, 0), Direction.UP, pos, false);

			final ActionResultType actionResult = MINECRAFT.playerController.func_217292_a(clientPlayer, MINECRAFT.world, hand, rayTraceResult);

			if (actionResult == ActionResultType.SUCCESS) {
				clientPlayer.swingArm(hand);

				if (!heldItem.isEmpty() && (heldItem.getCount() != heldItemCount || MINECRAFT.playerController.isInCreativeMode())) {
					MINECRAFT.gameRenderer.itemRenderer.resetEquippedProgress(hand);
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
		final ClientPlayerEntity clientPlayer = MINECRAFT.player;

		if (MINECRAFT.objectMouseOver.getType() == RayTraceResult.Type.ENTITY) {
			final EntityRayTraceResult rayTraceResult = (EntityRayTraceResult) MINECRAFT.objectMouseOver;
			if (rayTraceResult.getEntity() instanceof LivingEntity) {
				final Collection<EffectInstance> activePotionEffects = ((LivingEntity) rayTraceResult.getEntity()).getActivePotionEffects();

				if (activePotionEffects.isEmpty()) {
					clientPlayer.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_POTIONS.getTranslationKey(), rayTraceResult.getEntity().getDisplayName()), Util.DUMMY_UUID);
				} else {
					clientPlayer.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_PRINT_POTIONS_POTIONS.getTranslationKey(), rayTraceResult.getEntity().getDisplayName()), Util.DUMMY_UUID);

					activePotionEffects.forEach(
							potionEffect -> clientPlayer.sendMessage(new StringTextComponent(potionEffect.toString()), Util.DUMMY_UUID)
					);
				}
			} else {
				clientPlayer.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_PRINT_POTIONS_NOT_LIVING.getTranslationKey(), rayTraceResult.getEntity().getDisplayName()), Util.DUMMY_UUID);
			}
		} else {
			clientPlayer.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_PRINT_POTIONS_NO_ENTITY.getTranslationKey()), Util.DUMMY_UUID);
		}
	}
}
