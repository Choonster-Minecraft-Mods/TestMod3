package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.TypedEntityData;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.slf4j.Logger;

import java.util.function.Supplier;

/**
 * A global loot modifier that adds an ItemStack of the harvested block's item with the BlockEntity's NBT stored in the
 * "block_entity_data" component.
 *
 * @author Choonster
 */
public class BlockEntityNBTLootModifier extends LootModifier {
	private static final Logger LOGGER = LogUtils.getLogger();

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
	protected ObjectArrayList<ItemStack> doApply(
			final LootTable table,
			final ObjectArrayList<ItemStack> generatedLoot,
			final LootContext context
	) {
		final var tableKey = ResourceKey.create(Registries.LOOT_TABLE, context.getQueriedLootTableId());
		final var tablePathElement = new ProblemReporter.RootElementPathElement(tableKey);

		try (final var problems = new ProblemReporter.ScopedCollector(tablePathElement, LOGGER)) {
			final var state = context.getOptionalParameter(LootContextParams.BLOCK_STATE);
			final var blockEntity = context.getOptionalParameter(LootContextParams.BLOCK_ENTITY);

			if (state != null && blockEntity != null) {
				// Write the BlockEntity to NBT
				final var blockEntityOutput = TagValueOutput.createWithContext(problems, context.getLevel().registryAccess());

				blockEntity.saveWithId(blockEntityOutput);

				// Store the BlockEntity data in the ItemStack
				final var stack = new ItemStack(state.getBlock());
				stack.set(
						DataComponents.BLOCK_ENTITY_DATA,
						TypedEntityData.of(blockEntity.getType(), blockEntityOutput.buildResult())
				);

				generatedLoot.add(stack);
			}

			return generatedLoot;
		}
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
