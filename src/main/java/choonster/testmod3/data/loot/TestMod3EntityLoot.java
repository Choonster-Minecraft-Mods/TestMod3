package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.EnchantedCountIncreaseFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.stream.Stream;

/**
 * Generates this mod's entity loot tables.
 *
 * @author Choonster
 */
public class TestMod3EntityLoot extends EntityLootSubProvider {
	public TestMod3EntityLoot(final HolderLookup.Provider registries) {
		super(FeatureFlags.REGISTRY.allFlags(), registries);
	}

	@Override
	public void generate() {
		final var entities = registries.lookupOrThrow(Registries.ENTITY_TYPE);

		add(ModEntities.PLAYER_AVOIDING_CREEPER.get(),
				LootTable.lootTable()
						.withPool(
								LootPool.lootPool()
										.setRolls(ConstantValue.exactly(1.0F))
										.add(LootItem.lootTableItem(Items.GUNPOWDER)
												.apply(
														SetItemCountFunction.setCount(
																UniformGenerator.between(0.0F, 2.0F)
														)
												)
												.apply(
														EnchantedCountIncreaseFunction.lootingMultiplier(
																registries,
																UniformGenerator.between(0.0F, 1.0F)
														)
												)
										)
						)
						.withPool(
								LootPool.lootPool()
										.add(TagEntry.expandTag(ItemTags.CREEPER_DROP_MUSIC_DISCS))
										.when(
												LootItemEntityPropertyCondition.hasProperties(
														LootContext.EntityTarget.ATTACKER,
														EntityPredicate.Builder.entity().of(
																entities, EntityTypeTags.SKELETONS
														)
												)
										)
						)
		);
	}

	@Override
	protected Stream<EntityType<?>> getKnownEntityTypes() {
		return RegistryUtil.getModRegistryEntriesStream(ForgeRegistries.ENTITY_TYPES);
	}
}
