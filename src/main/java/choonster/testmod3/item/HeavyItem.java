package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

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
	public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);

		if (entity instanceof LivingEntity) { // If the Entity is an instance of EntityLivingBase,
			((LivingEntity) entity).addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN, 10, 1)); // Apply Slowness II (amplifier = 1) for 10 ticks (0.5 seconds)
		}
	}
}
