package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.capability.lock.LockScreenData;
import choonster.testmod3.init.ModClientScreenTypes;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

/**
 * A key that can lock {@link ILock}s.
 *
 * @author Choonster
 */
public class KeyItem extends Item {
	public KeyItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult useOn(final UseOnContext context) {
		return LockCapability.getLock(context.getLevel(), context.getClickedPos(), context.getClickedFace())
				.<InteractionResult>map(lock -> {
					if (
							!context.getLevel().isClientSide
									&& context.getPlayer() instanceof final ServerPlayer serverPlayer
					) {
						if (lock.isLocked()) {
							serverPlayer.sendSystemMessage(
									Component.translatable("testmod3.lock.already_locked")
							);
						} else {
							NetworkUtil.openClientScreen(
									serverPlayer,
									ModClientScreenTypes.LOCK,
									new LockScreenData(context.getClickedPos(), context.getClickedFace())
							);
						}
					}

					return InteractionResult.SUCCESS;
				})
				.orElse(InteractionResult.PASS);
	}
}
