package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Registers this mod's {@link ArmorMaterial ArmorMaterials}.
 *
 * @author Choonster
 */
public class ModArmorMaterials {

	private static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(Registries.ARMOR_MATERIAL, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<ArmorMaterial> REPLACEMENT = ARMOR_MATERIALS.register("replacement",
			() -> armorMaterial("replacement",
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
			)
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		ARMOR_MATERIALS.register(modEventBus);

		isInitialised = true;
	}

	private static ArmorMaterial armorMaterial(
			final String name,
			final EnumMap<ArmorItem.Type, Integer> defense,
			final int enchantmentValue,
			final Holder<SoundEvent> equipSound,
			final float toughness,
			final float knockbackResistance,
			final Supplier<Ingredient> repairIngredient
	) {
		final var layers = List.of(new ArmorMaterial.Layer(ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, name)));

		return armorMaterial(defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, layers);
	}

	private static ArmorMaterial armorMaterial(
			final EnumMap<ArmorItem.Type, Integer> defense,
			final int enchantmentValue,
			final Holder<SoundEvent> equipSound,
			final float toughness,
			final float knockbackResistance,
			final Supplier<Ingredient> repairIngredient,
			final List<ArmorMaterial.Layer> layers
	) {
		final var allDefenseValues = new EnumMap<ArmorItem.Type, Integer>(ArmorItem.Type.class);

		for (final var type : ArmorItem.Type.values()) {
			allDefenseValues.put(type, defense.get(type));
		}

		return new ArmorMaterial(allDefenseValues, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance);
	}
}
