package choonster.testmod3.world.item;

import choonster.testmod3.text.TestMod3Lang;
import choonster.testmod3.util.InventoryUtils;
import choonster.testmod3.util.InventoryUtils.EntityInventoryType;
import com.mojang.logging.LogUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;
import org.slf4j.Logger;

import org.jetbrains.annotations.Nullable;
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
	private static final Logger LOGGER = LogUtils.getLogger();

	public RestrictedArmourItem(final ArmorMaterial material, final EquipmentSlot equipmentSlot, final Item.Properties properties) {
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
	public void inventoryTick(final ItemStack stack, final Level world, final Entity entity, final int itemSlot, final boolean isSelected) {
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

	@Override
	public boolean onEntityItemUpdate(final ItemStack stack, final ItemEntity entity) {
		entity.discard(); // Discard the item entity
		LOGGER.info("Restricted armour deleted from world at {}", entity.blockPosition());
		return true; // Skip the rest of the update
	}

	@Override
	public void appendHoverText(final ItemStack stack, @Nullable final Level level, final List<Component> tooltip, final TooltipFlag flag) {
		tooltip.add(Component.translatable(TestMod3Lang.ITEM_DESC_ARMOUR_RESTRICTED.getTranslationKey()));
	}
}
