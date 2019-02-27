package choonster.testmod3.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * An item that applies Slowness II to the player while it's in their inventory.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2423521-detecting-item-in-inventory
 *
 * @author Choonster
 */
public class ItemHeavy extends Item {
	public ItemHeavy(final Item.Properties properties) {
		super(properties);
	}

	@Override
	public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
		super.inventoryTick(stack, world, entity, itemSlot, isSelected);

		if (entity instanceof EntityLivingBase) { // If the Entity is an instance of EntityLivingBase,
			((EntityLivingBase) entity).addPotionEffect(new PotionEffect(MobEffects.SLOWNESS, 10, 1)); // Apply Slowness II (amplifier = 1) for 10 ticks (0.5 seconds)
		}
	}
}
