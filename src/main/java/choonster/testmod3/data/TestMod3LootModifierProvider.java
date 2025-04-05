package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.world.level.storage.loot.modifiers.BlockEntityNBTLootModifier;
import choonster.testmod3.world.level.storage.loot.modifiers.ItemLootModifier;
import choonster.testmod3.world.level.storage.loot.modifiers.LootTableLootModifier;
import choonster.testmod3.world.level.storage.loot.predicates.MatchBlockTag;
import net.minecraft.advancements.critereon.DataComponentMatchers;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.predicates.DataComponentPredicates;
import net.minecraft.core.component.predicates.EnchantmentsPredicate;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Generates this mod's global loot modifier JSON files.
 *
 * @author Choonster
 */
public class TestMod3LootModifierProvider extends GlobalLootModifierProvider {
	public TestMod3LootModifierProvider(final PackOutput output, final CompletableFuture<HolderLookup.Provider> registries) {
		super(output, TestMod3.MODID, registries);
	}

	@Override
	protected void start(final HolderLookup.Provider registries) {
		// Test for this thread:
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2781780-chest-loot
		add("loot_table_test", new LootTableLootModifier(
				new LootItemCondition[]{
						LootItemRandomChanceCondition.randomChance(0.5f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON.location()).build(),
				},
				ModLootTables.LOOT_TABLE_TEST
		));

		// Allows Mob Spawners to be dropped when broken with a Silk Touch pickaxe.
		// Test for this thread:
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677402-unable-to-get-current-blocks-tile-entity-metadata
		add("spawner_drops", new BlockEntityNBTLootModifier(
				new LootItemCondition[]{
						LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SPAWNER).build(),
						hasSilkTouch(registries).build(),
				}
		));

		// Drops two sticks when the player harvests leaves
		add("two_sticks_from_leaves", ItemLootModifier.create(
				new LootItemCondition[]{
						MatchBlockTag.builder(BlockTags.LEAVES).build(),
				},
				Items.STICK,
				List.of(
						SetItemCountFunction.setCount(ConstantValue.exactly(2)).build()
				)
		));
	}

	@Override
	public String getName() {
		return "TestMod3LootModifiers";
	}

	private LootItemCondition.Builder hasSilkTouch(final HolderLookup.Provider registries) {
		final var enchantments = registries.lookupOrThrow(Registries.ENCHANTMENT);

		return MatchTool.toolMatches(
				ItemPredicate.Builder.item()
						.withComponents(
								DataComponentMatchers.Builder.components()
										.partial(
												DataComponentPredicates.ENCHANTMENTS,
												EnchantmentsPredicate.enchantments(
														List.of(
																new EnchantmentPredicate(
																		enchantments.getOrThrow(Enchantments.SILK_TOUCH),
																		MinMaxBounds.Ints.atLeast(1)
																)
														)
												)
										)
										.build()
						)
		);
	}
}
