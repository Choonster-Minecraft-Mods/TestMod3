package choonster.testmod3.loot.functions;

import choonster.testmod3.init.ModLootFunctionTypes;
import choonster.testmod3.item.FluidStackItem;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.SetContents;
import net.minecraft.util.JSONUtils;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Stores the contents of a fluid tank in the ItemStack's NBT.
 * <p>
 * Adapted from {@link SetContents}.
 *
 * @author Choonster
 */
public class SetFluidTankContents extends LootFunction {
	private final List<LootEntry> lootEntries;

	public SetFluidTankContents(final ILootCondition[] conditions, final List<LootEntry> lootEntries) {
		super(conditions);
		this.lootEntries = lootEntries;
	}

	@Override
	protected ItemStack run(final ItemStack stack, final LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		}

		final NonNullList<ItemStack> itemStacks = NonNullList.create();

		lootEntries.forEach(lootEntry ->
				lootEntry.expand(context, lootGenerator ->
						lootGenerator.createItemStack(LootTable.createStackSplitter(itemStacks::add), context)
				)
		);

		stack.getCapability(CapabilityFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY, null)
				.ifPresent(fluidHandler ->
						itemStacks.stream()
								.filter(itemStack -> itemStack.getItem() instanceof FluidStackItem)
								.map(itemStack -> ((FluidStackItem) itemStack.getItem()).getFluidStack(itemStack))
								.forEach(fluidStack -> fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE))
				);

		return stack;
	}

	@Override
	public LootFunctionType getType() {
		return ModLootFunctionTypes.SET_FLUID_TANK_CONTENTS;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends LootFunction.Builder<Builder> {
		private final List<LootEntry> lootEntries = Lists.newArrayList();

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder addLootEntry(final LootEntry.Builder<?> lootEntryBuilder) {
			lootEntries.add(lootEntryBuilder.build());
			return this;
		}

		@Override
		public ILootFunction build() {
			return new SetFluidTankContents(getConditions(), lootEntries);
		}
	}

	public static class Serializer extends LootFunction.Serializer<SetFluidTankContents> {
		@Override
		public void serialize(final JsonObject object, final SetFluidTankContents function, final JsonSerializationContext serializationContext) {
			super.serialize(object, function, serializationContext);
			object.add("entries", serializationContext.serialize(function.lootEntries));
		}

		@Override
		public SetFluidTankContents deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final ILootCondition[] conditions) {
			final LootEntry[] lootEntries = JSONUtils.convertToObject(object, "entries", deserializationContext, LootEntry[].class);
			return new SetFluidTankContents(conditions, Arrays.asList(lootEntries));
		}
	}
}
