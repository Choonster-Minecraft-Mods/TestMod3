package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.loot.TestMod3BlockLoot;
import choonster.testmod3.data.loot.TestMod3EntityLoot;
import choonster.testmod3.data.loot.TestMod3GenericLoot;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Generates this mod's loot tables.
 *
 * @author Choonster
 */
public class TestMod3LootTableProvider extends LootTableProvider {
	private TestMod3LootTableProvider(final PackOutput output, final List<SubProviderEntry> subProviders) {
		super(output, Set.of(), subProviders);
	}

	public static TestMod3LootTableProvider create(final PackOutput output) {
		return new TestMod3LootTableProvider(output, ImmutableList.of(
				new SubProviderEntry(TestMod3BlockLoot::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(TestMod3EntityLoot::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(TestMod3GenericLoot::new, LootContextParamSets.ALL_PARAMS)
		));
	}

	@Override
	protected void validate(final Map<ResourceLocation, LootTable> map, final ValidationContext validationContext) {
		final var modLootTableIds = BuiltInLootTables
				.all()
				.stream()
				.filter(lootTable -> lootTable.getNamespace().equals(TestMod3.MODID))
				.collect(Collectors.toSet());

		for (final var id : Sets.difference(modLootTableIds, map.keySet())) {
			validationContext.reportProblem("Missing mod loot table: " + id);
		}

		map.forEach((id, lootTable) -> lootTable.validate(
				validationContext
						.setParams(lootTable.getParamSet())
						.enterElement("{" + id + "}", new LootDataId<>(LootDataType.TABLE, id))
		));
	}
}
