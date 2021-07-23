package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModItems;
import choonster.testmod3.init.ModLootTables;
import net.minecraft.advancements.criterion.EntityFlagsPredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.EntityHasProperty;
import net.minecraft.loot.functions.LootingEnchantBonus;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.loot.functions.SetDamage;
import net.minecraft.loot.functions.Smelt;
import net.minecraft.util.ResourceLocation;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * Generates this mod's generic loot tables.
 *
 * @author Choonster
 */
public class TestMod3GenericLootTables implements Consumer<BiConsumer<ResourceLocation, LootTable.Builder>> {
	@Override
	public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		consumer.accept(
				ModLootTables.LOOT_TABLE_TEST,
				LootTable.lootTable()
						.withPool(
								LootPool.lootPool()
										.name("main")
										.setRolls(new RandomValueRange(1, 4))
										.add(
												ItemLootEntry.lootTableItem(Items.PORKCHOP)
														.setWeight(1)
														.apply(SetCount.setCount(new RandomValueRange(1, 3)))
														.apply(
																Smelt.smelted()
																		.when(
																				EntityHasProperty.hasProperties(
																						LootContext.EntityTarget.THIS,
																						EntityPredicate.Builder.entity()
																								.flags(EntityFlagsPredicate.Builder.flags().setOnFire(true).build())
																				)
																		)
														)
														.apply(LootingEnchantBonus.lootingMultiplier(new RandomValueRange(0, 1)))
										)
										.add(
												ItemLootEntry.lootTableItem(ModItems.ARROW.get())
														.setWeight(2)
														.apply(SetCount.setCount(new RandomValueRange(1, 64)))
										)
										.add(
												ItemLootEntry.lootTableItem(Items.WOODEN_AXE)
														.setWeight(1)
														.apply(SetDamage.setDamage(new RandomValueRange(0.28f, 0.28f)))
										)
						)
		);
	}
}
