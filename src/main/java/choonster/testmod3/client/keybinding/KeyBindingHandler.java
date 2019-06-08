package choonster.testmod3.client.keybinding;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.init.ModKeyBindings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.TickEvent;

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
		final EntityPlayerSP clientPlayer = MINECRAFT.player;

		for (final EnumHand hand : EnumHand.values()) {
			final ItemStack heldItem = clientPlayer.getHeldItem(hand);
			final int heldItemCount = heldItem.getCount();

			final BlockPos pos = new BlockPos(clientPlayer).down();

			final EnumActionResult actionResult = MINECRAFT.playerController.processRightClickBlock(clientPlayer, MINECRAFT.world, pos, EnumFacing.UP, new Vec3d(0, 0, 0), hand);

			if (actionResult == EnumActionResult.SUCCESS) {
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
		final RayTraceResult objectMouseOver = MINECRAFT.objectMouseOver;
		final EntityPlayerSP clientPlayer = MINECRAFT.player;

		if (objectMouseOver.type == RayTraceResult.Type.ENTITY && objectMouseOver.entity != null) {
			if (objectMouseOver.entity instanceof EntityLivingBase) {
				final Collection<PotionEffect> activePotionEffects = ((EntityLivingBase) objectMouseOver.entity).getActivePotionEffects();

				if (activePotionEffects.isEmpty()) {
					clientPlayer.sendMessage(new TextComponentTranslation("message.testmod3:print_potions.no_potions", objectMouseOver.entity.getDisplayName()));
				} else {
					clientPlayer.sendMessage(new TextComponentTranslation("message.testmod3:print_potions.potions", objectMouseOver.entity.getDisplayName()));

					activePotionEffects.forEach(
							potionEffect -> clientPlayer.sendMessage(new TextComponentString(potionEffect.toString()))
					);
				}
			} else {
				clientPlayer.sendMessage(new TextComponentTranslation("message.testmod3:print_potions.not_living", objectMouseOver.entity.getDisplayName()));
			}
		} else {
			clientPlayer.sendMessage(new TextComponentTranslation("message.testmod3:print_potions.no_entity"));
		}
	}
}
