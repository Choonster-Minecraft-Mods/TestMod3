package choonster.testmod3.world.item;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.Level;

/**
 * An item that applies Slowness II to the player while it's in their inventory.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2423521-detecting-item-in-inventory
 *
 * @author Choonster
 */
public class HeavyItem extends Item {
	public HeavyItem(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(final ItemStack stack, final Level world, final Entity entity, final int itemSlot, final boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);

		if (entity instanceof LivingEntity) { // If the Entity is an instance of EntityLivingBase,
			((LivingEntity) entity).addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 10, 1)); // Apply Slowness II (amplifier = 1) for 10 ticks (0.5 seconds)
		}
	}
}
