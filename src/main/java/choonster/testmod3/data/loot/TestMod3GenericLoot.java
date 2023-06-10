package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModItems;
import choonster.testmod3.init.ModLootTables;
import net.minecraft.advancements.critereon.EntityFlagsPredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.functions.SmeltItemFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

/**
 * Generates this mod's generic loot tables.
 *
 * @author Choonster
 */
public class TestMod3GenericLoot implements LootTableSubProvider {
	@Override
	public void generate(final BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(
				ModLootTables.LOOT_TABLE_TEST,
				LootTable.lootTable()
						.withPool(
								LootPool.lootPool()
										.setRolls(UniformGenerator.between(1, 4))
										.add(
												LootItem.lootTableItem(Items.PORKCHOP)
														.setWeight(1)
														.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 3)))
														.apply(
																SmeltItemFunction.smelted()
																		.when(
																				LootItemEntityPropertyCondition.hasProperties(
																						LootContext.EntityTarget.THIS,
																						EntityPredicate.Builder.entity()
																								.flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build())
																				)
																		)
														)
														.apply(LootingEnchantFunction.lootingMultiplier(UniformGenerator.between(0, 1)))
										)
										.add(
												LootItem.lootTableItem(ModItems.ARROW.get())
														.setWeight(2)
														.apply(SetItemCountFunction.setCount(UniformGenerator.between(1, 64)))
										)
										.add(
												LootItem.lootTableItem(Items.WOODEN_AXE)
														.setWeight(1)
														.apply(SetItemDamageFunction.setDamage(UniformGenerator.between(0.28f, 0.28f)))
										)
						)
		);
	}
}
