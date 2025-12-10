package choonster.testmod3.world.inventory.itemhandler;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

/**
 * An extension of {@link LootItemHandler} with a {@link BlockEntity}.
 * <p>
 * This uses the {@link BlockEntity BlockEntity}'s {@link Level} and adds {@link ContextKey ContextKey}s for it.
 *
 * @author Choonster
 */
public class BlockEntityLootItemHandler extends LootItemHandler {
	public static BlockEntityLootItemHandler empty(final int size, final BlockEntity blockEntity) {
		return new BlockEntityLootItemHandler(size, blockEntity);
	}

	public static Codec<BlockEntityLootItemHandler> codec(final int size, final BlockEntity blockEntity) {
		final var lootTableCodec = RecordCodecBuilder.<BlockEntityLootItemHandler>create(builder ->
				lootTableCodecStart(builder).apply(
						builder,
						(lootTable, lootTableSeed) -> new BlockEntityLootItemHandler(size, blockEntity, lootTable, lootTableSeed)
				)
		);

		final var itemsCodec = RecordCodecBuilder.<BlockEntityLootItemHandler>create(builder ->
				itemsCodecStart(builder, size).apply(
						builder,
						(stacks) -> new BlockEntityLootItemHandler(stacks, blockEntity)
				)
		);

		return Codec.withAlternative(lootTableCodec, itemsCodec);
	}

	private final BlockEntity blockEntity;

	protected BlockEntityLootItemHandler(
			final int size,
			final BlockEntity blockEntity,
			final ResourceKey<LootTable> lootTable,
			final long lootTableSeed
	) {
		super(size, blockEntity::getLevel, lootTable, lootTableSeed);
		this.blockEntity = blockEntity;
	}

	protected BlockEntityLootItemHandler(final int size, final BlockEntity blockEntity) {
		super(size, blockEntity::getLevel);
		this.blockEntity = blockEntity;
	}

	protected BlockEntityLootItemHandler(final NonNullList<ItemStack> stacks, final BlockEntity blockEntity) {
		super(stacks, blockEntity::getLevel);
		this.blockEntity = blockEntity;
	}

	@Override
	protected void addAdditionalLootParameters(@Nullable final Player player, final LootParams.Builder builder) {
		builder.withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockEntity.getBlockPos()));
	}
}
