package choonster.testmod3.item;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
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
public class ItemArmourPotionEffect extends ItemArmourTestMod3 {
	private final PotionEffect potionEffect;

	public ItemArmourPotionEffect(final ArmorMaterial material, final EntityEquipmentSlot equipmentSlot, final String armourName, final PotionEffect potionEffect) {
		super(material, equipmentSlot, armourName);
		this.potionEffect = potionEffect;
	}

	@Override
	public void onArmorTick(final World world, final EntityPlayer player, final ItemStack itemStack) {
		if (!player.isPotionActive(potionEffect.getPotion())) { // If the Potion isn't currently active,
			player.addPotionEffect(new PotionEffect(potionEffect)); // Apply a copy of the PotionEffect to the player
		}
	}
}
