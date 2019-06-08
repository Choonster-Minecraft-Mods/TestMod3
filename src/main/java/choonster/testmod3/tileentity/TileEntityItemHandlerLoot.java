package choonster.testmod3.tileentity;

import choonster.testmod3.capability.lock.Lock;
import choonster.testmod3.inventory.itemhandler.ItemHandlerLoot;
import choonster.testmod3.util.IWorldContainer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraft.world.storage.loot.LootTable;

import javax.annotation.Nullable;

/**
 * A {@link TileEntity} with a single {@link ItemHandlerLoot} inventory that generates its contents from a {@link LootTable} the first time it's accessed.
 * <p>
 * Locked with a {@link Lock}.
 *
 * @author Choonster
 */
public abstract class TileEntityItemHandlerLoot extends TileEntityItemHandlerLockable<ItemHandlerLoot, Lock> implements IWorldContainer {

	public TileEntityItemHandlerLoot(final TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}

	@Override
	public ITextComponent getDisplayName() {
		return inventory.getDisplayName();
	}

	@Override
	public ITextComponent getName() {
		return inventory.getName();
	}

	@Override
	public boolean hasCustomName() {
		return inventory.hasCustomName();
	}

	@Nullable
	@Override
	public ITextComponent getCustomName() {
		return inventory.getCustomName();
	}

	@Override
	public void openGUI(final EntityPlayerMP player) {
		if (inventory.getLootTable() != null && player.isSpectator()) {
			player.sendMessage(new TextComponentTranslation("container.spectatorCantOpen").setStyle(new Style().setColor(TextFormatting.RED)));
		} else {
			super.openGUI(player);
		}
	}

	@Override
	public World getContainedWorld() {
		return getWorld();
	}
}
