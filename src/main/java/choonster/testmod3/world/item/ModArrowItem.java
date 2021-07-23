package choonster.testmod3.world.item;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;

import java.util.function.BiFunction;

/**
 * An arrow item that spawns the arrow entity specified in the constructor.
 *
 * @author Choonster
 */
public class ModArrowItem extends ArrowItem {
	/**
	 * A factory function to create the arrow entity.
	 */
	private final BiFunction<Level, LivingEntity, Arrow> entityFactory;

	public ModArrowItem(final BiFunction<Level, LivingEntity, Arrow> entityFactory, final Item.Properties properties) {
		super(properties);
		this.entityFactory = entityFactory;
	}

	@Override
	public AbstractArrow createArrow(final Level level, final ItemStack stack, final LivingEntity shooter) {
		final Arrow entityModArrow = entityFactory.apply(level, shooter);
		entityModArrow.setEffectsFromItem(stack);
		return entityModArrow;
	}

	@Override
	public boolean isInfinite(final ItemStack stack, final ItemStack bow, final Player player) {
		return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
	}
}
