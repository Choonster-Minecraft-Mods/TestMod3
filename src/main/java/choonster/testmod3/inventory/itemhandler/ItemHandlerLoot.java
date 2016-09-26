package choonster.testmod3.inventory.itemhandler;

import choonster.testmod3.util.IWorldContainer;
import choonster.testmod3.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntityLockableLoot;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.storage.loot.ILootContainer;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.util.Constants;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * An inventory that generates its contents from a {@link LootTable} the first time it's accessed by a player.
 * <p>
 * Adapted from {@link TileEntityLockableLoot}.
 *
 * @author Choonster
 */
public class ItemHandlerLoot extends ItemHandlerNameable implements ILootContainer {
	/**
	 * The {@link IWorldContainer} to get the {@link World} from.
	 */
	protected final IWorldContainer worldContainer;

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

	public ItemHandlerLoot(ITextComponent defaultName, IWorldContainer worldContainer) {
		super(defaultName);
		this.worldContainer = worldContainer;
	}

	public ItemHandlerLoot(int size, ITextComponent defaultName, IWorldContainer worldContainer) {
		super(size, defaultName);
		this.worldContainer = worldContainer;
	}

	public ItemHandlerLoot(ItemStack[] stacks, ITextComponent defaultName, IWorldContainer worldContainer) {
		super(stacks, defaultName);
		this.worldContainer = worldContainer;
	}

	/**
	 * Write the {@link LootTable} location and seed to NBT if they're present.
	 *
	 * @param compound The compound tag
	 * @return Was the location written to NBT?
	 */
	protected boolean checkLootAndWrite(NBTTagCompound compound) {
		if (lootTableLocation != null) {
			compound.setString("LootTable", lootTableLocation.toString());

			if (lootTableSeed != 0L) {
				compound.setLong("LootTableSeed", lootTableSeed);
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
	protected boolean checkLootAndRead(NBTTagCompound compound) {
		if (compound.hasKey("LootTable", Constants.NBT.TAG_STRING)) {
			lootTableLocation = new ResourceLocation(compound.getString("LootTable"));
			lootTableSeed = compound.getLong("LootTableSeed");
			return true;
		} else {
			return false;
		}
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tagCompound = super.serializeNBT();

		if (checkLootAndWrite(tagCompound)) { // If the LootTable location exists, don't write the inventory contents to NBT
			tagCompound.removeTag("Items");
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(NBTTagCompound nbt) {
		if (checkLootAndRead(nbt)) { // If the LootTable location exists, don't read the inventory contents from NBT
			setSize(nbt.hasKey("Size", Constants.NBT.TAG_INT) ? nbt.getInteger("Size") : stacks.length);
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
	 * @param player The player whose Luck to use when generating loot
	 */
	public void fillWithLoot(@Nullable EntityPlayer player) {
		final World world = worldContainer.getContainedWorld();
		if (lootTableLocation != null && !world.isRemote) {
			final LootTable lootTable = world.getLootTableManager().getLootTableFromLocation(lootTableLocation);
			lootTableLocation = null;

			final Random random = lootTableSeed == 0 ? new Random() : new Random(lootTableSeed);

			final LootContext.Builder builder = new LootContext.Builder((WorldServer) world);

			if (player != null) {
				builder.withLuck(player.getLuck());
			}

			InventoryUtils.fillItemHandlerWithLoot(this, lootTable, random, builder.build());
		}
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		fillWithLoot(null);
		return super.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		fillWithLoot(null);
		return super.insertItem(slot, stack, simulate);
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		fillWithLoot(null);
		return super.extractItem(slot, amount, simulate);
	}

	@Override
	public void setStackInSlot(int slot, ItemStack stack) {
		fillWithLoot(null);
		super.setStackInSlot(slot, stack);
	}

	@Nullable
	@Override
	public ResourceLocation getLootTable() {
		return lootTableLocation;
	}
}
