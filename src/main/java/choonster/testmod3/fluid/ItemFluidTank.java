package choonster.testmod3.fluid;

import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.world.item.component.fluidhandler.FluidHandlerType;
import choonster.testmod3.world.item.component.fluidhandler.IFluidHandlerWithType;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import org.jetbrains.annotations.Nullable;

/**
 * An implementation of {@link IFluidHandlerWithType} that stores its contents as a {@link FluidStack} in memory rather than
 * storing them in the vanilla {@link ItemStack} NBT.
 *
 * @author Choonster
 */
public class ItemFluidTank extends FluidTank implements IFluidHandlerWithType {
	public static final MapCodec<ItemFluidTank> CODEC = RecordCodecBuilder.mapCodec(builder ->
			builder.group(
					FluidStack.CODEC
							.fieldOf("fluid")
							.forGetter(ItemFluidTank::getFluid),

					Codec.INT
							.fieldOf("capacity")
							.forGetter(ItemFluidTank::getCapacity)
			).apply(builder, ItemFluidTank::new)
	);

	public static final StreamCodec<RegistryFriendlyByteBuf, ItemFluidTank> STREAM_CODEC = StreamCodec.composite(
			VanillaCodecs.FLUID_STACK_OPTIONAL_STREAM_CODEC,
			ItemFluidTank::getFluid,
			ByteBufCodecs.VAR_INT,
			ItemFluidTank::getCapacity,
			ItemFluidTank::new
	);

	public ItemFluidTank(final int capacity) {
		super(capacity);
	}

	protected ItemFluidTank(final FluidStack fluid, final int capacity) {
		super(capacity);
		setFluid(fluid);
	}

	@Override
	public FluidHandlerType getType() {
		return FluidHandlerType.TANK;
	}

	@Override
	public boolean equals(@Nullable final Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}

		final FluidTank that = ((FluidTank) obj);

		return getFluid().equals(that.getFluid());
	}

	@Override
	public int hashCode() {
		return fluid.hashCode();
	}
}
