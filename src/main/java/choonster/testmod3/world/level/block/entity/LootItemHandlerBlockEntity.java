package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.capability.lock.Lock;
import choonster.testmod3.world.inventory.itemhandler.LootItemHandler;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootTable;


/**
 * A {@link BlockEntity} with a single {@link LootItemHandler} inventory that generates its contents from a {@link LootTable} the first time it's accessed.
 * <p>
 * Locked with a {@link Lock}.
 *
 * @author Choonster
 */
public abstract class LootItemHandlerBlockEntity extends LockableItemHandlerBlockEntity<LootItemHandler, Lock> {

	public LootItemHandlerBlockEntity(final BlockEntityType<?> blockEntityType, final BlockPos pos, final BlockState state) {
		super(blockEntityType, pos, state);
	}

	@Override
	public void openGUI(final ServerPlayer player) {
		if (inventory.getLootTable() != null && player.isSpectator()) {
			player.sendSystemMessage(Component.translatable("container.spectatorCantOpen").setStyle(Style.EMPTY.withColor(ChatFormatting.RED)));
		} else {
			super.openGUI(player);
		}
	}
}
