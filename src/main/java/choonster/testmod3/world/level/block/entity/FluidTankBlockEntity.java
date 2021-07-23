package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidAttributes;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * A tank that holds 10 buckets of fluid.
 *
 * @author Choonster
 */
public class FluidTankBlockEntity extends BaseFluidTankBlockEntity {
	public static final int CAPACITY = 10 * FluidAttributes.BUCKET_VOLUME;

	public FluidTankBlockEntity(final BlockPos pos, final BlockState state) {
		super(ModBlockEntities.FLUID_TANK.get(), pos, state);
		tank = new FluidTank(CAPACITY);
	}
}
