package choonster.testmod3.world.item;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.entity.PartEntity;

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
		if (
				player instanceof final ServerPlayer serverPlayer
						&& player.level() instanceof final ServerLevel serverLevel
		) {
			final Entity entityToKill;

			// If it's a part of a multipart entity, kill the main entity
			if (entity instanceof final PartEntity<?> partEntity) {
				entityToKill = partEntity.getParent();
			} else {
				entityToKill = entity;
			}

			entityToKill.kill(serverLevel);

			serverPlayer.sendSystemMessage(
					Component.translatable(
							"commands.kill.success.single",
							entityToKill.getDisplayName()
					)
			);
		}

		return true;
	}
}
