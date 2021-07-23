package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModLootModifierSerializers;
import choonster.testmod3.init.ModLootTables;
import choonster.testmod3.loot.conditions.IsChestLoot;
import choonster.testmod3.loot.conditions.MatchBlockTag;
import choonster.testmod3.loot.modifiers.ItemLootModifier;
import choonster.testmod3.loot.modifiers.LootTableLootModifier;
import choonster.testmod3.loot.modifiers.TileEntityNBTLootModifier;
import net.minecraft.advancements.criterion.EnchantmentPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.MinMaxBounds;
import net.minecraft.block.Blocks;
import net.minecraft.data.DataGenerator;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.conditions.MatchTool;
import net.minecraft.loot.conditions.RandomChance;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.loot.functions.SetCount;
import net.minecraft.tags.BlockTags;
import net.minecraftforge.common.data.GlobalLootModifierProvider;

/**
 * Generates this mod's global loot modifier JSON files.
 *
 * @author Choonster
 */
public class TestMod3LootModifierProvider extends GlobalLootModifierProvider {
	private final ILootCondition.IBuilder SILK_TOUCH = MatchTool.toolMatches(
			ItemPredicate.Builder.item()
					.hasEnchantment(new EnchantmentPredicate(Enchantments.SILK_TOUCH, MinMaxBounds.IntBound.atLeast(1)))
	);

	public TestMod3LootModifierProvider(final DataGenerator gen) {
		super(gen, TestMod3.MODID);
	}

	@Override
	protected void start() {
		// Test for this thread:
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2781780-chest-loot
		add("loot_table_test", ModLootModifierSerializers.LOOT_TABLE.get(), new LootTableLootModifier(
				new ILootCondition[]{
						RandomChance.randomChance(0.5f).build(),
						/*
						 TODO: Replace with loot table ID condition for minecraft:chests/simple_dungeon once
						  https://github.com/MinecraftForge/MinecraftForge/pull/7428 is merged
						 */
						IsChestLoot.builder().build(),
				},
				ModLootTables.LOOT_TABLE_TEST
		));

		// Allows Mob Spawners to be dropped when broken with a Silk Touch pickaxe.
		// Test for this thread:
		// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2677402-unable-to-get-current-blocks-tile-entity-metadata
		add("spawner_drops", ModLootModifierSerializers.TILE_ENTITY_NBT.get(), new TileEntityNBTLootModifier(
				new ILootCondition[]{
						BlockStateProperty.hasBlockStateProperties(Blocks.SPAWNER).build(),
						SILK_TOUCH.build(),
				}
		));

		// Drops two sticks when the player harvests leaves
		add("two_sticks_from_leaves", ModLootModifierSerializers.ITEM.get(), new ItemLootModifier(
				new ILootCondition[]{
						MatchBlockTag.builder(BlockTags.LEAVES).build(),
				},
				Items.STICK,
				new ILootFunction[]{
						SetCount.setCount(ConstantRange.exactly(2)).build(),
				}
		));
	}

	@Override
	public String getName() {
		return "TestMod3LootModifiers";
	}
}
