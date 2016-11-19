package choonster.testmod3.tileentity;

import choonster.testmod3.capability.lock.Lock;
import choonster.testmod3.inventory.itemhandler.ItemHandlerLoot;
import choonster.testmod3.util.IWorldContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

/**
 * A {@link TileEntity} with a single {@link ItemHandlerLoot} inventory that generates its contents from a {@link LootTable} the first time it's accessed.
 * <p>
 * Locked with a {@link Lock}.
 *
 * @author Choonster
 */
public abstract class TileEntityItemHandlerLoot extends TileEntityItemHandlerLockable<ItemHandlerLoot, Lock> implements IWorldContainer {

	@Override
	public void openGUI(World world, EntityPlayer player) {
		if (inventory.getLootTable() != null && player.isSpectator()) {
			player.sendMessage(new TextComponentTranslation("container.spectatorCantOpen").setStyle(new Style().setColor(TextFormatting.RED)));
		} else {
			super.openGUI(world, player);
		}
	}

	@Override
	public World getContainedWorld() {
		return getWorld();
	}
}
