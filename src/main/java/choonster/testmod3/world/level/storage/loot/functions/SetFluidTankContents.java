package choonster.testmod3.world.level.storage.loot.functions;

import choonster.testmod3.init.ModLootFunctionTypes;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.world.item.FluidStackItem;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntries;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.functions.SetContainerContents;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.fluids.capability.IFluidHandler;

import java.util.List;

/**
 * Stores the contents of a fluid tank in the ItemStack's NBT.
 * <p>
 * Adapted from {@link SetContainerContents}.
 *
 * @author Choonster
 */
public class SetFluidTankContents extends LootItemConditionalFunction {
	public static final Codec<SetFluidTankContents> CODEC = RecordCodecBuilder.create(builder ->
			commonFields(builder).and(

					LootPoolEntries.CODEC
							.listOf()
							.fieldOf("entries")
							.forGetter(instance -> instance.lootEntries)

			).apply(builder, SetFluidTankContents::new)
	);

	private final List<LootPoolEntryContainer> lootEntries;

	public SetFluidTankContents(final List<LootItemCondition> conditions, final List<LootPoolEntryContainer> lootEntries) {
		super(conditions);
		this.lootEntries = lootEntries;
	}

	@Override
	protected ItemStack run(final ItemStack stack, final LootContext context) {
		if (stack.isEmpty()) {
			return stack;
		}

		final var itemStacks = NonNullList.<ItemStack>create();

		lootEntries.forEach(lootEntry ->
				lootEntry.expand(context, lootGenerator ->
						lootGenerator.createItemStack(LootTable.createStackSplitter(context.getLevel(), itemStacks::add), context)
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
}
