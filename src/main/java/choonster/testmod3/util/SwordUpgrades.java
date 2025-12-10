package choonster.testmod3.util;

import choonster.testmod3.TestMod3;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SwordUpgrades {
	/**
	 * The ID of the attack damage modifier.
	 */
	private static final Identifier MODIFIER_ID = Identifier.fromNamespaceAndPath(TestMod3.MODID, "weapon_upgrade");

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
		final var attackDamageModifier = new AttributeModifier(MODIFIER_ID, MODIFIER_AMOUNT, AttributeModifier.Operation.ADD_VALUE);

		// Add it to the modifiers
		final var newModifiers = modifiers.withModifierAdded(Attributes.ATTACK_DAMAGE, attackDamageModifier, EquipmentSlotGroup.MAINHAND);

		// Update the ItemStack
		stack.set(DataComponents.ATTRIBUTE_MODIFIERS, newModifiers);

		return stack;
	}
}
