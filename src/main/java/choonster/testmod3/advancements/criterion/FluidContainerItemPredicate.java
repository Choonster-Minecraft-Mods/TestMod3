package choonster.testmod3.advancements.criterion;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.ModJsonUtil;
import choonster.testmod3.util.RegistryUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.advancements.criterion.NBTPredicate;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tags.ITag;
import net.minecraft.tags.TagCollectionManager;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
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

	private final ITag<Fluid> tag;
	private final Fluid fluid;
	private final MinMaxBounds.IntBound amount;
	private final NBTPredicate nbt;

	public FluidContainerItemPredicate(@Nullable final ITag<Fluid> tag, @Nullable final Fluid fluid, final MinMaxBounds.IntBound amount, final NBTPredicate nbt) {
		this.tag = tag;
		this.fluid = fluid;
		this.amount = amount;
		this.nbt = nbt;
	}

	@Override
	public boolean test(final ItemStack item) {
		final Optional<FluidStack> fluidContained = FluidUtil.getFluidContained(item);

		if (!fluidContained.isPresent()) {
			return false;
		}

		final FluidStack fluidStack = fluidContained.get();

		if (tag != null && !fluidStack.getFluid().isIn(tag)) {
			return false;
		}

		if (fluid != null && fluidStack.getFluid() != fluid) {
			return false;
		}

		if (!amount.test(fluidStack.getAmount())) {
			return false;
		}

		return nbt.test(fluidStack.getTag());
	}

	public static ItemPredicate deserialize(@Nullable final JsonElement element) {
		if (element == null || element.isJsonNull()) {
			return ANY;
		}

		final JsonObject object = JSONUtils.getJsonObject(element, "item");

		final MinMaxBounds.IntBound amount = MinMaxBounds.IntBound.fromJson(object.get("amount"));

		final NBTPredicate nbt = NBTPredicate.deserialize(object.get("nbt"));

		Fluid fluid = null;
		if (object.has("fluid")) {
			fluid = ModJsonUtil.getFluid(object, "fluid");
		}

		ITag<Fluid> tag = null;
		if (object.has("tag")) {
			final ResourceLocation tagName = new ResourceLocation(JSONUtils.getString(object, "tag"));
			tag = TagCollectionManager.getManager().getFluidTags().get(tagName);
			if (tag == null) {
				throw new JsonSyntaxException("Unknown fluid tag '" + tagName + "'");
			}
		}

		return new FluidContainerItemPredicate(tag, fluid, amount, nbt);
	}

	@Override
	public JsonElement serialize() {
		if (this == ANY) {
			return JsonNull.INSTANCE;
		}

		final JsonObject object = new JsonObject();

		object.addProperty("type", TYPE.toString());

		if (fluid != null) {
			final String registryName = RegistryUtil.getRequiredRegistryName(fluid).toString();
			object.addProperty("fluid", registryName);
		}

		object.add("amount", amount.serialize());
		object.add("nbt", nbt.serialize());

		return object;
	}

	public static class Builder {
		private Fluid fluid;
		private ITag<Fluid> tag;
		private MinMaxBounds.IntBound amount = MinMaxBounds.IntBound.UNBOUNDED;
		private NBTPredicate nbt = NBTPredicate.ANY;

		private Builder() {
		}

		public static Builder create() {
			return new Builder();
		}

		public Builder fluid(final Fluid fluid) {
			this.fluid = fluid;
			return this;
		}

		public Builder tag(final ITag<Fluid> tag) {
			this.tag = tag;
			return this;
		}

		public Builder amount(final MinMaxBounds.IntBound amount) {
			this.amount = amount;
			return this;
		}

		public Builder nbt(final CompoundNBT nbt) {
			this.nbt = new NBTPredicate(nbt);
			return this;
		}

		public FluidContainerItemPredicate build() {
			return new FluidContainerItemPredicate(tag, fluid, amount, nbt);
		}
	}
}
