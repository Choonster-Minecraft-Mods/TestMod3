package choonster.testmod3.advancements.criterion;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.util.RegistryUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.advancements.critereon.NbtPredicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * An item predicate that matches a container filled with the specified fluid.
 *
 * @author Choonster
 */
public class FluidContainerItemPredicate extends ItemPredicate {
	public static final ResourceLocation TYPE = new ResourceLocation(TestMod3.MODID, "fluid_container");

	private final TagKey<Fluid> tag;
	private final Fluid fluid;
	private final MinMaxBounds.Ints amount;
	private final NbtPredicate nbt;

	public FluidContainerItemPredicate(@Nullable final TagKey<Fluid> tag, @Nullable final Fluid fluid, final MinMaxBounds.Ints amount, final NbtPredicate nbt) {
		this.tag = tag;
		this.fluid = fluid;
		this.amount = amount;
		this.nbt = nbt;
	}

	@Override
	public boolean matches(final ItemStack item) {
		final Optional<FluidStack> fluidContained = FluidUtil.getFluidContained(item);

		if (fluidContained.isEmpty()) {
			return false;
		}

		final FluidStack fluidStack = fluidContained.get();

		if (tag != null && !fluidStack.getFluid().defaultFluidState().is(tag)) {
			return false;
		}

		if (fluid != null && fluidStack.getFluid() != fluid) {
			return false;
		}

		if (!amount.matches(fluidStack.getAmount())) {
			return false;
		}

		return nbt.matches(fluidStack.getTag());
	}

	public static ItemPredicate deserialize(@Nullable final JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return ANY;
		}

		final JsonObject object = GsonHelper.convertToJsonObject(element, "item");

		final MinMaxBounds.Ints amount = MinMaxBounds.Ints.fromJson(object.get("amount"));

		final NbtPredicate nbt = NbtPredicate.fromJson(object.get("nbt"));

		Fluid fluid = null;
		if (object.has("fluid")) {
			fluid = ModJsonUtil.getFluid(object, "fluid");
		}

		TagKey<Fluid> tag = null;
		if (object.has("tag")) {
			final ResourceLocation tagName = new ResourceLocation(GsonHelper.getAsString(object, "tag"));
			tag = FluidTags.create(tagName);
		}

		return new FluidContainerItemPredicate(tag, fluid, amount, nbt);
	}

	@Override
	public JsonElement serializeToJson() {
		if (this == ANY) {
			return JsonNull.INSTANCE;
		}

		final JsonObject object = new JsonObject();

		object.addProperty("type", TYPE.toString());

		if (fluid != null) {
			final String registryName = RegistryUtil.getRequiredRegistryName(fluid).toString();
			object.addProperty("fluid", registryName);
		}

		object.add("amount", amount.serializeToJson());
		object.add("nbt", nbt.serializeToJson());

		return object;
	}

	public static class Builder {
		private Fluid fluid;
		private TagKey<Fluid> tag;
		private MinMaxBounds.Ints amount = MinMaxBounds.Ints.ANY;
		private NbtPredicate nbt = NbtPredicate.ANY;

		private Builder() {
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder fluid(final Fluid fluid) {
			this.fluid = fluid;
			return this;
		}

		public Builder tag(final TagKey<Fluid> tag) {
			this.tag = tag;
			return this;
		}

		public Builder amount(final MinMaxBounds.Ints amount) {
			this.amount = amount;
			return this;
		}

		public Builder nbt(final CompoundTag nbt) {
			this.nbt = new NbtPredicate(nbt);
			return this;
		}

		public FluidContainerItemPredicate build() {
			return new FluidContainerItemPredicate(tag, fluid, amount, nbt);
		}
	}
}
