package com.choonster.testmod3.inventory.itemhandler;

import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.ItemStackHandler;

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
	private ITextComponent displayName;

	public ItemHandlerNameable(ITextComponent defaultName) {
		this.defaultName = defaultName.createCopy();
	}

	public ItemHandlerNameable(int size, ITextComponent defaultName) {
		super(size);
		this.defaultName = defaultName.createCopy();
	}

	public ItemHandlerNameable(ItemStack[] stacks, ITextComponent defaultName) {
		super(stacks);
		this.defaultName = defaultName.createCopy();
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return getDisplayName().getUnformattedText();
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return displayName != null;
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		return hasCustomName() ? displayName : defaultName;
	}

	/**
	 * Set the display name of this inventory.
	 *
	 * @param displayName The display name
	 */
	public void setDisplayName(ITextComponent displayName) {
		this.displayName = displayName.createCopy();
	}
}
