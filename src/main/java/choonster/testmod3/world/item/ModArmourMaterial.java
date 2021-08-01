package choonster.testmod3.world.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

/**
 * A copy of {@link ArmorMaterial} for this mod's armour materials.
 *
 * @author Choonster
 */
public enum ModArmourMaterial implements ArmorMaterial {
	REPLACEMENT(
			"replacement", 15, new int[]{1, 4, 5, 2},
			12, SoundEvents.ARMOR_EQUIP_CHAIN, 0,
			0, () -> Ingredient.of(ModItems.ARROW.get())
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
	 * The percentage of knockback resistance provided by armor of the material.
	 */
	private final float knockbackResistance;

	/**
	 * The repair material of the armour material.
	 */
	private final Supplier<Ingredient> repairMaterial;

	ModArmourMaterial(
			final String name, final int maxDamageFactor, final int[] damageReductionAmountArray,
			final int enchantability, final SoundEvent soundEvent, final float toughness,
			final float knockbackResistance, final Supplier<Ingredient> repairMaterial
	) {
		this.name = new ResourceLocation(TestMod3.MODID, name).toString();
		this.maxDamageFactor = maxDamageFactor;
		this.damageReductionAmountArray = damageReductionAmountArray;
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairMaterial = Lazy.of(repairMaterial);
	}

	@Override
	public int getDurabilityForSlot(final EquipmentSlot slotIn) {
		return MAX_DAMAGE_ARRAY[slotIn.getIndex()] * maxDamageFactor;
	}

	@Override
	public int getDefenseForSlot(final EquipmentSlot slotIn) {
		return damageReductionAmountArray[slotIn.getIndex()];
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public SoundEvent getEquipSound() {
		return soundEvent;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getToughness() {
		return toughness;
	}

	@Override
	public float getKnockbackResistance() {
		return knockbackResistance;
	}
}
