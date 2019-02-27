package choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.function.BiFunction;

/**
 * An arrow fired from this mod's bows.
 *
 * @author Choonster
 */
public class ItemModArrow extends ItemArrow {
	/**
	 * A factory function to create the arrow entity.
	 */
	private final BiFunction<World, EntityLivingBase, EntityTippedArrow> entityFactory;

	public ItemModArrow(final BiFunction<World, EntityLivingBase, EntityTippedArrow> entityFactory, final Item.Properties properties) {
		super(properties);
		this.entityFactory = entityFactory;
	}

	@Override
	public EntityArrow createArrow(final World worldIn, final ItemStack stack, final EntityLivingBase shooter) {
		final EntityTippedArrow entityModArrow = entityFactory.apply(worldIn, shooter);
		entityModArrow.setPotionEffect(stack);
		return entityModArrow;
	}

	@Override
	public boolean isInfinite(final ItemStack stack, final ItemStack bow, final EntityPlayer player) {
		return true;
	}
}
