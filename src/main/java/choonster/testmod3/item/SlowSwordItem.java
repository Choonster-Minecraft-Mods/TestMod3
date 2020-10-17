package choonster.testmod3.item;

import com.google.common.collect.Multimap;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.IItemTier;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

/**
 * A sword that's 1.5 times slower than and does twice the damage of vanilla swords.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2679129-how-do-i-change-the-attack-cooldown-of-a-weapon
 *
 * @author Choonster
 */
public class SlowSwordItem extends SwordItem {
	public SlowSwordItem(final IItemTier tier, final Item.Properties properties) {
		super(tier, 3, -2.4f, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlotType slot, final ItemStack stack) {
		final Multimap<Attribute, AttributeModifier> modifiers = super.getAttributeModifiers(slot, stack);

		if (slot == EquipmentSlotType.MAINHAND) {
			replaceModifier(modifiers, Attributes.ATTACK_DAMAGE, ATTACK_DAMAGE_MODIFIER, 2);
			replaceModifier(modifiers, Attributes.ATTACK_SPEED, ATTACK_SPEED_MODIFIER, 1.5);
		}

		return modifiers;
	}

	/**
	 * Replace a modifier in the {@link Multimap} with a copy that's had {@code multiplier} applied to its value.
	 *
	 * @param modifierMultimap The Multimap
	 * @param attribute        The attribute being modified
	 * @param id               The ID of the modifier
	 * @param multiplier       The multiplier to apply
	 */
	private void replaceModifier(final Multimap<Attribute, AttributeModifier> modifierMultimap, final Attribute attribute, final UUID id, final double multiplier) {
		// Get the modifiers for the specified attribute
		final Collection<AttributeModifier> modifiers = modifierMultimap.get(attribute);

		// Find the modifier with the specified ID, if any
		final Optional<AttributeModifier> modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getID().equals(id)).findFirst();

		modifierOptional.ifPresent(modifier -> { // If it exists,
			modifiers.remove(modifier); // Remove it
			modifiers.add(new AttributeModifier(modifier.getID(), modifier.getName(), modifier.getAmount() * multiplier, modifier.getOperation())); // Add the new modifier
		});
	}
}
