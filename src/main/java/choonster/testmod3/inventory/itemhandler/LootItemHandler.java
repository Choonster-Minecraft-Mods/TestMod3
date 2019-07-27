package choonster.testmod3.inventory.itemhandler;

import choonster.testmod3.util.InventoryUtils;
import com.google.common.base.Preconditions;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.LockableLootTileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootParameterSets;
import net.minecraft.world.storage.loot.LootParameters;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.function.Supplier;

/**
 * An inventory that generates its contents from a {@link LootTable} the first time it's accessed by a player.
 * <p>
 * Adapted from {@link LockableLootTileEntity}.
 *
 * @author Choonster
 */
public class LootItemHandler extends NameableItemHandler {
	/**
	 * The {@link Supplier} to get the {@link World} from.
	 */
	protected final Supplier<World> worldSupplier;

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

	public LootItemHandler(final ITextComponent defaultName, final Supplier<World> worldSupplier) {
		super(defaultName);
		this.worldSupplier = worldSupplier;
	}

	public LootItemHandler(final int size, final ITextComponent defaultName, final Supplier<World> worldSupplier) {
		super(size, defaultName);
		this.worldSupplier = worldSupplier;
	}

	public LootItemHandler(final NonNullList<ItemStack> stacks, final ITextComponent defaultName, final Supplier<World> worldSupplier) {
		super(stacks, defaultName);
		this.worldSupplier = worldSupplier;
	}

	/**
	 * Write the {@link LootTable} location and seed to NBT if they're present.
	 *
	 * @param compound The compound tag
	 * @return Was the location written to NBT?
	 */
	protected boolean checkLootAndWrite(final CompoundNBT compound) {
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
	protected boolean checkLootAndRead(final CompoundNBT compound) {
		if (compound.contains("LootTable", Constants.NBT.TAG_STRING)) {
			lootTableLocation = new ResourceLocation(compound.getString("LootTable"));
			lootTableSeed = compound.getLong("LootTableSeed");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public CompoundNBT serializeNBT() {
		final CompoundNBT tagCompound = super.serializeNBT();

		if (checkLootAndWrite(tagCompound)) { // If the LootTable location exists, don't write the inventory contents to NBT
			tagCompound.remove("Items");
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(final CompoundNBT nbt) {
		if (checkLootAndRead(nbt)) { // If the LootTable location exists, don't read the inventory contents from NBT
			setSize(nbt.contains("Size", Constants.NBT.TAG_INT) ? nbt.getInt("Size") : stacks.size());
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
	public void fillWithLoot(@Nullable final PlayerEntity player) {
		final World world = worldSupplier.get();
		if (lootTableLocation != null && world != null && !world.isRemote) {
			final MinecraftServer server = Preconditions.checkNotNull(world.getServer());
			final LootTable lootTable = server.getLootTableManager().getLootTableFromLocation(lootTableLocation);
			lootTableLocation = null;

			final LootContext.Builder builder = new LootContext.Builder((ServerWorld) world)
					.withSeed(lootTableSeed);

			if (player != null) {
				builder.withLuck(player.getLuck())
						.withParameter(LootParameters.THIS_ENTITY, player);
			}

			addAdditionalLootParameters(player, builder);

			InventoryUtils.fillItemHandlerWithLoot(this, lootTable, builder.build(LootParameterSets.CHEST));
		}
	}

	/**
	 * Adds additional parameters to the loot context builder before loot is generated.
	 *
	 * @param player  The player generating the loot.
	 * @param builder The loot context builder
	 */
	protected void addAdditionalLootParameters(@Nullable final PlayerEntity player, final LootContext.Builder builder) {

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
