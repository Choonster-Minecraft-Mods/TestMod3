package choonster.testmod3.advancements.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.component.DataComponentGetter;
import net.minecraft.core.component.predicates.DataComponentPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * An item predicate that matches a container filled with the specified fluid.
 *
 * @author Choonster
 */
public record ItemFluidContainerPredicate(
		Optional<HolderSet<Fluid>> fluid,
		MinMaxBounds.Ints amount,
		Optional<NbtPredicate> nbt
) implements DataComponentPredicate {
	public static Codec<ItemFluidContainerPredicate> CODEC = RecordCodecBuilder.create(builder ->
			builder.group(

					RegistryCodecs.homogeneousList(Registries.FLUID)
							.optionalFieldOf("fluid")
							.forGetter(ItemFluidContainerPredicate::fluid),

					MinMaxBounds.Ints.CODEC
							.optionalFieldOf("amount", MinMaxBounds.Ints.ANY)
							.forGetter(ItemFluidContainerPredicate::amount),

					NbtPredicate.CODEC
							.optionalFieldOf("nbt")
							.forGetter(ItemFluidContainerPredicate::nbt)

			).apply(builder, ItemFluidContainerPredicate::new)
	);

	// TODO: Reimplement if/when Forge reimplements IFluidHandler - https://github.com/MinecraftForge/MinecraftForge/issues/10408
	@Override
	public boolean matches(final DataComponentGetter getter) {
		if (!(getter instanceof final ItemStack item)) {
			return false;
		}

		final var fluidContained = FluidUtil.getFluidContained(item);
		if (fluidContained.isEmpty()) {
			return false;
		}

		final var fluidStack = fluidContained.get();
		if (fluid.isPresent() && !fluid.get().contains(Holder.direct(fluidContained.get().getFluid()))) {
			return false;
		}

		if (!amount.matches(fluidStack.getAmount())) {
			return false;
		}

		return nbt.isEmpty() || nbt.get().matches(fluidStack.getTag());
	}

	public static class Builder {
		@Nullable
		private HolderSet<Fluid> fluid;
		private MinMaxBounds.Ints amount = MinMaxBounds.Ints.ANY;
		@Nullable
		private NbtPredicate nbt = null;

		private Builder() {
		}

		public static Builder create() {
			return new Builder();
		}

		@SuppressWarnings("deprecation")
		public Builder of(final Fluid... fluids) {
			fluid = HolderSet.direct(Fluid::builtInRegistryHolder, fluids);
			return this;
		}

		public Builder of(final HolderGetter<Fluid> getter, final TagKey<Fluid> tagKey) {
			fluid = getter.getOrThrow(tagKey);
			return this;
		}

		public Builder withAmount(final MinMaxBounds.Ints amount) {
			this.amount = amount;
			return this;
		}

		public Builder withNbt(final CompoundTag nbt) {
			this.nbt = new NbtPredicate(nbt);
			return this;
		}

		public ItemFluidContainerPredicate build() {
			return new ItemFluidContainerPredicate(Optional.ofNullable(fluid), amount, Optional.ofNullable(nbt));
		}
	}
}
