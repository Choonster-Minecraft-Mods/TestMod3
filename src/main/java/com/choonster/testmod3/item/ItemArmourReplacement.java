package com.choonster.testmod3.item;

import com.choonster.testmod3.Logger;
import com.choonster.testmod3.util.Constants;
import com.choonster.testmod3.util.ItemStackUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.minecraftforge.common.util.Constants.NBT;

/**
 * An armour item that replaces your other armour when equipped and restores it when unequipped.
 *
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2595100-persistent-variables-for-armor
 *
 * @author Choonster
 */
public class ItemArmourReplacement extends ItemArmourTestMod3 {
	// NBT keys
	private static final String KEY_REPLACED_ARMOUR = "ReplacedArmour";
	private static final String KEY_SLOT = "Slot";

	/**
	 * The items to replace the other armour with.
	 */
	private Set<ItemStack> replacementItems;

	public ItemArmourReplacement(final ArmorMaterial material, final int armorType, final String armourName) {
		super(material, armorType, armourName);
	}

	/**
	 * Get the items to replace the other armour with.
	 *
	 * @return The items to replace the other armour with
	 */
	public Set<ItemStack> getReplacementItems() {
		return replacementItems;
	}

	/**
	 * Set the items to replace the other armour with.
	 *
	 * @param replacements The items to replace the other armour with
	 */
	public void setReplacementItems(final ItemStack... replacements) {
		if (replacements.length > 3) {
			throw new IllegalArgumentException("Must supply at most 3 replacement items");
		}

		replacementItems = ImmutableSet.copyOf(replacements);
	}

	/**
	 * Has this item replaced the other armour?
	 *
	 * @param stack The ItemStack of this item
	 * @return Has this item replaced the other armour?
	 */
	public boolean hasReplacedArmour(final ItemStack stack) {
		return stack.hasTagCompound() && stack.getTagCompound().hasKey(KEY_REPLACED_ARMOUR, NBT.TAG_LIST);
	}

	/**
	 * Convert the specified {@link #armorType} to a slot number as used by {@link Entity#setCurrentItemOrArmor} and {@link EntityLivingBase#getEquipmentInSlot}.
	 *
	 * @param armorType The armour type
	 * @return The slot number
	 */
	private static int armourTypeToSlot(final int armorType) {
		return 4 - armorType;
	}

	/**
	 * Save the player's armour and replace it with the replacements defined for this item.
	 *
	 * @param stack  The ItemStack of this item
	 * @param player The player
	 */
	private void replaceArmour(final ItemStack stack, final EntityPlayer player) {
		final NBTTagCompound stackTagCompound = ItemStackUtils.getOrCreateTagCompound(stack);
		final NBTTagList replacedArmour = new NBTTagList();

		// Create a mutable copy of the replacements
		final Set<ItemStack> replacements = new HashSet<>(replacementItems);

		// For each armour type,
		for (int armorType = Constants.ARMOUR_TYPE_HEAD; armorType <= Constants.ARMOUR_TYPE_FEET; armorType++) {
			if (armorType != this.armorType) { // If it's not this item's armour type,
				final int constantArmorType = armorType;
				Optional<ItemStack> optionalReplacement = replacements.stream()
						.filter(replacementStack -> replacementStack.getItem().isValidArmor(replacementStack, constantArmorType, player))
						.findFirst();

				if (optionalReplacement.isPresent()) { // If there's a replacement for this armour type,
					final ItemStack replacement = optionalReplacement.get(); // Get it
					replacements.remove(replacement); // Don't use it for any other armour type

					final int slot = armourTypeToSlot(armorType); // Get the slot number

					final ItemStack original = player.getEquipmentInSlot(slot);
					final NBTTagCompound tagCompound = new NBTTagCompound();
					tagCompound.setByte(KEY_SLOT, (byte) slot);

					if (original != null) { // If the player is wearing something in this slot,
						original.writeToNBT(tagCompound); // Write it to NBT
					}

					replacedArmour.appendTag(tagCompound); // Add it to the list of replaced armour

					player.setCurrentItemOrArmor(slot, replacement.copy()); // Equip a copy of the replacement
					Logger.info("Equipped replacement %s to slot %d, replacing %s", replacement, slot, original);
				}
			}
		}

		stackTagCompound.setTag(KEY_REPLACED_ARMOUR, replacedArmour); // Save the replaced armour to the ItemStack
	}

	/**
	 * Restore the player's saved armour from this item's ItemStack NBT.
	 *
	 * @param stack  The ItemStack of this item
	 * @param player The player
	 */
	private void restoreArmour(final ItemStack stack, final EntityPlayer player) {
		final NBTTagCompound stackTagCompound = ItemStackUtils.getOrCreateTagCompound(stack);
		final NBTTagList replacedArmour = stackTagCompound.getTagList(KEY_REPLACED_ARMOUR, NBT.TAG_COMPOUND);

		for (int i = 0; i < replacedArmour.tagCount(); i++) { // For each saved armour item,
			final NBTTagCompound replacedTagCompound = replacedArmour.getCompoundTagAt(i);
			final ItemStack original = ItemStack.loadItemStackFromNBT(replacedTagCompound); // Load the original ItemStack from the NBT

			final int slot = replacedTagCompound.getByte(KEY_SLOT); // Get the armour slot
			final ItemStack current = player.getEquipmentInSlot(slot);

			// Is the item currently in the slot one of the replacements defined for this item?
			final boolean isReplacement = replacementItems.stream().anyMatch(replacement -> ItemStack.areItemStacksEqual(replacement, current));

			if (original == null) { // If the original item is null,
				if (isReplacement) { // If the current item is a replacement,
					Logger.info("Original item for slot %d is null, clearing replacement", slot);
					player.setCurrentItemOrArmor(slot, null); // Delete it
				} else { // Else do nothing
					Logger.info("Original item for slot %d is null, leaving current item", slot);
				}
			} else {
				Logger.info("Restoring original %s to slot %d, replacing %s", original, slot, current);

				if (!isReplacement && !player.inventory.addItemStackToInventory(current)) { // If the current item isn't a replacement, try to add it to the player's inventory
					player.dropPlayerItemWithRandomChoice(current, false); // Else drop it on the ground
				}

				player.setCurrentItemOrArmor(slot, original); // Equip the original item
			}
		}

		stackTagCompound.removeTag(KEY_REPLACED_ARMOUR);
	}

	/**
	 * Called every tick while the item is worn by a player.
	 *
	 * @param world     The player's world
	 * @param player    The player
	 * @param itemStack The ItemStack of this item
	 */
	@Override
	public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {
		if (!world.isRemote && !hasReplacedArmour(itemStack)) { // If this is the server and the player's armour hasn't been replaced,
			replaceArmour(itemStack, player); // Replace the player's armour
			player.inventoryContainer.detectAndSendChanges(); // Sync the player's inventory with the client
		}
	}

	/**
	 * Called every tick while the item is in a player's inventory (not while worn).
	 *
	 * @param stack      The ItemStack of this item
	 * @param worldIn    The entity's world
	 * @param entity     The entity
	 * @param itemSlot   The slot containing this item
	 * @param isSelected Is the entity holding this item?
	 */
	@Override
	public void onUpdate(final ItemStack stack, final World worldIn, final Entity entity, final int itemSlot, final boolean isSelected) {
		// If this is the server, the entity is a player and the player's armour has been replaced,
		if (!worldIn.isRemote && entity instanceof EntityPlayer && hasReplacedArmour(stack)) {
			EntityPlayer player = (EntityPlayer) entity;
			restoreArmour(stack, player); // Restore the player's armour
			player.inventoryContainer.detectAndSendChanges(); // Sync the player's inventory with the client
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
		tooltip.add(StatCollector.translateToLocal("item.testmod3:armourReplacement.equip.desc"));
		tooltip.add(StatCollector.translateToLocal("item.testmod3:armourReplacement.unequip.desc"));
	}
}
