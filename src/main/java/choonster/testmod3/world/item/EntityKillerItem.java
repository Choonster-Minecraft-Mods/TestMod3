package choonster.testmod3.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * An item that kills an entity when you left-click on it.
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

	@SuppressWarnings("resource")
	@Override
	public boolean onLeftClickEntity(final ItemStack stack, final Player player, final Entity entity) {
		if (!player.level().isClientSide) {
			final Entity entityToKill;

			if (entity instanceof final EnderDragonPart enderDragonPart) { // If it's a part of an Ender Dragon, kill the main Ender Dragon entity
				entityToKill = enderDragonPart.parentMob;
			} else {
				entityToKill = entity;
			}

			entityToKill.kill();
			player.sendSystemMessage(Component.translatable("commands.kill.success.single", entityToKill.getDisplayName()));
		}

		return true;
	}
}
