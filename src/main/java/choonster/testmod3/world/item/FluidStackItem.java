package choonster.testmod3.world.item;

import net.minecraft.nbt.CompoundTag;
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
	private static final String FLUID_STACK = "FluidStack";

	public FluidStackItem(final Properties properties) {
		super(properties);
	}

	public ItemStack withFluidStack(final FluidStack fluidStack) {
		final ItemStack itemStack = new ItemStack(this);

		itemStack.getOrCreateTag().put(FLUID_STACK, fluidStack.writeToNBT(new CompoundTag()));

		return itemStack;
	}

	public FluidStack getFluidStack(final ItemStack itemStack) {
		final CompoundTag stackTag = itemStack.getOrCreateTag();

		if (stackTag.contains(FLUID_STACK)) {
			return FluidStack.loadFluidStackFromNBT(stackTag.getCompound(FLUID_STACK));
		}

		return FluidStack.EMPTY;
	}
}
