package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;

/**
 * An armour item that constantly applies a {@link PotionEffect} when worn.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2820254-adding-potions-effects-permanently-while-armor-is
 *
 * @author Choonster
 */
public class ItemArmourPotionEffect extends ItemArmor {
	private final PotionEffect potionEffect;

	public ItemArmourPotionEffect(final IArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final PotionEffect potionEffect, final Item.Properties properties) {
		super(material, equipmentSlot, properties);
		this.potionEffect = potionEffect;
	}

	@Override
	public void onArmorTick(final ItemStack stack, final World world, final EntityPlayer player) {
		if (!player.isPotionActive(potionEffect.getPotion())) { // If the Potion isn't currently active,
			player.addPotionEffect(new PotionEffect(potionEffect)); // Apply a copy of the PotionEffect to the player
		}
	}
}
