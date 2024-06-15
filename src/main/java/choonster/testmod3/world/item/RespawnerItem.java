package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An item that teleports the player to their spawn position when right-clicked.
 *
 * @author Choonster
 */
public class RespawnerItem extends Item {
	public RespawnerItem(final Item.Properties properties) {
		super(properties);
	}

	@SuppressWarnings("resource")
	@Override
	public InteractionResultHolder<ItemStack> use(final Level world, final Player player, final InteractionHand hand) {
		final var heldItem = player.getItemInHand(hand);

		if (world.isClientSide) {
			return InteractionResultHolder.success(heldItem);
		}

		final var serverPlayer = (ServerPlayer) player;
		final var respawnPosition = serverPlayer.getRespawnPosition();
		final var respawnLevel = serverPlayer.server.getLevel(serverPlayer.getRespawnDimension());

		if (respawnPosition == null || respawnLevel == null) {
			serverPlayer.sendSystemMessage(Component.translatable(TestMod3Lang.MESSAGE_RESPAWNER_NO_SPAWN_LOCATION.getTranslationKey()));
			return InteractionResultHolder.fail(heldItem);
		}

		final var respawnTransition = serverPlayer.findRespawnPositionAndUseSpawnBlock(
				/* wonGame */ false,
				entity -> entity.sendSystemMessage(
						Component.translatable(
								TestMod3Lang.MESSAGE_RESPAWNER_TELEPORTING.getTranslationKey(),
								entity.getX(),
								entity.getY(),
								entity.getZ(),
								entity.level().dimension()
						)
				)
		);

		serverPlayer.changeDimension(respawnTransition);

		return InteractionResultHolder.consume(heldItem);
	}
}
