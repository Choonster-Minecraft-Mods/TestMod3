package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.collect.Sets;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Generates this mod's entity loot tables.
 *
 * @author Choonster
 */
public class TestMod3EntityLootTables extends EntityLootTables {
	private static final Field BUILDERS = ObfuscationReflectionHelper.findField(EntityLootTables.class, "field_218587_b" /* TODO: No MCP name yet */);

	@Override
	public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		registerLootTable(ModEntities.PLAYER_AVOIDING_CREEPER,
				LootTable.builder()
						.addLootPool(
								LootPool.builder()
										.rolls(ConstantRange.of(1))
										.addEntry(ItemLootEntry.builder(Items.GUNPOWDER)
												.acceptFunction(SetCount.func_215932_a(RandomValueRange.func_215837_a(0.0F, 2.0F)))
												.acceptFunction(LootingEnchantBonus.func_215915_a(RandomValueRange.func_215837_a(0.0F, 1.0F)))
										)
						)
						.addLootPool(
								LootPool.builder()
										.addEntry(TagLootEntry.func_216176_b(ItemTags.MUSIC_DISCS))
										.acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.create().func_217989_a(EntityTypeTags.SKELETONS)))
						)
		);

		final Map<ResourceLocation, LootTable.Builder> builders = getBuilders();
		final Set<ResourceLocation> encounteredLootTables = Sets.newHashSet();

		for (final EntityType<?> entityType : RegistryUtil.getModRegistryEntries(ForgeRegistries.ENTITIES)) {
			final ResourceLocation lootTable = entityType.getLootTable();

			if (entityType.getClassification() == EntityClassification.MISC) {
				if (lootTable != LootTables.EMPTY & builders.remove(lootTable) != null) {
					throw new IllegalStateException(String.format("Weird loot table '%s' for '%s', not a LivingEntity so should not have loot", lootTable, entityType.getRegistryName()));
				}
			} else if (lootTable != LootTables.EMPTY && encounteredLootTables.add(lootTable)) {
				final LootTable.Builder lootTableBuilder = builders.remove(lootTable);
				if (lootTableBuilder == null) {
					throw new IllegalStateException(String.format("Missing loot table '%s' for '%s'", lootTable, entityType.getRegistryName()));
				}

				consumer.accept(lootTable, lootTableBuilder);
			}
		}

		builders.forEach(consumer);
	}

	private void registerLootTable(final EntityType<?> entityType, final LootTable.Builder lootTableBuilder) {
		registerLootTable(entityType.getLootTable(), lootTableBuilder);
	}

	private void registerLootTable(final ResourceLocation lootTableName, final LootTable.Builder lootTableBuilder) {
		getBuilders().put(lootTableName, lootTableBuilder);
	}

	private Map<ResourceLocation, LootTable.Builder> getBuilders() {
		try {
			@SuppressWarnings("unchecked")
			final Map<ResourceLocation, LootTable.Builder> builders = (Map<ResourceLocation, LootTable.Builder>) BUILDERS.get(this);
			return builders;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get builders Map", e);
		}
	}
}
