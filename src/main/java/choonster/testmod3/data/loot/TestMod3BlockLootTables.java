package choonster.testmod3.data.loot;

import choonster.testmod3.block.RightClickTestBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.ConstantRange;
import net.minecraft.loot.ItemLootEntry;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Function;

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
				dropping(block)
						.addLootPool(
								withSurvivesExplosion(Items.ENDER_EYE, LootPool.builder()
										.name("ender_eye")
										.acceptCondition(
												BlockStateProperty.builder(block)
														.fromProperties(
																StatePropertiesPredicate.Builder.newBuilder()
																		.withBoolProp(RightClickTestBlock.HAS_ENDER_EYE, true)
														)
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

		ModBlocks.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(this::registerDropSelfLootTable);

		ModBlocks.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(this::registerDropSelfLootTable);

		ModBlocks.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(this::registerDropSelfLootTable);

		ModBlocks.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(slab -> registerLootTable(slab, BlockLootTables::droppingSlab));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS);
	}

	private void registerLootTable(final RegistryObject<? extends Block> block, final Function<Block, LootTable.Builder> factory) {
		registerLootTable(block.get(), factory);
	}

	private void registerDropSelfLootTable(final RegistryObject<? extends Block> block) {
		registerDropSelfLootTable(block.get()); // TODO: Why is this unchecked?
	}

}
