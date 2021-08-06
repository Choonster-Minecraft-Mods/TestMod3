package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.util.NameHolder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fmllegacy.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;

/**
 * A {@link BlockEntity} with a single {@link IItemHandler} inventory.
 *
 * @param <INVENTORY> The inventory type
 */
public abstract class ItemHandlerBlockEntity<INVENTORY extends IItemHandler & INBTSerializable<CompoundTag>> extends BlockEntity implements MenuProvider {
	/**
	 * The inventory.
	 */
	protected final INVENTORY inventory = createInventory();

	private final LazyOptional<INVENTORY> holder = LazyOptional.of(() -> inventory);

	protected final NameHolder nameHolder = new NameHolder(getDefaultName());

	public ItemHandlerBlockEntity(final BlockEntityType<?> blockEntityType, final BlockPos pos, final BlockState state) {
		super(blockEntityType, pos, state);
	}

	/**
	 * Create and return the inventory.
	 *
	 * @return The inventory
	 */
	protected abstract INVENTORY createInventory();

	/**
	 * Gets the default name of this BlockEntity.
	 *
	 * @return The default name
	 */
	protected abstract Component getDefaultName();

	/**
	 * Open the GUI for the specified player.
	 *
	 * @param player The player
	 */
	public void openGUI(final ServerPlayer player) {
		if (!level.isClientSide) {
			NetworkHooks.openGui(player, this, getBlockPos());
		}
	}

	public INVENTORY getInventory() {
		return inventory;
	}

	public Nameable getNameHolder() {
		return nameHolder;
	}

	public void setDisplayName(final Component displayName) {
		nameHolder.setCustomName(displayName);
	}

	@Override
	public Component getDisplayName() {
		return nameHolder.getDisplayName();
	}

	@Override
	public void load(final CompoundTag nbt) {
		super.load(nbt);
		inventory.deserializeNBT(nbt.getCompound("ItemHandler"));
		nameHolder.deserializeNBT(nbt.getCompound("NameHolder"));
	}

	@Override
	public CompoundTag save(final CompoundTag compound) {
		super.save(compound);

		compound.put("ItemHandler", inventory.serializeNBT());
		compound.put("NameHolder", nameHolder.serializeNBT());

		return compound;
	}

	@Override
	public void invalidateCaps() {
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
