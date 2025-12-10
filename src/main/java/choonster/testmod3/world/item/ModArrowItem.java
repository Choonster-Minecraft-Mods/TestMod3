package choonster.testmod3.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

/**
 * An arrow item that spawns the arrow entity specified in the constructor.
 *
 * @author Choonster
 */
public class ModArrowItem extends ArrowItem {
	/**
	 * A factory function to create the arrow entity.
	 */
	private final ArrowFactory arrowFactory;

	public ModArrowItem(final ArrowFactory arrowFactory, final Item.Properties properties) {
		super(properties);
		this.arrowFactory = arrowFactory;
	}

	@Override
	public AbstractArrow createArrow(final Level level, final ItemStack stack, final LivingEntity shooter, @Nullable final ItemStack firedFromWeapon) {
		return arrowFactory.create(level, shooter, stack.copyWithCount(1), firedFromWeapon);
	}

	@FunctionalInterface
	public interface ArrowFactory {
		Arrow create(final Level level, final LivingEntity shooter, final ItemStack pickupItemStack, @Nullable final ItemStack firedFromWeapon);
	}
}
