package choonster.testmod3.item;

import net.minecraft.init.Items;
import net.minecraft.item.IItemTier;
import net.minecraft.item.ItemTier;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.LazyLoadBase;

import java.util.function.Supplier;

/**
 * A copy of {@link ItemTier} for this mod's item tiers.
 *
 * @author Choonster
 */
public enum ModItemTier implements IItemTier {
	GLOWSTONE(
			1, 5, 0.5f, 1.0f, 10,
			() -> Ingredient.fromItems(Items.GLOWSTONE_DUST)
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
	private final LazyLoadBase<Ingredient> repairMaterial;

	ModItemTier(final int harvestLevel, final int maxUses, final float efficiency, final float attackDamage, final int enchantability, final Supplier<Ingredient> repairMaterial) {
		this.harvestLevel = harvestLevel;
		this.maxUses = maxUses;
		this.efficiency = efficiency;
		this.attackDamage = attackDamage;
		this.enchantability = enchantability;
		this.repairMaterial = new LazyLoadBase<>(repairMaterial);
	}

	@Override
	public int getMaxUses() {
		return maxUses;
	}

	@Override
	public float getEfficiency() {
		return efficiency;
	}

	@Override
	public float getAttackDamage() {
		return attackDamage;
	}

	@Override
	public int getHarvestLevel() {
		return harvestLevel;
	}

	@Override
	public int getEnchantability() {
		return enchantability;
	}

	@Override
	public Ingredient getRepairMaterial() {
		return repairMaterial.getValue();
	}
}
