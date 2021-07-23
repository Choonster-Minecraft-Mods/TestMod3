package choonster.testmod3.world.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;

/**
 * An item that kills an entity when you left click on it.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2760814-getting-entitys-string-id
 *
 * @author Choonster
 */
public class EntityKillerItem extends Item {
	public EntityKillerItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final Player player, final Entity entity) {
		if (!player.level.isClientSide) {
			final Entity entityToKill;
			if (entity instanceof EnderDragonPart) { // If it's a part of an Ender Dragon, kill the main Ender Dragon entity
				entityToKill = ((EnderDragonPart) entity).parentMob;
			} else {
				entityToKill = entity;
			}

			entityToKill.kill();
			player.sendMessage(new TranslatableComponent("commands.kill.success.single", entityToKill.getDisplayName()), Util.NIL_UUID);
		}

		return true;
	}
}
