package choonster.testmod3.util;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

import java.util.UUID;

public class SwordUpgrades {
	/**
	 * The ID of the attack damage modifier. This can be used to look up the modifier in an {@link ItemStack}'s NBT.
	 */
	private static final UUID MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");

	/**
	 * The name of the attack damage modifier.
	 */
	private static final String MODIFIER_NAME = "Weapon Upgrade";

	/**
	 * The amount of attack damage provided by the modifier.
	 */
	private static final float MODIFIER_AMOUNT = 30.0f;

	/**
	 * Returns an {@link ItemStack} of the {@link Item} with +30 attack damage.
	 *
	 * @param item The item
	 * @return An ItemStack of the Item with the attack damage modifier added to it
	 */
	public static ItemStack upgradeSword(final Item item) {
		// Create an ItemStack of the Item
		final var stack = new ItemStack(item);

		// Get the modifiers
		final var modifiers = stack.getOrDefault(DataComponents.ATTRIBUTE_MODIFIERS, ItemAttributeModifiers.EMPTY);

		// Create the attack damage modifier
		final var attackDamageModifier = new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, MODIFIER_AMOUNT, AttributeModifier.Operation.ADD_VALUE);

		// Add it to the modifiers
		final var newModifiers = modifiers.withModifierAdded(Attributes.ATTACK_DAMAGE, attackDamageModifier, EquipmentSlotGroup.MAINHAND);

		// Update the ItemStack
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, newModifiers);

		return stack;
	}
}
