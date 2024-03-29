package choonster.testmod3.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * A bow that fires continuously while right click is held.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/topic/36494-189-changing-the-values-of-bow-damage/
 *
 * @author Choonster
 */
public class ContinuousBowItem extends ModBowItem {

	/**
	 * The amount to multiply the use time by to determine the charge when firing arrows.
	 */
	private final int CHARGE_MULTIPLIER = 5;

	public ContinuousBowItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public int getUseDuration(final ItemStack stack) {
		return 10;
	}

	@Override
	public void releaseUsing(final ItemStack stack, final Level level, final LivingEntity livingEntity, final int timeLeft) {
		final int charge = (stack.getUseDuration() - timeLeft) * CHARGE_MULTIPLIER;
		fireArrow(stack, level, livingEntity, charge);
	}

	@Override
	public ItemStack finishUsingItem(final ItemStack stack, final Level level, final LivingEntity livingEntity) {
		final int charge = stack.getUseDuration() * CHARGE_MULTIPLIER;
		fireArrow(stack, level, livingEntity, charge);

		return stack;
	}
}
