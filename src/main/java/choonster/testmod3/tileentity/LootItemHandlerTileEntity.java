package choonster.testmod3.tileentity;

import choonster.testmod3.capability.lock.Lock;
import choonster.testmod3.inventory.itemhandler.LootItemHandler;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.storage.loot.LootTable;

/**
 * A {@link TileEntity} with a single {@link LootItemHandler} inventory that generates its contents from a {@link LootTable} the first time it's accessed.
 * <p>
 * Locked with a {@link Lock}.
 *
 * @author Choonster
 */
public abstract class LootItemHandlerTileEntity extends LockableItemHandlerTileEntity<LootItemHandler, Lock> {

	public LootItemHandlerTileEntity(final TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}

	@Override
	public ITextComponent getDisplayName() {
		return inventory.getDisplayName();
	}

	@Override
	public void openGUI(final ServerPlayerEntity player) {
		if (inventory.getLootTable() != null && player.isSpectator()) {
			player.sendMessage(new TranslationTextComponent("container.spectatorCantOpen").setStyle(new Style().setColor(TextFormatting.RED)));
		} else {
			super.openGUI(player);
		}
	}
}
