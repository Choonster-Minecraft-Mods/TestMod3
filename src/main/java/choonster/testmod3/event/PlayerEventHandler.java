package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.ItemHandlerHelper;

/**
 * Handler for player-related events.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID)
public class PlayerEventHandler {

	/**
	 * Give the player an Apple when they first log in.
	 * <p>
	 * Test for this thread:
	 * http://www.minecraftforge.net/forum/index.php/topic,36355.0.html
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void playerLoggedIn(final PlayerEvent.PlayerLoggedInEvent event) {
		final PlayerEntity player = event.getPlayer();

		final CompoundNBT entityData = player.getPersistentData();
		final CompoundNBT persistedData = entityData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		entityData.put(PlayerEntity.PERSISTED_NBT_TAG, persistedData);

		final String key = Constants.RESOURCE_PREFIX + "ReceivedItems";
		final TestMod3Lang message;

		if (persistedData.getBoolean(key)) {
			message = TestMod3Lang.MESSAGE_LOGIN_ALREADY_RECEIVED;
		} else {
			persistedData.putBoolean(key, true);

			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.APPLE));

			message = TestMod3Lang.MESSAGE_LOGIN_FREE_APPLE;
		}

		final ITextComponent textComponent = new TranslationTextComponent(message.getTranslationKey());
		textComponent.getStyle().withColor(TextFormatting.LIGHT_PURPLE);
		player.sendMessage(textComponent, Util.NIL_UUID);
	}

	/**
	 * When a player dies, tell them their coordinates.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void livingDeath(final LivingDeathEvent event) {
		if (event.getEntity() instanceof PlayerEntity && !event.getEntity().getCommandSenderWorld().isClientSide) {
			final PlayerEntity player = (PlayerEntity) event.getEntity();
			final BlockPos pos = player.blockPosition();
			player.sendMessage(new TranslationTextComponent(TestMod3Lang.MESSAGE_DEATH_COORDINATES.getTranslationKey(), pos.getX(), pos.getY(), pos.getZ(), player.level.dimension()), Util.NIL_UUID);
		}
	}
}
