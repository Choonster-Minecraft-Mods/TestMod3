package choonster.testmod3.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.Objects;

/**
 * An inventory that can be named.
 *
 * @author Choonster
 */
public class ItemHandlerNameable extends ItemStackHandler implements IItemHandlerNameable {
	/**
	 * The default name of this inventory.
	 */
	private final ITextComponent defaultName;

	/**
	 * The custom name of this inventory, if any.
	 */
	private ITextComponent customName;

	public ItemHandlerNameable(final ITextComponent defaultName) {
		this.defaultName = defaultName.deepCopy();
	}

	public ItemHandlerNameable(final int size, final ITextComponent defaultName) {
		super(size);
		this.defaultName = defaultName.deepCopy();
	}

	public ItemHandlerNameable(final NonNullList<ItemStack> stacks, final ITextComponent defaultName) {
		super(stacks);
		this.defaultName = defaultName.deepCopy();
	}

	@Override
	public ITextComponent getName() {
		return hasCustomName() ? customName : defaultName;
	}

	@Override
	public boolean hasCustomName() {
		return customName != null;
	}

	@Nullable
	@Override
	public ITextComponent getCustomName() {
		return customName;
	}

	/**
	 * Set the custom name of this inventory.
	 *
	 * @param customName The custom name
	 */
	public void setCustomName(final ITextComponent customName) {
		this.customName = customName.deepCopy();
	}

	@Override
	public NBTTagCompound serializeNBT() {
		final NBTTagCompound tagCompound = super.serializeNBT();

		if (hasCustomName()) {
			tagCompound.putString("DisplayName", ITextComponent.Serializer.toJson(getDisplayName()));
		}

		return tagCompound;
	}

	@Override
	public void deserializeNBT(final NBTTagCompound nbt) {
		super.deserializeNBT(nbt);

		if (nbt.contains("DisplayName")) {
			final ITextComponent customName = Objects.requireNonNull(ITextComponent.Serializer.fromJson(nbt.getString("DisplayName")));
			setCustomName(customName);
		}
	}
}
