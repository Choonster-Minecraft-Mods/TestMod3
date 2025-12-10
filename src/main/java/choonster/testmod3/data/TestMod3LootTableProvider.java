package choonster.testmod3.data;

import choonster.testmod3.data.loot.TestMod3BlockLoot;
import choonster.testmod3.data.loot.TestMod3EntityLoot;
import choonster.testmod3.data.loot.TestMod3GenericLoot;
import choonster.testmod3.init.ModLootTables;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * Generates this mod's loot tables.
 *
 * @author Choonster
 */
public class TestMod3LootTableProvider extends LootTableProvider {
	private TestMod3LootTableProvider(final PackOutput output, final List<SubProviderEntry> subProviders, final CompletableFuture<HolderLookup.Provider> registries) {
		super(output, Set.of(), subProviders, registries);
	}

	public static TestMod3LootTableProvider create(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
		return new TestMod3LootTableProvider(output, ImmutableList.of(
				new SubProviderEntry(TestMod3BlockLoot::new, LootContextParamSets.BLOCK),
				new SubProviderEntry(TestMod3EntityLoot::new, LootContextParamSets.ENTITY),
				new SubProviderEntry(TestMod3GenericLoot::new, LootContextParamSets.ALL_PARAMS)
		), registries);
	}

	@Override
	protected void validate(final Registry<LootTable> registry, final ValidationContext validationContext, final ProblemReporter problemReporter) {
		final var modLootTableIds = ModLootTables.all();

		for (final var id : Sets.difference(modLootTableIds, registry.registryKeySet())) {
			validationContext.reportProblem(new MissingModTableProblem(id));
		}

		registry.listElements().forEach((lootTable) -> lootTable.value().validate(
				validationContext
						.setContextKeySet(lootTable.value().getParamSet())
						.enterElement(new ProblemReporter.RootElementPathElement(lootTable.key()), lootTable.key())
		));
	}

	public record MissingModTableProblem(ResourceKey<LootTable> id) implements ProblemReporter.Problem {
		@Override
		public String description() {
			return "Missing mod table: " + id.identifier();
		}
	}
}
