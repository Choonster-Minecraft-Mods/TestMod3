package choonster.testmod3.data.loot;

import choonster.testmod3.block.RightClickTestBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Generates this mod's block loot tables.
 *
 * @author Choonster
 */
public class TestMod3BlockLootTables extends BlockLootTables {
	@Override
	protected void addTables() {
		registerDropSelfLootTable(ModBlocks.WATER_GRASS);
		registerDropSelfLootTable(ModBlocks.LARGE_COLLISION_TEST);

		registerLootTable(ModBlocks.RIGHT_CLICK_TEST, block -> (
				LootTable.builder()
						.addLootPool(
								withSurvivesExplosion(LootPool.builder()
										.name("main")
										.rolls(ConstantRange.of(1))
										.addEntry(ItemLootEntry.builder(block))
								)
						)
						.addLootPool(
								withSurvivesExplosion(LootPool.builder()
										.name("ender_eye")
										.acceptCondition(
												BlockStateProperty.builder(block)
														.with(RightClickTestBlock.HAS_ENDER_EYE, true)
										)
										.rolls(ConstantRange.of(1))
										.addEntry(ItemLootEntry.builder(Items.ENDER_EYE))
								)
						)
		));

		registerDropSelfLootTable(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK);
		registerDropSelfLootTable(ModBlocks.ROTATABLE_LAMP);
		registerDropSelfLootTable(ModBlocks.ITEM_COLLISION_TEST);
		registerDropSelfLootTable(ModBlocks.FLUID_TANK);
		registerDropSelfLootTable(ModBlocks.ITEM_DEBUGGER);
		registerDropSelfLootTable(ModBlocks.END_PORTAL_FRAME_FULL);
		registerDropSelfLootTable(ModBlocks.POTION_EFFECT);
		registerDropSelfLootTable(ModBlocks.CLIENT_PLAYER_ROTATION);
		registerDropSelfLootTable(ModBlocks.PIG_SPAWNER_REFILLER);
		registerDropSelfLootTable(ModBlocks.MIRROR_PLANE);
		registerDropSelfLootTable(ModBlocks.VANILLA_MODEL_TEST);
		registerDropSelfLootTable(ModBlocks.FULLBRIGHT);
		registerDropSelfLootTable(ModBlocks.NORMAL_BRIGHTNESS);
		registerDropSelfLootTable(ModBlocks.MAX_HEALTH_SETTER);
		registerDropSelfLootTable(ModBlocks.MAX_HEALTH_GETTER);
		registerDropSelfLootTable(ModBlocks.SMALL_COLLISION_TEST);
		registerDropSelfLootTable(ModBlocks.CHEST);
		registerDropSelfLootTable(ModBlocks.HIDDEN);
		registerDropSelfLootTable(ModBlocks.BASIC_PIPE);
		registerDropSelfLootTable(ModBlocks.FLUID_PIPE);
		registerDropSelfLootTable(ModBlocks.SURVIVAL_COMMAND_BLOCK);
		registerDropSelfLootTable(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK);
		registerDropSelfLootTable(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK);
		registerDropSelfLootTable(ModBlocks.OAK_SAPLING);
		registerDropSelfLootTable(ModBlocks.SPRUCE_SAPLING);
		registerDropSelfLootTable(ModBlocks.BIRCH_SAPLING);
		registerDropSelfLootTable(ModBlocks.JUNGLE_SAPLING);
		registerDropSelfLootTable(ModBlocks.ACACIA_SAPLING);
		registerDropSelfLootTable(ModBlocks.DARK_OAK_SAPLING);
		registerDropSelfLootTable(ModBlocks.INVISIBLE);
		registerDropSelfLootTable(ModBlocks.FLUID_TANK_RESTRICTED);
		registerDropSelfLootTable(ModBlocks.PLANKS);

		ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(this::registerDropSelfLootTable);

		ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(this::registerDropSelfLootTable);

		ModBlocks.VariantGroups.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(this::registerDropSelfLootTable);

		ModBlocks.VariantGroups.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(slab -> registerLootTable(slab, BlockLootTables::droppingSlab));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS);
	}

	private static <T> T withSurvivesExplosion(final ILootConditionConsumer<T> lootConditionConsumer) {
		return lootConditionConsumer.acceptCondition(SurvivesExplosion.builder());
	}
}
