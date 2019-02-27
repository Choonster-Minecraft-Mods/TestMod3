package choonster.testmod3.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModItems;
import net.minecraft.init.SoundEvents;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.IArmorMaterial;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

/**
 * A copy of {@link ArmorMaterial} for this mod's armour materials.
 *
 * @author Choonster
 */
public enum ModArmourMaterial implements IArmorMaterial {
	REPLACEMENT(
			"replacement", 15, new int[]{1, 4, 5, 2},
			12, SoundEvents.ITEM_ARMOR_EQUIP_CHAIN, 0,
			() -> Ingredient.fromItems(ModItems.ARROW)
	),

	;


	/**
	 * The base max damage for each armour slot.
	 */
	private static final int[] MAX_DAMAGE_ARRAY = new int[]{13, 15, 16, 11};

	/**
	 * The name of the armour material.
	 */
	private final String name;

	/**
	 * The maximum damage factor of the armour material, this is the item damage (how much it can absorb before it breaks).
	 */
	private final int maxDamageFactor;

	/**
	 * The damage reduction (each 1 point is half a shield on the GUI) of each piece of armour (helmet, plate, legs and boots).
	 */
	private final int[] damageReductionAmountArray;

	/**
	 * The enchantability factor of the armour material.
	 */
	private final int enchantability;

	/**
	 * The sound played when armour of the armour material is equipped.
	 */
	private final SoundEvent soundEvent;

	/**
	 * The armour toughness value of the armour material.
	 */
	private final float toughness;

	/**
	 * The repair material of the armour material.
	 */
	private final LazyLoadBase<Ingredient> repairMaterial;

	ModArmourMaterial(
			final String name, final int maxDamageFactor, final int[] damageReductionAmountArray,
			final int enchantability, final SoundEvent soundEvent, final float toughness,
			final Supplier<Ingredient> repairMaterial
	) {
		this.name = new ResourceLocation(TestMod3.MODID, name).toString();
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.repairMaterial = new LazyLoadBase<>(repairMaterial);
	}

	public int getDurability(final EntityEquipmentSlot slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * maxDamageFactor;
	}

	public int getDamageReductionAmount(final EntityEquipmentSlot slotIn) {
		return damageReductionAmountArray[slotIn.getIndex()];
	}

	public int getEnchantability() {
		return enchantability;
	}

	public SoundEvent getSoundEvent() {
		return soundEvent;
	}

	public Ingredient getRepairMaterial() {
		return repairMaterial.getValue();
	}

	@OnlyIn(Dist.CLIENT)
	public String getName() {
		return name;
	}

	public float getToughness() {
		return toughness;
	}
}
