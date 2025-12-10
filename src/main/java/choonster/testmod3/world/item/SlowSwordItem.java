package choonster.testmod3.world.item;

import net.minecraft.core.Holder;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A sword that's 1.5 times slower than and does twice the damage of vanilla swords.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2679129-how-do-i-change-the-attack-cooldown-of-a-weapon
 *
 * @author Choonster
 */
public class SlowSwordItem extends Item {
	private static final Method CREATE_SWORD_ATTRIBUTES = ObfuscationReflectionHelper.findMethod(
			ToolMaterial.class,
			"createSwordAttributes",
			float.class,
			float.class
	);

	private static final int BASE_ATTACK_DAMAGE = 3;
	private static final float ATTACK_SPEED = -2.4f;

	public SlowSwordItem(final ToolMaterial toolMaterial, final Item.Properties properties) {
		super(
				replaceAttributes(
						toolMaterial,
						toolMaterial.applySwordProperties(properties, BASE_ATTACK_DAMAGE, ATTACK_SPEED)
				)
		);

		replaceAttributes(toolMaterial, properties);
	}

	private static Item.Properties replaceAttributes(final ToolMaterial toolMaterial, final Item.Properties properties) {
		final ItemAttributeModifiers baseAttributes;
		try {
			baseAttributes = (ItemAttributeModifiers) CREATE_SWORD_ATTRIBUTES.invoke(toolMaterial, BASE_ATTACK_DAMAGE, ATTACK_SPEED);
		} catch (final IllegalAccessException | InvocationTargetException e) {
			throw new RuntimeException("Failed to create sword attributes", e);
		}

		final var builder = ItemAttributeModifiers.builder();

		copyModifier(baseAttributes, builder, Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE_ID, EquipmentSlot.MAINHAND, 2);
		copyModifier(baseAttributes, builder, Attributes.ATTACK_SPEED, BASE_ATTACK_SPEED_ID, EquipmentSlot.MAINHAND, 1.5);

		final var newAttributes = builder.build();

		return properties.attributes(newAttributes);
	}

	/**
	 * Copy a modifier from the {@link ItemAttributeModifiers} to the {@link ItemAttributeModifiers.Builder} with {@code multiplier} applied to its value.
	 *
	 * @param baseAttributes The base attributes to copy from
	 * @param builder        The builder to copy to
	 * @param attribute      The attribute being modified
	 * @param id             The ID of the modifier
	 * @param slot           The equipment slot to copy the modifier for
	 * @param multiplier     The multiplier to apply
	 */
	private static void copyModifier(
			final ItemAttributeModifiers baseAttributes,
			final ItemAttributeModifiers.Builder builder,
			final Holder<Attribute> attribute,
			final Identifier id,
			final EquipmentSlot slot,
			final double multiplier
	) {
		// Find the modifier with the specified ID, if any
		final var entryOptional = baseAttributes.modifiers()
				.stream()
				.filter(entry -> entry.attribute().get() == attribute.get())
				.filter(entry -> entry.modifier().id().equals(id))
				.filter(entry -> entry.slot().test(slot))
				.findFirst();

		entryOptional.ifPresent(entry -> { // If it exists,
			final var modifier = entry.modifier();

			// Add the new modifier
			builder.add(
					entry.attribute(),
					new AttributeModifier(modifier.id(), modifier.amount() * multiplier, modifier.operation()),
					entry.slot()
			);
		});
	}
}
