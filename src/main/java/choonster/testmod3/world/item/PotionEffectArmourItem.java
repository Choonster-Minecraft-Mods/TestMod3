package choonster.testmod3.world.item;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorMaterial;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.world.level.Level;

import java.util.Objects;

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

	public PotionEffectArmourItem(final ArmorMaterial material, final ArmorType type, final MobEffectInstance mobEffectInstance, final Item.Properties properties) {
		super(material, type, properties);
		this.mobEffectInstance = mobEffectInstance;
	}

	private EquipmentSlot getSlot(final ItemStack stack) {
		final var equippable = Objects.requireNonNull(
				stack.get(DataComponents.EQUIPPABLE),
				() -> "%s isn't equippable".formatted(stack)
		);

		return equippable.slot();
	}

	@Override
	public void inventoryTick(final ItemStack stack, final Level level, final Entity entity, final int itemSlot, final boolean isSelected) {
		if (
				entity instanceof final LivingEntity livingEntity && // If the entity is living,
						!livingEntity.hasEffect(mobEffectInstance.getEffect()) && // The effect isn't currently active,
						livingEntity.getItemBySlot(getSlot(stack)) == stack // And the item is equipped as armour
		) {
			livingEntity.addEffect(new MobEffectInstance(mobEffectInstance)); // Apply a copy of the effect to the entity
		}
	}
}
