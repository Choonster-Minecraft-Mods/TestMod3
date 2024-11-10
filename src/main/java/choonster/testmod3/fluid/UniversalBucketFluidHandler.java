package choonster.testmod3.fluid;

import choonster.testmod3.util.ModFluidUtil;
import choonster.testmod3.world.item.component.fluidhandler.FluidHandlerType;
import choonster.testmod3.world.item.component.fluidhandler.IFluidHandlerWithType;
import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;

import java.util.function.Function;

/**
 * An {@link IFluidHandlerWithType} implementation that only allows complete filling/draining and can only be filled with
 * fluids that have a bucket registered ({@link Fluid#getBucket()}).
 *
 * @author Choonster
 */
public class UniversalBucketFluidHandler extends ItemFluidTank {
	public static final MapCodec<UniversalBucketFluidHandler> BUCKET_CODEC = CODEC.xmap(
			itemFluidTank -> new UniversalBucketFluidHandler(itemFluidTank.getFluid(), itemFluidTank.getCapacity()),
			Function.identity()
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, UniversalBucketFluidHandler> BUCKET_STREAM_CODEC = STREAM_CODEC.map(
			itemFluidTank -> new UniversalBucketFluidHandler(itemFluidTank.getFluid(), itemFluidTank.getCapacity()),
			Function.identity()
	);

	public UniversalBucketFluidHandler(final int capacity) {
		super(capacity);
		setValidator(ModFluidUtil::hasBucket);
	}

	protected UniversalBucketFluidHandler(final FluidStack fluid, final int capacity) {
		super(fluid, capacity);
		setValidator(ModFluidUtil::hasBucket);
	}

	@Override
	public FluidHandlerType getType() {
		return FluidHandlerType.BUCKET;
	}

	@Override
	public int fill(final FluidStack resource, final FluidAction action) {
		// Simulate the fill to see how much would be filled
		final int amountToBeFilled = super.fill(resource, FluidAction.SIMULATE);

		// If it's not equal to the bucket's capacity, don't allow the fill
		if (amountToBeFilled != getCapacity()) {
			return 0;
		}

		// If this is a simulate request, return the result from the super method
		if (action.simulate()) {
			return amountToBeFilled;
		}

		// Otherwise, call the super method to execute the fill
		return super.fill(resource, FluidAction.EXECUTE);
	}

	@Override
	public FluidStack drain(final int maxDrain, final FluidAction action) {
		// Simulate the drain to see how much would be drained
		final FluidStack fluidToBeDrained = super.drain(maxDrain, FluidAction.SIMULATE);

		// If it's not equal to the bucket's capacity, don't allow the drain
		if (fluidToBeDrained.getAmount() != getCapacity()) {
			return FluidStack.EMPTY;
		}

		// If this is a simulate request, return the result from the super method
		if (action.simulate()) {
			return fluidToBeDrained;
		}

		// Otherwise, call the super method to execute the drain
		return super.drain(maxDrain, FluidAction.EXECUTE);
	}
}
