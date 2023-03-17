package choonster.testmod3.world.item;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

/**
 * An armour item that constantly applies a {@link MobEffectInstance} when worn.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2820254-adding-potions-effects-permanently-while-armor-is
 *
 * @author Choonster
 */
public class PotionEffectArmourItem extends ArmorItem {
	private final MobEffectInstance mobEffectInstance;

	public PotionEffectArmourItem(final ArmorMaterial material, final ArmorItem.Type type, final MobEffectInstance mobEffectInstance, final Item.Properties properties) {
		super(material, type, properties);
		this.mobEffectInstance = mobEffectInstance;
	}

	@Override
	public void onArmorTick(final ItemStack stack, final Level world, final Player player) {
		if (!player.hasEffect(mobEffectInstance.getEffect())) { // If the effect isn't currently active,
			player.addEffect(new MobEffectInstance(mobEffectInstance)); // Apply a copy of the effect to the player
		}
	}
}
