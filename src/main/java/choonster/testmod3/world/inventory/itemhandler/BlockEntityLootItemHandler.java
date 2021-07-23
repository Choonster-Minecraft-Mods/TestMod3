package choonster.testmod3.world.inventory.itemhandler;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;

/**
 * An extension of {@link LootItemHandler} with a {@link BlockEntity}.
 * <p>
 * This uses the {@link BlockEntity}'s {@link Level} and adds {@link LootContextParam}s for it.
 *
 * @author Choonster
 */
public class BlockEntityLootItemHandler extends LootItemHandler {
	private final BlockEntity blockEntity;

	public BlockEntityLootItemHandler(final BlockEntity blockEntity) {
		super(blockEntity::getLevel);
		this.blockEntity = blockEntity;
	}

	public BlockEntityLootItemHandler(final int size, final BlockEntity blockEntity) {
		super(size, blockEntity::getLevel);
		this.blockEntity = blockEntity;
	}

	public BlockEntityLootItemHandler(final NonNullList<ItemStack> stacks, final BlockEntity blockEntity) {
		super(stacks, blockEntity::getLevel);
		this.blockEntity = blockEntity;
	}

	@Override
	protected void addAdditionalLootParameters(@Nullable final Player player, final LootContext.Builder builder) {
		builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockEntity.getBlockPos()));
	}
}
