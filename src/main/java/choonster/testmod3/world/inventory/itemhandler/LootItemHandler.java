package choonster.testmod3.world.inventory.itemhandler;

import choonster.testmod3.util.InventoryUtils;
import com.google.common.base.Preconditions;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * An inventory that generates its contents from a {@link LootTable} the first time it's accessed by a player.
 * <p>
 * Adapted from {@link BaseContainerBlockEntity}.
 *
 * @author Choonster
 */
public class LootItemHandler extends ItemStackHandler {
	/**
	 * The {@link Supplier} to get the {@link Level} from.
	 */
	protected final Supplier<Level> levelSupplier;

	/**
	 * The location of the {@link LootTable} to generate loot from.
	 * <p>
	 * This will be {@code null} if no {@link LootTable} has been set or loot has already been generated.
	 */
	protected ResourceLocation lootTableLocation;

	/**
	 * The random seed to use when generating loot.
	 */
	protected long lootTableSeed;

	public LootItemHandler(final Supplier<Level> levelSupplier) {
		this.levelSupplier = levelSupplier;
	}

	public LootItemHandler(final int size, final Supplier<Level> levelSupplier) {
		super(size);
		this.levelSupplier = levelSupplier;
	}

	public LootItemHandler(final NonNullList<ItemStack> stacks, final Supplier<Level> levelSupplier) {
		super(stacks);
		this.levelSupplier = levelSupplier;
	}

	/**
	 * Write the {@link LootTable} location and seed to NBT if they're present.
	 *
	 * @param compound The compound tag
	 * @return Was the location written to NBT?
	 */
	protected boolean checkLootAndWrite(final CompoundTag compound) {
		if (lootTableLocation != null) {
			compound.putString("LootTable", lootTableLocation.toString());

			if (lootTableSeed != 0L) {
				compound.putLong("LootTableSeed", lootTableSeed);
			}

			return true;
		} else {
			return false;
		}
	}

	/**
	 * Read the {@link LootTable} location and seed from NBT if they're present.
	 *
	 * @param compound The compound tag
	 * @return Was the location read from NBT?
	 */
	protected boolean checkLootAndRead(final CompoundTag compound) {
		if (compound.contains("LootTable", Tag.TAG_STRING)) {
			lootTableLocation = new ResourceLocation(compound.getString("LootTable"));
			lootTableSeed = compound.getLong("LootTableSeed");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public CompoundTag serializeNBT() {
		final CompoundTag tagCompound = super.serializeNBT();

		if (checkLootAndWrite(tagCompound)) { // If the LootTable location exists, don't write the inventory contents to NBT
			tagCompound.remove("Items");
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(final CompoundTag nbt) {
		if (checkLootAndRead(nbt)) { // If the LootTable location exists, don't read the inventory contents from NBT
			setSize(nbt.contains("Size", Tag.TAG_INT) ? nbt.getInt("Size") : stacks.size());
			onLoad();
		} else {
			super.deserializeNBT(nbt);
		}
	}

	/**
	 * Fill this inventory with loot.
	 * <p>
	 * Does nothing if no loot table has been set, loot has already been generated or this is being called on the client side.
	 *
	 * @param player The player generating the loot.
	 */
	public void fillWithLoot(@Nullable final Player player) {
		final Level level = levelSupplier.get();
		if (lootTableLocation != null && level != null && !level.isClientSide) {
			final MinecraftServer server = Preconditions.checkNotNull(level.getServer());
			final LootTable lootTable = server.getLootTables().get(lootTableLocation);
			lootTableLocation = null;

			final LootContext.Builder builder = new LootContext.Builder((ServerLevel) level)
					.withOptionalRandomSeed(lootTableSeed);

			if (player != null) {
				builder.withLuck(player.getLuck())
						.withParameter(LootContextParams.THIS_ENTITY, player);
			}

			addAdditionalLootParameters(player, builder);

			InventoryUtils.fillItemHandlerWithLoot(this, lootTable, builder.create(LootContextParamSets.CHEST));
		}
	}

	/**
	 * Adds additional parameters to the loot context builder before loot is generated.
	 *
	 * @param player  The player generating the loot.
	 * @param builder The loot context builder
	 */
	protected void addAdditionalLootParameters(@Nullable final Player player, final LootContext.Builder builder) {

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
	public ResourceLocation getLootTable() {
		return lootTableLocation;
	}
}
