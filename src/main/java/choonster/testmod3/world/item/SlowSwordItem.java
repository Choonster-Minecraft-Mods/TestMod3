package choonster.testmod3.world.item;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
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
	private static final Field NAME = ObfuscationReflectionHelper.findField(AttributeModifier.class, /* name */ "f_303575_");

	public SlowSwordItem(final Tier tier, final Item.Properties properties) {
		super(tier, 3, -2.4f, properties);
	}

	@Override
	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(final EquipmentSlot slot, final ItemStack stack) {
		final var modifiers = ArrayListMultimap.create(super.getAttributeModifiers(slot, stack));

		if (slot == EquipmentSlot.MAINHAND) {
			replaceModifier(modifiers, Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE_UUID, 2);
			replaceModifier(modifiers, Attributes.ATTACK_SPEED, BASE_ATTACK_SPEED_UUID, 1.5);
		}

		return ImmutableMultimap.copyOf(modifiers);
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
		final var modifiers = modifierMultimap.get(attribute);

		// Find the modifier with the specified ID, if any
		final var modifierOptional = modifiers.stream().filter(attributeModifier -> attributeModifier.getId().equals(id)).findFirst();

		modifierOptional.ifPresent(modifier -> { // If it exists,
			modifiers.remove(modifier); // Remove it
			modifiers.add(new AttributeModifier(modifier.getId(), getName(modifier), modifier.getAmount() * multiplier, modifier.getOperation())); // Add the new modifier
		});
	}

	private String getName(final AttributeModifier modifier) {
		try {
			return (String) NAME.get(modifier);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get name of attribute modifier", e);
		}
	}
}
