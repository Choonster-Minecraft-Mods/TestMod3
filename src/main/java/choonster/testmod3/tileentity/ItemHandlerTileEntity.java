package choonster.testmod3.tileentity;

import choonster.testmod3.util.NameHolder;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
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

	protected final NameHolder nameHolder = new NameHolder(getDefaultName());

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
	 * Gets the default name of this TileEntity.
	 *
	 * @return The default name
	 */
	protected abstract ITextComponent getDefaultName();

	/**
	 * Open the GUI for the specified player.
	 *
	 * @param player The player
	 */
	public void openGUI(final ServerPlayerEntity player) {
		if (!level.isClientSide) {
			NetworkHooks.openGui(player, this, getBlockPos());
		}
	}

	public INVENTORY getInventory() {
		return inventory;
	}

	public INameable getNameHolder() {
		return nameHolder;
	}

	public void setDisplayName(final ITextComponent displayName) {
		nameHolder.setCustomName(displayName);
	}

	@Override
	public ITextComponent getDisplayName() {
		return nameHolder.getDisplayName();
	}

	@Override
	public void load(final BlockState state, final CompoundNBT nbt) {
		super.load(state, nbt);
		inventory.deserializeNBT(nbt.getCompound("ItemHandler"));
		nameHolder.deserializeNBT(nbt.getCompound("NameHolder"));
	}

	@Override
	public CompoundNBT save(final CompoundNBT compound) {
		super.save(compound);

		compound.put("ItemHandler", inventory.serializeNBT());
		compound.put("NameHolder", nameHolder.serializeNBT());

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
