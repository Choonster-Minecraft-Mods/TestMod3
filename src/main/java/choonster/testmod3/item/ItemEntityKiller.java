package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.EntityDragonPart;
import net.minecraft.entity.player.EntityPlayer;
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
public class ItemEntityKiller extends ItemTestMod3 {
	public ItemEntityKiller() {
		super("entity_killer");
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (!player.world.isRemote) {
			final Entity entityToKill;
			if (entity instanceof EntityDragonPart) { // If it's a multipart entity, kill the main entity
				entityToKill = (Entity) ((EntityDragonPart) entity).entityDragonObj;
			} else {
				entityToKill = entity;
			}

			entityToKill.onKillCommand();
			player.sendMessage(new TextComponentTranslation("commands.kill.successful", entityToKill.getDisplayName()));
		}

		return true;
	}
}
