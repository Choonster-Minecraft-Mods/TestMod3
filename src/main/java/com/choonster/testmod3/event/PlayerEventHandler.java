package com.choonster.testmod3.event;

import com.choonster.testmod3.util.Constants;
import com.choonster.testmod3.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Handler for player-related events.
 *
 * @author Choonster
 */
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
	public void playerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
		final EntityPlayer player = event.player;

		final NBTTagCompound entityData = player.getEntityData();
		final NBTTagCompound persistedData = entityData.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
		entityData.setTag(EntityPlayer.PERSISTED_NBT_TAG, persistedData);

		final String key = Constants.RESOURCE_PREFIX + "ReceivedItems";
		String message = "message.testmod3:login.alreadyReceived";

		if (!persistedData.getBoolean(key)) {
			persistedData.setBoolean(key, true);

			InventoryUtils.addOrDropItem(player, new ItemStack(Items.apple));

			message = "message.testmod3:login.freeApple";
		}

		ChatComponentTranslation chatComponent = new ChatComponentTranslation(message);
		chatComponent.getChatStyle().setColor(EnumChatFormatting.LIGHT_PURPLE);
		player.addChatComponentMessage(chatComponent);
	}
}
