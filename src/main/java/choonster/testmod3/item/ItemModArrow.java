package choonster.testmod3.item;

import choonster.testmod3.entity.EntityModArrow;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * An arrow fired from this mod's bows.
 *
 * @author Choonster
 */
public class ItemModArrow extends ItemArrow {
	public ItemModArrow(String itemName) {
		ItemTestMod3.setItemName(this, itemName);
	}

	@Override
	public EntityArrow createArrow(World worldIn, ItemStack stack, EntityLivingBase shooter) {
		final EntityModArrow entityModArrow = new EntityModArrow(worldIn, shooter);
		entityModArrow.setPotionEffect(stack);
		return entityModArrow;
	}

	@Override
	public boolean isInfinite(ItemStack stack, ItemStack bow, EntityPlayer player) {
		return true;
	}
}
