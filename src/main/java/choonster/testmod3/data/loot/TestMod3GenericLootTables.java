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
				LootTable.builder()
						.addLootPool(
								LootPool.builder()
										.name("main")
										.rolls(new RandomValueRange(1, 4))
										.addEntry(
												ItemLootEntry.builder(Items.PORKCHOP)
														.weight(1)
														.acceptFunction(SetCount.builder(new RandomValueRange(1, 3)))
														.acceptFunction(
																Smelt.func_215953_b()
																		.acceptCondition(
																				EntityHasProperty.builder(
																						LootContext.EntityTarget.THIS,
																						EntityPredicate.Builder.create()
																								.flags(EntityFlagsPredicate.Builder.create().onFire(true).build())
																				)
																		)
														)
														.acceptFunction(LootingEnchantBonus.builder(new RandomValueRange(0, 1)))
										)
										.addEntry(
												ItemLootEntry.builder(ModItems.ARROW.get())
														.weight(2)
														.acceptFunction(SetCount.builder(new RandomValueRange(1, 64)))
										)
										.addEntry(
												ItemLootEntry.builder(Items.WOODEN_AXE)
														.weight(1)
														.acceptFunction(SetDamage.func_215931_a(new RandomValueRange(0.28f, 0.28f)))
										)
						)
		);
	}
}
