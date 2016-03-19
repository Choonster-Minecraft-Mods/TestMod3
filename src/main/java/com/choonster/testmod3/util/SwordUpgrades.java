package com.choonster.testmod3.util;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.UUID;

public class SwordUpgrades {
	// The ID to use for the attack damage modifier. This can be used to look up the modifier in an ItemStack's NBT.
	public final static UUID MODIFIER_UUID = UUID.fromString("294093da-54f0-4c1b-9dbb-13b77534a84c");

	// Returns an ItemStack of the ItemSword with +30 attack damage
	public static ItemStack upgradeSword(ItemSword item) {
		// Using the ItemStack AttributeModifiers NBT completely replaces the ItemSword modifiers,
		// so we need to manually add the sword's damage to the total
		float swordDamage = 4 + item.getDamageVsEntity();
		AttributeModifier attackModifier = new AttributeModifier(MODIFIER_UUID, "Weapon Upgrade", 30 + swordDamage, 0);
		NBTTagCompound modifierNBT = writeAttributeModifierToNBT(SharedMonsterAttributes.ATTACK_DAMAGE, attackModifier);

		// Create the NBT structure needed by ItemStack#getAttributeModifiers
		NBTTagCompound stackTagCompound = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		list.appendTag(modifierNBT);
		stackTagCompound.setTag("AttributeModifiers", list);

		// Create an ItemStack of the Item
		ItemStack stack = new ItemStack(item);

		// Set the stack's NBT to the modifier structure
		stack.setTagCompound(stackTagCompound);

		return stack;
	}

	// Adapted from SharedMonsterAttributes. Also adds AttributeName tag required by ItemStack#getAttributeModifiers
	private static NBTTagCompound writeAttributeModifierToNBT(IAttribute attribute, AttributeModifier modifier) {
		NBTTagCompound nbttagcompound = new NBTTagCompound();
		nbttagcompound.setString("AttributeName", attribute.getAttributeUnlocalizedName());
		nbttagcompound.setString("Name", modifier.getName());
		nbttagcompound.setDouble("Amount", modifier.getAmount());
		nbttagcompound.setInteger("Operation", modifier.getOperation());
		nbttagcompound.setLong("UUIDMost", modifier.getID().getMostSignificantBits());
		nbttagcompound.setLong("UUIDLeast", modifier.getID().getLeastSignificantBits());
		return nbttagcompound;
	}
}
