package choonster.testmod3.item;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.AbstractArrowEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.item.ArrowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

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
	private final BiFunction<World, LivingEntity, ArrowEntity> entityFactory;

	public ModArrowItem(final BiFunction<World, LivingEntity, ArrowEntity> entityFactory, final Item.Properties properties) {
		super(properties);
		this.entityFactory = entityFactory;
	}

	@Override
	public AbstractArrowEntity createArrow(final World worldIn, final ItemStack stack, final LivingEntity shooter) {
		final ArrowEntity entityModArrow = entityFactory.apply(worldIn, shooter);
		entityModArrow.setEffectsFromItem(stack);
		return entityModArrow;
	}

	@Override
	public boolean isInfinite(final ItemStack stack, final ItemStack bow, final PlayerEntity player) {
		return EnchantmentHelper.getItemEnchantmentLevel(Enchantments.INFINITY_ARROWS, bow) > 0;
	}
}
