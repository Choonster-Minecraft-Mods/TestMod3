package choonster.testmod3.dispensebehavior;

import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBehaviorDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.init.Bootstrap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;

/**
 * An extension of {@link Bootstrap.BehaviorDispenseOptional} that can delegate to another {@link IBehaviorDispenseItem} implementation.
 *
 * @author Choonster
 */
public class BehaviorDispenseDelegate extends Bootstrap.BehaviorDispenseOptional {
	/**
	 * The {@link IBehaviorDispenseItem} to delegate to.
	 */
	protected final IBehaviorDispenseItem delegate;

	/**
	 * Whether to play sounds and spawn particles. Should be set from an override of {@link BehaviorDefaultDispenseItem#dispenseStack}.
	 */
	protected boolean doSoundsParticles;

	public BehaviorDispenseDelegate(final IBehaviorDispenseItem delegate) {
		this.delegate = delegate;
	}

	/**
	 * Call the delegate's {@link IBehaviorDispenseItem#dispense} method without playing sounds or spawning particles
	 * from this behavior.
	 *
	 * @param source The block source
	 * @param stack  The item
	 * @return The replacement item
	 */
	protected ItemStack callDelegate(final IBlockSource source, final ItemStack stack) {
		doSoundsParticles = false;
		return delegate.dispense(source, stack);
	}

	@Override
	protected void spawnDispenseParticles(final IBlockSource source, final EnumFacing facingIn) {
		if (doSoundsParticles) {
			super.spawnDispenseParticles(source, facingIn);
		}
	}

	@Override
	protected void playDispenseSound(final IBlockSource source) {
		if (doSoundsParticles) {
			super.playDispenseSound(source);
		}
	}
}
