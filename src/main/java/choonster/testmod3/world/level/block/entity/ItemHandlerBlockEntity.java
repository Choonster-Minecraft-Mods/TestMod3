package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.util.NameHolder;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.Nullable;

/**
 * A {@link BlockEntity} with a single {@link IItemHandler} inventory.
 *
 * @param <INVENTORY> The inventory type
 */
public abstract class ItemHandlerBlockEntity<INVENTORY extends IItemHandler> extends BlockEntity implements MenuProvider {
	private final Codec<INVENTORY> inventoryCodec = createInventoryCodec();

	private LazyOptional<INVENTORY> inventoryOptional = LazyOptional.of(this::createEmptyInventory);

	private NameHolder nameHolder = new NameHolder(getDefaultName());

	public ItemHandlerBlockEntity(final BlockEntityType<?> blockEntityType, final BlockPos pos, final BlockState state) {
		super(blockEntityType, pos, state);
	}

	/**
	 * Create and return the empty inventory.
	 *
	 * @return The inventory
	 */
	protected abstract INVENTORY createEmptyInventory();

	/**
	 * Create and return a codec for the inventory type.
	 *
	 * @return The inventory codec
	 */
	protected abstract Codec<INVENTORY> createInventoryCodec();

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
			player.openMenu(this, getBlockPos());
		}
	}

	public INVENTORY getInventory() {
		return inventoryOptional.orElseThrow(CapabilityNotPresentException::new);
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
	protected void loadAdditional(final CompoundTag tag, final HolderLookup.Provider registries) {
		super.loadAdditional(tag, registries);

		final var ops = registries.createSerializationContext(NbtOps.INSTANCE);

		final var inventory = inventoryCodec.parse(
				ops,
				tag.getCompound("ItemHandler")
		).getOrThrow();

		inventoryOptional.invalidate();
		inventoryOptional = LazyOptional.of(() -> inventory);

		nameHolder = NameHolder.CODEC.parse(
				ops,
				tag.getCompound("NameHolder")
		).getOrThrow();
	}

	@Override
	protected void saveAdditional(final CompoundTag tag, final HolderLookup.Provider registries) {
		super.saveAdditional(tag, registries);

		final var ops = registries.createSerializationContext(NbtOps.INSTANCE);

		tag.put("ItemHandler", inventoryCodec.encodeStart(
				ops,
				getInventory()
		).getOrThrow());

		tag.put("NameHolder", NameHolder.CODEC.encodeStart(
				ops,
				nameHolder
		).getOrThrow());
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		inventoryOptional.invalidate();
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		if (capability == ForgeCapabilities.ITEM_HANDLER) {
			return inventoryOptional.cast();
		}

		return super.getCapability(capability, facing);
	}
}
