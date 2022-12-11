package choonster.testmod3.world.level.storage.loot.functions;

import choonster.testmod3.init.ModLootFunctionTypes;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.world.item.FluidStackItem;
import com.google.common.collect.Lists;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.core.NonNullList;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.Arrays;
import java.util.List;

/**
 * Stores the contents of a fluid tank in the ItemStack's NBT.
 * <p>
 * Adapted from {@link SetContainerContents}.
 *
 * @author Choonster
 */
public class SetFluidTankContents extends LootItemConditionalFunction {
	private final List<LootPoolEntryContainer> lootEntries;

	public SetFluidTankContents(final LootItemCondition[] conditions, final List<LootPoolEntryContainer> lootEntries) {
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
						lootGenerator.createItemStack(LootTable.createStackSplitter(context, itemStacks::add), context)
				)
		);

		final var fluidHandler = stack
				.getCapability(ForgeCapabilities.FLUID_HANDLER_ITEM, null)
				.orElseThrow(CapabilityNotPresentException::new);

		itemStacks.stream()
				.filter(itemStack -> itemStack.getItem() instanceof FluidStackItem)
				.map(itemStack -> ((FluidStackItem) itemStack.getItem()).getFluidStack(itemStack))
				.forEach(fluidStack -> fluidHandler.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE));

		return stack;
	}

	@Override
	public LootItemFunctionType getType() {
		return ModLootFunctionTypes.SET_FLUID_TANK_CONTENTS.get();
	}

	public static Builder builder() {
		return new Builder();
	}

	public static class Builder extends LootItemConditionalFunction.Builder<Builder> {
		private final List<LootPoolEntryContainer> lootEntries = Lists.newArrayList();

		@Override
		protected Builder getThis() {
			return this;
		}

		public Builder addLootEntry(final LootPoolEntryContainer.Builder<?> lootEntryBuilder) {
			lootEntries.add(lootEntryBuilder.build());
			return this;
		}

		@Override
		public LootItemFunction build() {
			return new SetFluidTankContents(getConditions(), lootEntries);
		}
	}

	public static class Serializer extends LootItemConditionalFunction.Serializer<SetFluidTankContents> {
		@Override
		public void serialize(final JsonObject object, final SetFluidTankContents function, final JsonSerializationContext serializationContext) {
			super.serialize(object, function, serializationContext);
			object.add("entries", serializationContext.serialize(function.lootEntries));
		}

		@Override
		public SetFluidTankContents deserialize(final JsonObject object, final JsonDeserializationContext deserializationContext, final LootItemCondition[] conditions) {
			final LootPoolEntryContainer[] lootEntries = GsonHelper.getAsObject(object, "entries", deserializationContext, LootPoolEntryContainer[].class);
			return new SetFluidTankContents(conditions, Arrays.asList(lootEntries));
		}
	}
}
