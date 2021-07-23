package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Generates this mod's entity loot tables.
 *
 * @author Choonster
 */
public class TestMod3EntityLootTables extends EntityLootTables {
	@Override
	protected void addTables() {
		add(ModEntities.PLAYER_AVOIDING_CREEPER.get(),
				LootTable.lootTable()
						.withPool(
								LootPool.lootPool()
										.setRolls(ConstantRange.exactly(1))
										.add(ItemLootEntry.lootTableItem(Items.GUNPOWDER)
												.apply(SetCount.setCount(RandomValueRange.between(0.0F, 2.0F)))
												.apply(LootingEnchantBonus.lootingMultiplier(RandomValueRange.between(0.0F, 1.0F)))
										)
						)
						.withPool(
								LootPool.lootPool()
										.add(TagLootEntry.expandTag(ItemTags.MUSIC_DISCS))
										.when(EntityHasProperty.hasProperties(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.entity().of(EntityTypeTags.SKELETONS)))
						)
		);
	}

	@Override
	protected Iterable<EntityType<?>> getKnownEntities() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.ENTITIES);
	}
}
