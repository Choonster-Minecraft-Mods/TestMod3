package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.Constants;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
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

		final CompoundNBT entityData = player.getPersistantData();
		final CompoundNBT persistedData = entityData.getCompound(PlayerEntity.PERSISTED_NBT_TAG);
		entityData.put(PlayerEntity.PERSISTED_NBT_TAG, persistedData);

		final String key = Constants.RESOURCE_PREFIX + "ReceivedItems";
		final String message;

		if (persistedData.getBoolean(key)) {
			message = "message.testmod3.login.already_received";
		} else {
			persistedData.putBoolean(key, true);

			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.APPLE));

			message = "message.testmod3.login.free_apple";
		}

		final ITextComponent textComponent = new TranslationTextComponent(message);
		textComponent.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
		player.sendMessage(textComponent);
	}

	/**
	 * When a player dies, tell them their coordinates.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void livingDeath(final LivingDeathEvent event) {
		if (event.getEntity() instanceof PlayerEntity && !event.getEntity().getEntityWorld().isRemote) {
			final PlayerEntity player = (PlayerEntity) event.getEntity();
			final BlockPos pos = player.getPosition();
			player.sendMessage(new TranslationTextComponent("message.testmod3.death.coordinates", pos.getX(), pos.getY(), pos.getZ(), player.dimension, player.getEntityWorld().dimension.getType().getRegistryName()));
		}
	}
}
