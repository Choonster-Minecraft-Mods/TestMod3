package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.MultiPartEntityPart;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * An item that kills an entity when you left click on it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2760814-getting-entitys-string-id
 *
 * @author Choonster
 */
public class ItemEntityKiller extends Item {
	public ItemEntityKiller(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final EntityPlayer player, final Entity entity) {
		if (!player.world.isRemote) {
			final Entity entityToKill;
			if (entity instanceof MultiPartEntityPart) { // If it's a multipart entity, kill the main entity
				entityToKill = (Entity) ((MultiPartEntityPart) entity).parent;
			} else {
				entityToKill = entity;
			}

			entityToKill.onKillCommand();
			player.sendMessage(new TextComponentTranslation("commands.kill.success.single", entityToKill.getDisplayName()));
		}

		return true;
	}
}
