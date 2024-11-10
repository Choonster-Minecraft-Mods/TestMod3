package choonster.testmod3.world.item;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraftforge.common.ForgeHooks;

/**
 * An axe that loses durability when used in crafting recipes
 *
 * @author Choonster
 */
public class CuttingAxeItem extends AxeItem {
	public CuttingAxeItem(final ToolMaterial toolMaterial, final float attackDamage, final float attackSpeed, final Properties p_40524_) {
		super(toolMaterial, attackDamage, attackSpeed, p_40524_);
	}

	@Override
	public boolean hasCraftingRemainingItem(final ItemStack stack) {
		return true;
	}

	@Override
	public ItemStack getCraftingRemainingItem(final ItemStack itemStack) {
		final var remainingItem = itemStack.copy();
		final var craftingPlayer = ForgeHooks.getCraftingPlayer();

		// If we have a crafting player available, use hurtAndBreak to process enchantments and stats
		if (craftingPlayer != null && craftingPlayer.level() instanceof final ServerLevel serverLevel) {
			remainingItem.hurtAndBreak(
					1,
					serverLevel,
					craftingPlayer instanceof final ServerPlayer serverPlayer ? serverPlayer : null,
					item -> {
					}
			);
		} else { // Otherwise increase damage directly
			final var damage = remainingItem.getDamageValue() + 1;
			remainingItem.setDamageValue(damage);
			if (damage >= remainingItem.getMaxDamage()) {
				remainingItem.shrink(1);
			}
		}

		return remainingItem;
	}
}
