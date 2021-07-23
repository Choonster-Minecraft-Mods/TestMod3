package choonster.testmod3.world.item;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.InteractionResult;
import net.minecraft.Util;
import net.minecraft.network.chat.TranslatableComponent;

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
				.map(lock -> {
					if (!context.getLevel().isClientSide && context.getPlayer() != null) {
						if (lock.isLocked()) {
							context.getPlayer().sendMessage(new TranslatableComponent("testmod3.lock.already_locked"), Util.NIL_UUID);
						} else {
							NetworkUtil.openClientGui((ServerPlayer) context.getPlayer(), GuiIDs.Client.LOCK, buffer -> {
								buffer.writeBlockPos(context.getClickedPos());
								NetworkUtil.writeNullableFacing(context.getClickedFace(), buffer);
							});
						}
					}

					return InteractionResult.SUCCESS;
				})
				.orElse(InteractionResult.PASS);
	}
}
