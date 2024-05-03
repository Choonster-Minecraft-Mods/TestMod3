package choonster.testmod3.world.level.storage.loot.modifiers;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

/**
 * A global loot modifier that generates loot from another loot table.
 *
 * @author Choonster
 */
public class LootTableLootModifier extends LootModifier {
	public static final Supplier<MapCodec<LootTableLootModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.mapCodec(inst ->
					codecStart(inst)
							.and(
									ResourceKey.codec(Registries.LOOT_TABLE)
											.fieldOf("loot_table")
											.forGetter(m -> m.lootTable)
							)
							.apply(inst, LootTableLootModifier::new)
			)
	);

	private final ResourceKey<LootTable> lootTable;

	public LootTableLootModifier(final LootItemCondition[] conditions, final ResourceKey<LootTable> lootTable) {
		super(conditions);
		this.lootTable = lootTable;
	}

	@SuppressWarnings("deprecation")
	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(final ObjectArrayList<ItemStack> generatedLoot, final LootContext context) {
		final var lootTable = context.getResolver()
				.get(Registries.LOOT_TABLE, this.lootTable)
				.map(Holder::value)
				.orElse(LootTable.EMPTY);

		// Generate additional loot without applying loot modifiers, otherwise each modifier would run multiple times
		// for the same loot generation.
		lootTable.getRandomItemsRaw(context, generatedLoot::add);

		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC.get();
	}
}
