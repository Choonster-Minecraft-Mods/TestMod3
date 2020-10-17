package choonster.testmod3.tileentity;

import choonster.testmod3.init.ModTileEntities;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.Set;

/**
 * A tank that holds 10 buckets of fluid and can have access enabled and disabled for each facing.
 *
 * @author Choonster
 */
public class RestrictedFluidTankTileEntity extends BaseFluidTankTileEntity {

	/**
	 * The facings that the {@link IFluidHandler} can be accessed from.
	 */
	private final Set<Direction> enabledFacings = EnumSet.allOf(Direction.class);

	public RestrictedFluidTankTileEntity() {
		super(ModTileEntities.FLUID_TANK_RESTRICTED);
	}

	/**
	 * Toggle the enabled state for the specified facing.
	 *
	 * @param facing The facing
	 * @return Is the facing now enabled?
	 */
	public boolean toggleFacing(final Direction facing) {
		if (enabledFacings.contains(facing)) {
			enabledFacings.remove(facing);
			return false;
		} else {
			enabledFacings.add(facing);
			return true;
		}
	}

	/**
	 * Is the specified facing enabled?
	 *
	 * @param facing The facing
	 * @return Is the facing enabled?
	 */
	public boolean isFacingEnabled(final @Nullable Direction facing) {
		return enabledFacings.contains(facing) || facing == null;
	}

	/**
	 * Get the enabled facings.
	 *
	 * @return The enabled facings.
	 */
	public Set<Direction> getEnabledFacings() {
		return enabledFacings;
	}

	@Override
	public void read(final BlockState state, final CompoundNBT tag) {
		super.read(state, tag);

		enabledFacings.clear();

		final int[] enabledFacingIndices = tag.getIntArray("EnabledFacings");
		for (final int index : enabledFacingIndices) {
			enabledFacings.add(Direction.byIndex(index));
		}
	}

	@Override
	public CompoundNBT write(final CompoundNBT tag) {
		final int[] enabledFacingIndices = enabledFacings.stream()
				.mapToInt(Direction::getIndex)
				.toArray();

		tag.putIntArray("EnabledFacings", enabledFacingIndices);

		return super.write(tag);
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isFacingEnabled(facing)) {
			return LazyOptional.empty();
		}

		return super.getCapability(capability, facing);
	}
}
