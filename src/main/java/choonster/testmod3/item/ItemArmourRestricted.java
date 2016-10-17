package choonster.testmod3.item;

import choonster.testmod3.Logger;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nullable;
import java.util.List;

/**
 * An armour item that will be deleted as soon as it's unequipped (i.e. in a player's inventory or on the ground).
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2595100-persistent-variables-for-armor
 *
 * @author Choonster
 */
public class ItemArmourRestricted extends ItemArmourTestMod3 {
	public ItemArmourRestricted(final ArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final String armourName) {
		super(material, equipmentSlot, armourName);
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
		if (!worldIn.isRemote) { // If this is the server,
			// Try to remove the item from the entity's inventories
			final EntityInventoryType successfulInventoryType = InventoryUtils.forEachEntityInventory(
					entity,
					inventory -> tryRemoveStack(inventory, itemSlot, stack),
					EntityInventoryType.MAIN, EntityInventoryType.HAND
			);

			if (successfulInventoryType != null) {
				Logger.info("Restricted armour deleted from slot %d of %s's %s inventory", itemSlot, entity.getName(), successfulInventoryType);
			}
		}
	}

	/**
	 * Try to remove the {@link ItemStack} from the specified inventory slot
	 *
	 * @param inventory The inventory
	 * @param slot      The inventory slot
	 * @param stack     The ItemStack to remove
	 * @return Was the ItemStack removed?
	 */
	private boolean tryRemoveStack(@Nullable final IItemHandler inventory, final int slot, final ItemStack stack) {
		if (inventory != null && inventory.getStackInSlot(slot) == stack && inventory.extractItem(slot, stack.stackSize, true) != null) {
			inventory.extractItem(slot, stack.stackSize, false); // Remove this item from their inventory
			return true;
		}

		return false;
	}

	/**
	 * Called by the default implementation of {@link EntityItem#onUpdate()}, allowing for cleaner
	 * control over the update of the item without having to write a subclass.
	 *
	 * @param entityItem The item entity
	 * @return Return true to skip any further update code.
	 */
	@Override
	public boolean onEntityItemUpdate(final EntityItem entityItem) {
		entityItem.setDead(); // Kill the item entity
		Logger.info("Restricted armour deleted from world at %s", entityItem.getPosition());
		return true; // Skip the rest of the update
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void addInformation(final ItemStack stack, final EntityPlayer playerIn, final List<String> tooltip, final boolean advanced) {
		tooltip.add(I18n.format("item.testmod3:armour_restricted.desc"));
	}
}
