package choonster.testmod3.item;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
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

	public ItemModArrow(String itemName, BiFunction<World, EntityLivingBase, EntityTippedArrow> entityFactory) {
		ItemTestMod3.setItemName(this, itemName);
		this.entityFactory = entityFactory;
	}

	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		final EntityTippedArrow entityModArrow = entityFactory.apply(worldIn, shooter);
		entityModArrow.setPotionEffect(stack);
		return entityModArrow;
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
		return true;
	}
}
