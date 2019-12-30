package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModEntities;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.data.loot.EntityLootTables;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Items;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.EntityHasProperty;
import net.minecraft.world.storage.loot.functions.LootingEnchantBonus;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Generates this mod's entity loot tables.
 *
 * @author Choonster
 */
public class TestMod3EntityLootTables extends EntityLootTables {
	@Override
	protected void addTables() {
		registerLootTable(ModEntities.PLAYER_AVOIDING_CREEPER,
				LootTable.builder()
						.addLootPool(
								LootPool.builder()
										.rolls(ConstantRange.of(1))
										.addEntry(ItemLootEntry.builder(Items.GUNPOWDER)
												.acceptFunction(SetCount.builder(RandomValueRange.of(0.0F, 2.0F)))
												.acceptFunction(LootingEnchantBonus.builder(RandomValueRange.of(0.0F, 1.0F)))
										)
						)
						.addLootPool(
								LootPool.builder()
										.addEntry(TagLootEntry.func_216176_b(ItemTags.MUSIC_DISCS))
										.acceptCondition(EntityHasProperty.builder(LootContext.EntityTarget.KILLER, EntityPredicate.Builder.create().type(EntityTypeTags.SKELETONS)))
						)
		);
	}

	@Override
	protected Iterable<EntityType<?>> getKnownEntities() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.ENTITIES);
	}
}
