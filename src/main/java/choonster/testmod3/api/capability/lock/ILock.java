package choonster.testmod3.api.capability.lock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketSoundEffect;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.ChatType;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.IWorldNameable;
import net.minecraft.world.LockCode;

/**
 * A capability allowing things to be locked with a {@link LockCode} and only opened by players holding an item with a matching display name.
 * <p>
 * This is a copy of {@link net.minecraft.world.ILockableContainer} that doesn't implement {@link net.minecraft.inventory.IInventory}.
 *
 * @author Choonster
 */
public interface ILock extends IWorldNameable {

	/**
	 * @return Is this locked?
	 */
	boolean isLocked();

	/**
	 * Set the lock code.
	 *
	 * @param code The lock code
	 */
	void setLockCode(final LockCode code);

	/**
	 * Get the lock code.
	 *
	 * @return The lock code
	 */
	LockCode getLockCode();

	/**
	 * Try to open this lock, notifying the player if they can't.
	 *
	 * @param player The player opening the lock
	 * @return Was the player allowed to open the lock?
	 */
	default boolean tryOpen(final EntityPlayer player) {
		// Adapted from EntityPlayerMP#displayGUIChest

		final LockCode lockCode = getLockCode();
		if (isLocked() && !player.canOpen(lockCode) && !player.isSpectator()) {
			if (player instanceof EntityPlayerMP) {
				final EntityPlayerMP playerMP = (EntityPlayerMP) player;
				playerMP.connection.sendPacket(new SPacketChat(new TextComponentTranslation("container.isLocked", getDisplayName()), ChatType.GAME_INFO));
				playerMP.connection.sendPacket(new SPacketSoundEffect(SoundEvents.BLOCK_CHEST_LOCKED, SoundCategory.BLOCKS, playerMP.posX, playerMP.posY, playerMP.posZ, 1.0F, 1.0F));
			}

			return false;
		}

		return true;
	}
}
