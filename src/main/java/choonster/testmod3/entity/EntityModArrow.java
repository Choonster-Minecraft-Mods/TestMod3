package choonster.testmod3.entity;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.init.ModItems;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

/**
 * An arrow entity that behaves like the vanilla arrow but renders with a different texture.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2577561-custom-arrow
 *
 * @author Choonster
 */
public class EntityModArrow extends EntityTippedArrow {
	public EntityModArrow(final World worldIn) {
		super(worldIn);
	}

	public EntityModArrow(final World worldIn, final double x, final double y, final double z) {
		super(worldIn, x, y, z);
	}

	public EntityModArrow(final World worldIn, final EntityLivingBase shooter) {
		super(worldIn, shooter);
	}

	@Override
	public EntityType<?> getType() {
		return ModEntities.MOD_ARROW;
	}

	@Override
	public void setPotionEffect(final ItemStack stack) {
		super.setPotionEffect(new ItemStack(Items.ARROW)); // Mod arrows can't have potion effects
	}

	@Override
	protected ItemStack getArrowStack() {
		return new ItemStack(ModItems.ARROW);
	}
}
