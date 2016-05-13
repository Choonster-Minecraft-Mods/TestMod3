package com.choonster.testmod3.inventory.itemhandler.wrapper;

import com.choonster.testmod3.inventory.itemhandler.IItemHandlerNameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IWorldNameable;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

/**
 * An {@link IItemHandlerNameable} wrapper around an {@link IWorldNameable} and one or more {@link IItemHandlerModifiable} inventories.
 *
 * @author Choonster
 */
public class NameableCombinedInvWrapper extends CombinedInvWrapper implements IItemHandlerNameable {
	/**
	 * The {@link IWorldNameable} to get the name from.
	 */
	private final IWorldNameable worldNameable;

	public NameableCombinedInvWrapper(IWorldNameable worldNameable, IItemHandlerModifiable... itemHandler) {
		super(itemHandler);
		this.worldNameable = worldNameable;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return worldNameable.getName();
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return worldNameable.hasCustomName();
	}

	/**
	 * Get the formatted ChatComponent that will be used for the sender's username in chat
	 */
	@Override
	public ITextComponent getDisplayName() {
		return worldNameable.getDisplayName();
	}
}
