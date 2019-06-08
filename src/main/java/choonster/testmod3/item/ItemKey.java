package choonster.testmod3.item;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.util.NetworkUtil;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.text.TextComponentTranslation;

/**
 * A key that can lock {@link ILock}s.
 *
 * @author Choonster
 */
public class ItemKey extends Item {
	public ItemKey(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public EnumActionResult onItemUse(final ItemUseContext context) {
		return CapabilityLock.getLock(context.getWorld(), context.getPos(), context.getFace())
				.map(lock -> {
					if (!context.getWorld().isRemote && context.getPlayer() != null) {
						if (lock.isLocked()) {
							context.getPlayer().sendMessage(new TextComponentTranslation("testmod3:lock.already_locked"));
						} else {
							NetworkUtil.openClientGui((EntityPlayerMP) context.getPlayer(), GuiIDs.Client.LOCK, buffer -> {
								buffer.writeBlockPos(context.getPos());
								NetworkUtil.writeNullableFacing(context.getFace(), buffer);
							});
						}
					}

					return EnumActionResult.SUCCESS;
				})
				.orElse(EnumActionResult.PASS);
	}
}
