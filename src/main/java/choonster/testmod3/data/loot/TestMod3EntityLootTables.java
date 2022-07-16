package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.loot.EntityLoot;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Generates this mod's entity loot tables.
 *
 * @author Choonster
 */
public class TestMod3EntityLootTables extends EntityLoot {
	@Override
	protected void addTables() {
		add(ModEntities.PLAYER_AVOIDING_CREEPER.get(),
				LootTable.lootTable()
						.withPool(
								LootPool.lootPool()
										.setRolls(ConstantValue.exactly(1))
										.add(LootItem.lootTableItem(Items.GUNPOWDER)
												.apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 2.0F)))
												.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0.0F, 1.0F)))
										)
						)
						.withPool(
								LootPool.lootPool()
										.add(TagEntry.expandTag(ItemTags.MUSIC_DISCS))
										.when(LootItemEntityPropertyCondition.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))
						)
		);
	}

	@Override
	protected Iterable<EntityType<?>> getKnownEntities() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.ENTITY_TYPES);
	}
}
