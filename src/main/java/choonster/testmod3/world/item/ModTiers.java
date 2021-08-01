package choonster.testmod3.world.item;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

/**
 * A copy of {@link Tiers} for this mod's item tiers.
 *
 * @author Choonster
 */
public enum ModTiers implements Tier {
	GLOWSTONE(
			1, 5, 0.5f, 1.0f, 10,
			() -> Ingredient.of(Items.GLOWSTONE_DUST)
	),

	;

	/**
	 * The level of material the tier can harvest (3 = DIAMOND, 2 = IRON, 1 = STONE, 0 = WOOD/GOLD)
	 */
	private final int harvestLevel;

	/**
	 * The number of uses the item tier allows.
	 */
	private final int maxUses;

	/**
	 * The strength of the item tier against blocks which it is effective against.
	 */
	private final float efficiency;

	/**
	 * The damage of the item tier versus entities.
	 */
	private final float attackDamage;

	/**
	 * The enchantability factor of the item tier.
	 */
	private final int enchantability;

	/**
	 * The repair material of the item tier.
	 */
	private final Supplier<Ingredient> repairMaterial;

	ModTiers(final int harvestLevel, final int maxUses, final float efficiency, final float attackDamage, final int enchantability, final Supplier<Ingredient> repairMaterial) {
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = Lazy.of(repairMaterial);
	}

	@Override
	public int getUses() {
		return maxUses;
	}

	@Override
	public float getSpeed() {
		return efficiency;
	}

	@Override
	public float getAttackDamageBonus() {
		return attackDamage;
	}

	@Override
	public int getLevel() {
		return harvestLevel;
	}

	@Override
	public int getEnchantmentValue() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairIngredient() {
		return repairMaterial.get();
	}
}
