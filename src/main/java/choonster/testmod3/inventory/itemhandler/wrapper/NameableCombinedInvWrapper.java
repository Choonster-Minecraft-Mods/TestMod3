package choonster.testmod3.inventory.itemhandler.wrapper;

import choonster.testmod3.inventory.itemhandler.IItemHandlerNameable;
import net.minecraft.util.INameable;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import javax.annotation.Nullable;

/**
 * An {@link IItemHandlerNameable} wrapper around an {@link INameable} and one or more {@link IItemHandlerModifiable} inventories.
 *
 * @author Choonster
 */
public class NameableCombinedInvWrapper extends CombinedInvWrapper implements IItemHandlerNameable {
	/**
	 * The {@link INameable} to get the name from.
	 */
	private final INameable nameable;

	public NameableCombinedInvWrapper(final INameable nameable, final IItemHandlerModifiable... itemHandler) {
		super(itemHandler);
		this.nameable = nameable;
	}

	@Override
	public ITextComponent getDisplayName() {
		return nameable.getDisplayName();
	}

	@Override
	public ITextComponent getName() {
		return nameable.getName();
	}

	@Override
	public boolean hasCustomName() {
		return nameable.hasCustomName();
	}

	@Nullable
	@Override
	public ITextComponent getCustomName() {
		return nameable.getCustomName();
	}
}
