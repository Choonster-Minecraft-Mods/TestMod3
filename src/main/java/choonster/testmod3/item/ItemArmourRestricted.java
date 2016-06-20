package choonster.testmod3.item;

import choonster.testmod3.Logger;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

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
			// Get the entity's main inventory
			final IItemHandler mainInventory = entity.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, EnumFacing.UP);

			// If this item is in their main inventory and it can be extracted.
			if (mainInventory != null && mainInventory.getStackInSlot(itemSlot) == stack && mainInventory.extractItem(itemSlot, stack.stackSize, true) != null) {
				mainInventory.extractItem(itemSlot, stack.stackSize, false); // Remove this item from their inventory

				Logger.info("Restricted armour deleted from slot %d of %s's inventory", itemSlot, entity.getName());
			}
		}
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
		tooltip.add(I18n.format("item.testmod3:armourRestricted.desc"));
	}
}
