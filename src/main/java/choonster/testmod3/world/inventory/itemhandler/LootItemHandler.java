package choonster.testmod3.world.inventory.itemhandler;

import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.util.InventoryUtils;
import com.google.common.base.Preconditions;
import com.mojang.datafixers.Products;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * An inventory that generates its contents from a {@link LootTable} the first time it's accessed by a player.
 * <p>
 * Adapted from {@link BaseContainerBlockEntity}.
 *
 * @author Choonster
 */
public class LootItemHandler extends ItemStackHandler {
	public static LootItemHandler empty(final int size, final Supplier<Level> levelSupplier) {
		return new LootItemHandler(size, levelSupplier);
	}

	public static Codec<LootItemHandler> codec(final int size, final Supplier<Level> levelSupplier) {
		final var lootTableCodec = RecordCodecBuilder.<LootItemHandler>create(builder ->
				lootTableCodecStart(builder).apply(
						builder,
						(lootTable, lootTableSeed) -> new LootItemHandler(size, levelSupplier, lootTable, lootTableSeed)
				)
		);

		final var itemsCodec = RecordCodecBuilder.<LootItemHandler>create(builder ->
				itemsCodecStart(builder, size).apply(
						builder,
						(stacks) -> new LootItemHandler(stacks, levelSupplier)
				)
		);

		return Codec.withAlternative(lootTableCodec, itemsCodec);
	}

	protected static <T extends LootItemHandler> Products.P2<
			RecordCodecBuilder.Mu<T>,
			ResourceKey<LootTable>,
			Long
			> lootTableCodecStart(final RecordCodecBuilder.Instance<T> builder) {
		return builder.group(
				ResourceKey.codec(Registries.LOOT_TABLE)
						.fieldOf("loot_table")
						.forGetter(LootItemHandler::getLootTable),

				Codec.LONG
						.optionalFieldOf("loot_table_seed", 0L)
						.forGetter(lootItemHandler -> lootItemHandler.lootTableSeed)

		);
	}

	protected static <T extends LootItemHandler> Products.P1<
			RecordCodecBuilder.Mu<T>,
			NonNullList<ItemStack>
			> itemsCodecStart(final RecordCodecBuilder.Instance<T> builder, final int size) {
		return builder.group(
				VanillaCodecs.itemListCodec(size)
						.fieldOf("items")
						.forGetter(lootItemHandler -> lootItemHandler.stacks)
		);
	}

	/**
	 * The {@link Supplier} to get the {@link Level} from.
	 */
	protected final Supplier<Level> levelSupplier;

	/**
	 * The key of the {@link LootTable} to generate loot from.
	 * <p>
	 * This will be {@code null} if no {@link LootTable} has been set or loot has already been generated.
	 */
	@Nullable
	protected ResourceKey<LootTable> lootTable;

	/**
	 * The random seed to use when generating loot.
	 */
	protected long lootTableSeed;

	protected LootItemHandler(
			final int size,
			final Supplier<Level> levelSupplier,
			final ResourceKey<LootTable> lootTable,
			final long lootTableSeed
	) {
		super(size);
		this.levelSupplier = levelSupplier;
		this.lootTable = lootTable;
		this.lootTableSeed = lootTableSeed;
	}

	protected LootItemHandler(final int size, final Supplier<Level> levelSupplier) {
		super(size);
		this.levelSupplier = levelSupplier;
	}

	protected LootItemHandler(final NonNullList<ItemStack> stacks, final Supplier<Level> levelSupplier) {
		super(stacks);
		this.levelSupplier = levelSupplier;
	}

	/**
	 * Fill this inventory with loot.
	 * <p>
	 * Does nothing if no loot table has been set, loot has already been generated or this is being called on the client side.
	 *
	 * @param player The player generating the loot.
	 */
	public void fillWithLoot(@Nullable final Player player) {
		final var level = levelSupplier.get();
		if (lootTable != null && level != null && !level.isClientSide()) {
			final var server = Preconditions.checkNotNull(level.getServer());
			final var lootTable = server.reloadableRegistries().getLootTable(this.lootTable);
			this.lootTable = null;

			final var builder = new LootParams.Builder((ServerLevel) level);

			if (player != null) {
				builder.withLuck(player.getLuck())
						.withParameter(LootContextParams.THIS_ENTITY, player);
			}

			addAdditionalLootParameters(player, builder);

			InventoryUtils.fillItemHandlerWithLoot(this, lootTable, builder.create(LootContextParamSets.CHEST), lootTableSeed);
		}
	}

	/**
	 * Adds additional parameters to the loot params builder before loot is generated.
	 *
	 * @param player  The player generating the loot.
	 * @param builder The loot params builder
	 */
	protected void addAdditionalLootParameters(@Nullable final Player player, final LootParams.Builder builder) {

	}

	@Override
	public ItemStack getStackInSlot(final int slot) {
		fillWithLoot(null);
		return super.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(final int slot, final ItemStack stack, final boolean simulate) {
		fillWithLoot(null);
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
		fillWithLoot(null);
		return super.extractItem(slot, amount, simulate);
	}

	@Override
	public void setStackInSlot(final int slot, final ItemStack stack) {
		fillWithLoot(null);
		super.setStackInSlot(slot, stack);
	}

	@Nullable
	public ResourceKey<LootTable> getLootTable() {
		return lootTable;
	}
}
