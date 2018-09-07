package choonster.testmod3.item;

import choonster.testmod3.Logger;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import choonster.testmod3.util.ItemStackUtils;
import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static net.minecraftforge.common.util.Constants.NBT;

/**
 * An armour item that replaces your other armour when equipped and restores it when unequipped.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2595100-persistent-variables-for-armor
 *
 * @author Choonster
 */
public class ItemArmourReplacement extends ItemArmourTestMod3 {
	// NBT keys
	private static final String KEY_REPLACED_ARMOUR = "ReplacedArmour";
	private static final String KEY_SLOT = "Slot";
	private static final String KEY_STACK = "Stack";

	/**
	 * The items to replace the other armour with.
	 */
	private Set<ItemStack> replacementItems;

	public ItemArmourReplacement(final ArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final String armourName) {
		super(material, equipmentSlot, armourName);
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
	 * Save the entity's armour and replace it with the replacements defined for this item.
	 *
	 * @param stack  The ItemStack of this item
	 * @param entity The entity
	 */
	private void replaceArmour(final ItemStack stack, final EntityLivingBase entity) {
		final NBTTagCompound stackTagCompound = ItemStackUtils.getOrCreateTagCompound(stack);
		final NBTTagList replacedArmour = new NBTTagList();

		// Create a mutable copy of the replacements
		final Set<ItemStack> replacements = new HashSet<>(replacementItems);

		Constants.ARMOUR_SLOTS.stream() // For each armour type,
				.filter(equipmentSlot -> equipmentSlot != armorType)
				.forEach(equipmentSlot -> { // If it's not this item's armour type,
					final Optional<ItemStack> optionalReplacement = replacements.stream()
							.filter(replacementStack -> replacementStack.getItem().isValidArmor(replacementStack, equipmentSlot, entity))
							.findFirst();

					optionalReplacement.ifPresent(replacement -> { // If there's a replacement for this armour type,
						replacements.remove(replacement); // Don't use it for any other armour type

						// Create a compound tag to store the original and add it to the list of replaced armour
						final NBTTagCompound tagCompound = new NBTTagCompound();
						replacedArmour.appendTag(tagCompound);
						tagCompound.setByte(KEY_SLOT, (byte) equipmentSlot.getSlotIndex());

						// If the original item exists, add it to the compound tag
						final ItemStack original = entity.getItemStackFromSlot(equipmentSlot);
						if (!original.isEmpty()) {
							tagCompound.setTag(KEY_STACK, original.serializeNBT());
						}

						entity.setItemStackToSlot(equipmentSlot, replacement.copy()); // Equip a copy of the replacement
						Logger.info("Equipped replacement %s to %s, replacing %s", replacement, equipmentSlot, original);
					});
				});

		stackTagCompound.setTag(KEY_REPLACED_ARMOUR, replacedArmour); // Save the replaced armour to the ItemStack
	}

	/**
	 * Restore the entity's saved armour from this item's ItemStack NBT.
	 *
	 * @param stack  The ItemStack of this item
	 * @param entity The entity
	 */
	private void restoreArmour(final ItemStack stack, final EntityLivingBase entity) {
		final NBTTagCompound stackTagCompound = ItemStackUtils.getOrCreateTagCompound(stack);
		final NBTTagList replacedArmour = stackTagCompound.getTagList(KEY_REPLACED_ARMOUR, NBT.TAG_COMPOUND);

		for (int i = 0; i < replacedArmour.tagCount(); i++) { // For each saved armour item,
			final NBTTagCompound replacedTagCompound = replacedArmour.getCompoundTagAt(i);
			final ItemStack original = new ItemStack(replacedTagCompound.getCompoundTag(KEY_STACK)); // Load the original ItemStack from the NBT

			final EntityEquipmentSlot equipmentSlot = InventoryUtils.getEquipmentSlotFromIndex(replacedTagCompound.getByte(KEY_SLOT)); // Get the armour slot
			final ItemStack current = entity.getItemStackFromSlot(equipmentSlot);

			// Is the item currently in the slot one of the replacements defined for this item?
			final boolean isReplacement = replacementItems.stream().anyMatch(replacement -> ItemStack.areItemStacksEqual(replacement, current));

			if (original.isEmpty()) { // If the original item is empty,
				if (isReplacement) { // If the current item is a replacement,
					Logger.info("Original item for %s is null, clearing replacement", equipmentSlot);
					entity.setItemStackToSlot(equipmentSlot, ItemStack.EMPTY); // Delete it
				} else { // Else do nothing
					Logger.info("Original item for %s is null, leaving current item", equipmentSlot);
				}
			} else {
				Logger.info("Restoring original %s to %s, replacing %s", original, equipmentSlot, current);

				// If the current item isn't a replacement and the entity is a player, try to add it to their inventory or drop it on the ground
				if (!isReplacement && entity instanceof EntityPlayer) {
					ItemHandlerHelper.giveItemToPlayer((EntityPlayer) entity, current);
				}

				entity.setItemStackToSlot(equipmentSlot, original); // Equip the original item
			}
		}

		stackTagCompound.removeTag(KEY_REPLACED_ARMOUR);

		if (stackTagCompound.isEmpty()) {
			stack.setTagCompound(null);
		}
	}

	/**
	 * Restore the entity's saved armour if the ItemStack is in the specified inventory slot.
	 *
	 * @param inventory The inventory
	 * @param slot      The inventory slot
	 * @param stack     The ItemStack of this item
	 * @param entity    The entity
	 * @return Was the armour restored?
	 */
	private boolean tryRestoreArmour(final IItemHandler inventory, final int slot, final ItemStack stack, final EntityLivingBase entity) {
		if (slot < inventory.getSlots() && inventory.getStackInSlot(slot) == stack) {
			restoreArmour(stack, entity); // Restore the entity's armour
			return true;
		}

		return false;
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
	 * Called every tick while the item is in a player's inventory (including while worn).
	 *
	 * @param stack      The ItemStack of this item
	 * @param worldIn    The entity's world
	 * @param entity     The entity
	 * @param itemSlot   The slot containing this item
	 * @param isSelected Is the entity holding this item?
	 */
	@Override
	public void onUpdate(final ItemStack stack, final World worldIn, final Entity entity, final int itemSlot, final boolean isSelected) {
		// If this is the server, the entity is living and the entity's armour has been replaced,
		if (!worldIn.isRemote && entity instanceof EntityLivingBase && hasReplacedArmour(stack)) {

			// Try to restore the entity's armour
			final EntityInventoryType successfulInventoryType = InventoryUtils.forEachEntityInventory(
					entity,
					inventory -> tryRestoreArmour(inventory, itemSlot, stack, (EntityLivingBase) entity),
					EntityInventoryType.MAIN, EntityInventoryType.HAND
			);

			if (successfulInventoryType != null) {
				Logger.info("Restored saved armour for slot %d of %s's %s inventory", itemSlot, entity.getName(), successfulInventoryType);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, @Nullable final World world, final List<String> tooltip, final ITooltipFlag flag) {
		tooltip.add(I18n.format("item.testmod3:armour_replacement.equip.desc"));
		tooltip.add(I18n.format("item.testmod3:armour_replacement.unequip.desc"));
	}
}
