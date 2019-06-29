package choonster.testmod3.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.world.World;

/**
 * An armour item that constantly applies a {@link EffectInstance} when worn.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2820254-adding-potions-effects-permanently-while-armor-is
 *
 * @author Choonster
 */
public class PotionEffectArmourItem extends ArmorItem {
	private final EffectInstance potionEffect;

	public PotionEffectArmourItem(final IArmorMaterial material, final EquipmentSlotType equipmentSlot, final EffectInstance potionEffect, final Item.Properties properties) {
		super(material, equipmentSlot, properties);
		this.potionEffect = potionEffect;
	}

	@Override
	public void onArmorTick(final ItemStack stack, final World world, final PlayerEntity player) {
		if (!player.isPotionActive(potionEffect.getPotion())) { // If the Potion isn't currently active,
			player.addPotionEffect(new EffectInstance(potionEffect)); // Apply a copy of the PotionEffect to the player
		}
	}
}
