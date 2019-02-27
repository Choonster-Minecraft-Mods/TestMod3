package choonster.testmod3.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.network.MessageOpenLockGui;
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
							TestMod3.network.sendTo(new MessageOpenLockGui(context.getPos(), context.getFace()), (EntityPlayerMP) context.getPlayer());
						}
					}

					return EnumActionResult.SUCCESS;
				})
				.orElse(EnumActionResult.PASS);
	}
}
