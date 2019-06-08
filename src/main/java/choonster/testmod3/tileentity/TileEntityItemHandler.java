package choonster.testmod3.tileentity;

import choonster.testmod3.util.InventoryUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A {@link TileEntity} with a single {@link IItemHandler} inventory.
 *
 * @param <INVENTORY> The inventory type
 */
public abstract class TileEntityItemHandler<INVENTORY extends IItemHandler & INBTSerializable<NBTTagCompound>> extends TileEntity implements IInteractionObject {
	/**
	 * The inventory.
	 */
	protected final INVENTORY inventory = createInventory();

	private final LazyOptional<INVENTORY> holder = LazyOptional.of(() -> inventory);

	public TileEntityItemHandler(final TileEntityType<?> tileEntityType) {
		super(tileEntityType);
	}

	/**
	 * Create and return the inventory.
	 *
	 * @return The inventory
	 */
	protected abstract INVENTORY createInventory();

	/**
	 * Open the GUI for the specified player.
	 *
	 * @param player The player
	 */
	public void openGUI(final EntityPlayerMP player) {
		if (!world.isRemote) {
			NetworkHooks.openGui(player, this, getPos());
		}
	}

	/**
	 * Get the inventory contents to drop.
	 *
	 * @return The drops list
	 */
	public List<ItemStack> getDrops() {
		return InventoryUtils.dropItemHandlerContents(inventory, getWorld().rand);
	}

	@Override
	public void read(final NBTTagCompound compound) {
		super.read(compound);
		inventory.deserializeNBT(compound.getCompound("ItemHandler"));
	}

	@Override
	public NBTTagCompound write(final NBTTagCompound compound) {
		super.write(compound);
		compound.put("ItemHandler", inventory.serializeNBT());
		return compound;
	}

	@Override
	protected void invalidateCaps() {
		super.invalidateCaps();
		holder.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return holder.cast();
		}

		return super.getCapability(capability, facing);
	}
}
