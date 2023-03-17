package choonster.testmod3.world.item;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModItems;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A copy of {@link ArmorMaterial} for this mod's armour materials.
 *
 * @author Choonster
 */
public enum ModArmourMaterial implements ArmorMaterial {
	REPLACEMENT(
			"replacement",
			15,
			Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
				map.put(ArmorItem.Type.BOOTS, 1);
				map.put(ArmorItem.Type.LEGGINGS, 4);
				map.put(ArmorItem.Type.CHESTPLATE, 5);
				map.put(ArmorItem.Type.HELMET, 2);
			}),
			12,
			SoundEvents.ARMOR_EQUIP_CHAIN,
			0,
			0,
			() -> Ingredient.of(ModItems.ARROW.get())
	),

	;


	/**
	 * The base durability for each armour type.
	 */
	private static final Map<ArmorItem.Type, Integer> HEALTH_FUNCTION_FOR_TYPE = ImmutableMap.copyOf(Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
		map.put(ArmorItem.Type.BOOTS, 13);
		map.put(ArmorItem.Type.LEGGINGS, 15);
		map.put(ArmorItem.Type.CHESTPLATE, 16);
		map.put(ArmorItem.Type.HELMET, 11);
	}));

	/**
	 * The name of the armour material.
	 */
	private final String name;

	/**
	 * The multiplier to apply to the base durability of each armour type.
	 */
	private final int durabilityMultiplier;

	/**
	 * The damage reduction (each 1 point is half a shield on the GUI) of each piece of armour (helmet, plate, legs and boots).
	 */
	private final Map<ArmorItem.Type, Integer> protectionFunctionForType;

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
			final String name, final int durabilityMultiplier, final EnumMap<ArmorItem.Type, Integer> protectionFunctionForType,
			final int enchantability, final SoundEvent soundEvent, final float toughness,
			final float knockbackResistance, final Supplier<Ingredient> repairMaterial
	) {
		this.name = new ResourceLocation(TestMod3.MODID, name).toString();
		this.durabilityMultiplier = durabilityMultiplier;
		this.protectionFunctionForType = ImmutableMap.copyOf(protectionFunctionForType);
		this.enchantability = enchantability;
		this.soundEvent = soundEvent;
		this.toughness = toughness;
		this.knockbackResistance = knockbackResistance;
		this.repairMaterial = Lazy.of(repairMaterial);
	}

	@Override
	public int getDurabilityForType(final ArmorItem.Type type) {
		return HEALTH_FUNCTION_FOR_TYPE.get(type) * durabilityMultiplier;
	}

	@Override
	public int getDefenseForType(final ArmorItem.Type type) {
		return protectionFunctionForType.get(type);
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
