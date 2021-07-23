package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.dragon.EnderDragonPartEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

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
	public boolean onLeftClickEntity(final ItemStack stack, final PlayerEntity player, final Entity entity) {
		if (!player.level.isClientSide) {
			final Entity entityToKill;
			if (entity instanceof EnderDragonPartEntity) { // If it's a part of an Ender Dragon, kill the main Ender Dragon entity
				entityToKill = ((EnderDragonPartEntity) entity).parentMob;
			} else {
				entityToKill = entity;
			}

			entityToKill.kill();
			player.sendMessage(new TranslationTextComponent("commands.kill.success.single", entityToKill.getDisplayName()), Util.NIL_UUID);
		}

		return true;
	}
}
