package choonster.testmod3.world.item;

import choonster.testmod3.init.ModDataComponents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraftforge.fluids.FluidStack;

/**
 * Dummy item to represent {@link FluidStack} drops in {@link LootTable}s.
 *
 * @author Choonster
 */
public class FluidStackItem extends Item {
	public FluidStackItem(final Properties properties) {
		super(properties);
	}

	public ItemStack withFluidStack(final FluidStack fluidStack) {
		final ItemStack itemStack = new ItemStack(this);

		itemStack.set(ModDataComponents.FLUID_STACK.get(), fluidStack);

		return itemStack;
	}

	public FluidStack getFluidStack(final ItemStack itemStack) {
		return itemStack.getOrDefault(ModDataComponents.FLUID_STACK.get(), FluidStack.EMPTY);
	}
}
