package choonster.testmod3.world.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An armour item that constantly applies a {@link MobEffectInstance} when worn.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2820254-adding-potions-effects-permanently-while-armor-is
 *
 * @author Choonster
 */
public class PotionEffectArmourItem extends ArmorItem {
	private final MobEffectInstance mobEffectInstance;

	public PotionEffectArmourItem(final ArmorMaterial material, final ArmorItem.Type type, final MobEffectInstance mobEffectInstance, final Item.Properties properties) {
		super(material, type, properties);
		this.mobEffectInstance = mobEffectInstance;
	}

	@Override
	public void inventoryTick(final ItemStack stack, final Level level, final Entity entity, final int itemSlot, final boolean isSelected) {
		if (
				entity instanceof final LivingEntity livingEntity && // If the entity is living,
						!livingEntity.hasEffect(mobEffectInstance.getEffect()) && // The effect isn't currently active,
						livingEntity.getItemBySlot(getEquipmentSlot()) == stack // And the item is equipped as armour
		) {
			livingEntity.addEffect(new MobEffectInstance(mobEffectInstance)); // Apply a copy of the effect to the entity
		}
	}
}
