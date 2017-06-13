package choonster.testmod3.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
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
public class TileEntityFluidTankRestricted extends TileEntityFluidTank {

	/**
	 * The facings that the {@link IFluidHandler} can be accessed from.
	 */
	private final Set<EnumFacing> enabledFacings = EnumSet.allOf(EnumFacing.class);

	/**
	 * Toggle the enabled state for the specified facing.
	 *
	 * @param facing The facing
	 * @return Is the facing now enabled?
	 */
	public boolean toggleFacing(final EnumFacing facing) {
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
	public boolean isFacingEnabled(final @Nullable EnumFacing facing) {
		return enabledFacings.contains(facing) || facing == null;
	}

	/**
	 * Get the enabled facings.
	 *
	 * @return The enabled facings.
	 */
	public Set<EnumFacing> getEnabledFacings() {
		return enabledFacings;
	}

	@Override
	public void readFromNBT(final NBTTagCompound tag) {
		super.readFromNBT(tag);

		enabledFacings.clear();

		final int[] enabledFacingIndices = tag.getIntArray("EnabledFacings");
		for (final int index : enabledFacingIndices) {
			enabledFacings.add(EnumFacing.getFront(index));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
		final int[] enabledFacingIndices = enabledFacings.stream()
				.mapToInt(EnumFacing::getIndex)
				.toArray();

		tag.setIntArray("EnabledFacings", enabledFacingIndices);

		return super.writeToNBT(tag);
	}

	@Override
	public boolean hasCapability(final Capability<?> capability, @Nullable final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			return isFacingEnabled(facing);
		}

		return super.hasCapability(capability, facing);
	}

	@Nullable
	@Override
	public <T> T getCapability(final Capability<T> capability, @Nullable final EnumFacing facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) {
			if (isFacingEnabled(facing)) {
				return CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY.cast(tank);
			}

			return null;
		}

		return super.getCapability(capability, facing);
	}
}
