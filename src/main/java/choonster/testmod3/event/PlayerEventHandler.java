package choonster.testmod3.event;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.Constants;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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
		final EntityPlayer player = event.getPlayer();

		final NBTTagCompound entityData = player.getEntityData();
		final NBTTagCompound persistedData = entityData.getCompound(EntityPlayer.PERSISTED_NBT_TAG);
		entityData.put(EntityPlayer.PERSISTED_NBT_TAG, persistedData);

		final String key = Constants.RESOURCE_PREFIX + "ReceivedItems";
		final String message;

		if (persistedData.getBoolean(key)) {
			message = "message.testmod3:login.already_received";
		} else {
			persistedData.putBoolean(key, true);

			ItemHandlerHelper.giveItemToPlayer(player, new ItemStack(Items.APPLE));

			message = "message.testmod3:login.free_apple";
		}

		final ITextComponent chatComponent = new TextComponentTranslation(message);
		chatComponent.getStyle().setColor(TextFormatting.LIGHT_PURPLE);
		player.sendMessage(chatComponent);
	}

	/**
	 * When a player dies, tell them their coordinates.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void livingDeath(final LivingDeathEvent event) {
		if (event.getEntity() instanceof EntityPlayer && !event.getEntity().getEntityWorld().isRemote) {
			final EntityPlayer player = (EntityPlayer) event.getEntity();
			final BlockPos pos = player.getPosition();
			player.sendMessage(new TextComponentTranslation("message.testmod3:death.coordinates", pos.getX(), pos.getY(), pos.getZ(), player.dimension, player.getEntityWorld().dimension.getType().getRegistryName()));
		}
	}
}
