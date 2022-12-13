package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.world.level.storage.loot.modifiers.BlockEntityNBTLootModifier;
import choonster.testmod3.world.level.storage.loot.modifiers.ItemLootModifier;
import choonster.testmod3.world.level.storage.loot.modifiers.LootTableLootModifier;
import choonster.testmod3.world.level.storage.loot.predicates.MatchBlockTag;
import net.minecraft.advancements.critereon.EnchantmentPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.level.storage.loot.predicates.MatchTool;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

/**
 * Generates this mod's global loot modifier JSON files.
 *
 * @author Choonster
 */
public class TestMod3LootModifierProvider extends GlobalLootModifierProvider {
	private final LootItemCondition.Builder SILK_TOUCH = MatchTool.toolMatches(
			ItemPredicate.Builder.item()
					.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.Ints.atLeast(1)))
	);

	public TestMod3LootModifierProvider(final PackOutput output) {
		super(output, TestMod3.MODID);
	}

	@Override
	protected void start() {
		// Test for this thread:
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2781780-chest-loot
		add("loot_table_test", new LootTableLootModifier(
				new LootItemCondition[]{
						LootItemRandomChanceCondition.randomChance(0.5f).build(),
						LootTableIdCondition.builder(BuiltInLootTables.SIMPLE_DUNGEON).build(),
				},
				ModLootTables.LOOT_TABLE_TEST
		));

		// Allows Mob Spawners to be dropped when broken with a Silk Touch pickaxe.
		// Test for this thread:
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677402-unable-to-get-current-blocks-tile-entity-metadata
		add("spawner_drops", new BlockEntityNBTLootModifier(
				new LootItemCondition[]{
						LootItemBlockStatePropertyCondition.hasBlockStateProperties(Blocks.SPAWNER).build(),
						SILK_TOUCH.build(),
				}
		));

		// Drops two sticks when the player harvests leaves
		add("two_sticks_from_leaves", new ItemLootModifier(
				new LootItemCondition[]{
						MatchBlockTag.builder(BlockTags.LEAVES).build(),
				},
				Items.STICK,
				new LootItemFunction[]{
						SetItemCountFunction.setCount(ConstantValue.exactly(2)).build(),
				}
		));
	}

	@Override
	public String getName() {
		return "TestMod3LootModifiers";
	}
}
