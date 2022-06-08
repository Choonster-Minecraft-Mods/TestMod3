package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Set;

/**
 * A tank that holds 10 buckets of fluid and can have access enabled and disabled for each facing.
 *
 * @author Choonster
 */
public class RestrictedFluidTankBlockEntity extends BaseFluidTankBlockEntity {

	/**
	 * The facings that the {@link IFluidHandler} can be accessed from.
	 */
	private final Set<Direction> enabledFacings = EnumSet.allOf(Direction.class);

	public RestrictedFluidTankBlockEntity(final BlockPos pos, final BlockState state) {
		super(ModBlockEntities.FLUID_TANK_RESTRICTED.get(), pos, state);
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
	public void load(final CompoundTag tag) {
		super.load(tag);

		enabledFacings.clear();

		final int[] enabledFacingIndices = tag.getIntArray("EnabledFacings");
		for (final int index : enabledFacingIndices) {
			enabledFacings.add(Direction.from3DDataValue(index));
		}
	}

	@Override
	protected void saveAdditional(final CompoundTag tag) {
		super.saveAdditional(tag);

		final int[] enabledFacingIndices = enabledFacings.stream()
				.mapToInt(Direction::get3DDataValue)
				.toArray();

		tag.putIntArray("EnabledFacings", enabledFacingIndices);
	}

	@Override
	public <T> LazyOptional<T> getCapability(final Capability<T> capability, @Nullable final Direction facing) {
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY && !isFacingEnabled(facing)) {
			return LazyOptional.empty();
		}

		return super.getCapability(capability, facing);
	}
}
