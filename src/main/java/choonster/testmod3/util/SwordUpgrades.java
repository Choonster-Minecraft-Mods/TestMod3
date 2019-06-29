package choonster.testmod3.util;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

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
		final ItemStack originalStack = new ItemStack(item);
		final ItemStack outputStack = originalStack.copy();

		// Modifiers provided by the Item are completely ignored as soon as any modifiers are added to the ItemStack,
		// so add the Item's modifiers to the output ItemStack manually
		for (final EquipmentSlotType slot : EquipmentSlotType.values()) {
			originalStack.getAttributeModifiers(slot)
					.entries()
					.forEach(entry -> outputStack.addAttributeModifier(entry.getKey(), entry.getValue(), slot));
		}

		// Create the attack damage modifier
		final AttributeModifier attackDamageModifier = new AttributeModifier(MODIFIER_UUID, MODIFIER_NAME, MODIFIER_AMOUNT, AttributeModifier.Operation.ADDITION);

		// Add it to the output ItemStack
		outputStack.addAttributeModifier(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), attackDamageModifier, EquipmentSlotType.MAINHAND);

		return outputStack;
	}
}
