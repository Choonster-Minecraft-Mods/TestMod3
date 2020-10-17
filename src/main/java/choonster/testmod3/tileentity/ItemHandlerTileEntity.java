package choonster.testmod3.tileentity;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A {@link TileEntity} with a single {@link IItemHandler} inventory.
 *
 * @param <INVENTORY> The inventory type
 */
public abstract class ItemHandlerTileEntity<INVENTORY extends IItemHandler & INBTSerializable<CompoundNBT>> extends TileEntity implements INamedContainerProvider {
	/**
	 * The inventory.
	 */
	protected final INVENTORY inventory = createInventory();

	private final LazyOptional<INVENTORY> holder = LazyOptional.of(() -> inventory);

	public ItemHandlerTileEntity(final TileEntityType<?> tileEntityType) {
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
	public void openGUI(final ServerPlayerEntity player) {
		if (!world.isRemote) {
			NetworkHooks.openGui(player, this, getPos());
		}
	}

	@Override
	public void read(final BlockState state, final CompoundNBT nbt) {
		super.read(state, nbt);
		inventory.deserializeNBT(nbt.getCompound("ItemHandler"));
	}

	@Override
	public CompoundNBT write(final CompoundNBT compound) {
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
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			return holder.cast();
		}

		return super.getCapability(capability, facing);
	}
}
