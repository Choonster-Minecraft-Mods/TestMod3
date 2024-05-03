package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;

import java.util.function.Supplier;

/**
 * A global loot modifier that adds an ItemStack of the harvested block's item with the BlockEntity's NBT stored in the
 * "BlockEntityTag" tag.
 *
 * @author Choonster
 */
public class BlockEntityNBTLootModifier extends LootModifier {
	public static final Supplier<MapCodec<BlockEntityNBTLootModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.mapCodec(inst ->
					codecStart(inst)
							.apply(inst, BlockEntityNBTLootModifier::new)
			)
	);

	public BlockEntityNBTLootModifier(final LootItemCondition[] conditionsIn) {
		super(conditionsIn);
	}

	@Override
	protected ObjectArrayList<ItemStack> doApply(final ObjectArrayList<ItemStack> generatedLoot, final LootContext context) {
		final var state = context.getParamOrNull(LootContextParams.BLOCK_STATE);
		final var blockEntity = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);

		if (state != null && blockEntity != null) {
			// Write the BlockEntity to NBT
			final var blockEntityTag = blockEntity.saveWithId(context.getLevel().registryAccess());

			// Store the BlockEntity data in the ItemStack
			final var stack = new ItemStack(state.getBlock());
			stack.set(DataComponents.BLOCK_ENTITY_DATA, CustomData.of(blockEntityTag));

			generatedLoot.add(stack);
		}

		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
