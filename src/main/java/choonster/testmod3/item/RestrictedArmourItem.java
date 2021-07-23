package choonster.testmod3.item;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.IItemHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class RestrictedArmourItem extends ArmorItem {
	private static final Logger LOGGER = LogManager.getLogger();

	public RestrictedArmourItem(final IArmorMaterial material, final EquipmentSlotType equipmentSlot, final Item.Properties properties) {
		super(material, equipmentSlot, properties);
	}

	/**
	 * Called every tick while the item is in a player's inventory (including while worn).
	 *
	 * @param stack      The ItemStack of this item
	 * @param world      The entity's world
	 * @param entity     The entity
	 * @param itemSlot   The slot containing this item
	 * @param isSelected Is the entity holding this item?
	 */
	@Override
	public void inventoryTick(final ItemStack stack, final World world, final Entity entity, final int itemSlot, final boolean isSelected) {
		if (!world.isClientSide) { // If this is the server,
			// Try to remove the item from the entity's inventories
			InventoryUtils.forEachEntityInventory(
					entity,
					inventory -> tryRemoveStack(inventory, itemSlot, stack),
					EntityInventoryType.MAIN, EntityInventoryType.HAND
			).ifPresent(successfulInventoryType ->
					LOGGER.info("Restricted armour deleted from slot {} of {}'s {} inventory", itemSlot, entity.getName(), successfulInventoryType)
			);
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
	private boolean tryRemoveStack(final IItemHandler inventory, final int slot, final ItemStack stack) {
		if (slot < inventory.getSlots() && inventory.getStackInSlot(slot) == stack && !inventory.extractItem(slot, stack.getCount(), true).isEmpty()) {
			inventory.extractItem(slot, stack.getCount(), false); // Remove this item from their inventory
			return true;
		}

		return false;
	}

	/**
	 * Called by the default implementation of {@link ItemEntity#tick()}, allowing for cleaner
	 * control over the update of the item without having to write a subclass.
	 *
	 * @param entity The item entity
	 * @return Return true to skip any further update code.
	 */
	@Override
	public boolean onEntityItemUpdate(final ItemStack stack, final ItemEntity entity) {
		entity.remove(); // Kill the item entity
		LOGGER.info("Restricted armour deleted from world at {}", entity.blockPosition());
		return true; // Skip the rest of the update
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final World world, final List<ITextComponent> tooltip, final ITooltipFlag flag) {
		tooltip.add(new TranslationTextComponent(TestMod3Lang.ITEM_DESC_ARMOUR_RESTRICTED.getTranslationKey()));
	}
}
