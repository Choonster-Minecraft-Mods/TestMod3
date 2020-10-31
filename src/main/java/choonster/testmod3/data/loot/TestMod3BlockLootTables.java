package choonster.testmod3.data.loot;

import choonster.testmod3.block.FluidTankBlock;
import choonster.testmod3.block.RightClickTestBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.loot.functions.SetFluidTankContents;
import choonster.testmod3.util.RegistryUtil;
import net.minecraft.advancements.criterion.StatePropertiesPredicate;
import net.minecraft.block.Block;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.loot.*;
import net.minecraft.loot.conditions.BlockStateProperty;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Generates this mod's block loot tables.
 *
 * @author Choonster
 */
public class TestMod3BlockLootTables extends BlockLootTables {
	@Override
	protected void addTables() {
		registerDropSelfLootTable(ModBlocks.WATER_GRASS.get());
		registerDropSelfLootTable(ModBlocks.LARGE_COLLISION_TEST.get());

		registerLootTable(ModBlocks.RIGHT_CLICK_TEST.get(), block -> (
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

		registerDropSelfLootTable(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get());
		registerDropSelfLootTable(ModBlocks.ROTATABLE_LAMP.get());
		registerDropSelfLootTable(ModBlocks.ITEM_COLLISION_TEST.get());
		registerLootTable(ModBlocks.FLUID_TANK.get(), TestMod3BlockLootTables::droppingWithFluidTankContents);
		registerDropSelfLootTable(ModBlocks.ITEM_DEBUGGER.get());
		registerDropSelfLootTable(ModBlocks.END_PORTAL_FRAME_FULL.get());
		registerDropSelfLootTable(ModBlocks.POTION_EFFECT.get());
		registerDropSelfLootTable(ModBlocks.CLIENT_PLAYER_ROTATION.get());
		registerDropSelfLootTable(ModBlocks.PIG_SPAWNER_REFILLER.get());
		registerDropSelfLootTable(ModBlocks.MIRROR_PLANE.get());
		registerDropSelfLootTable(ModBlocks.VANILLA_MODEL_TEST.get());
		registerDropSelfLootTable(ModBlocks.FULLBRIGHT.get());
		registerDropSelfLootTable(ModBlocks.NORMAL_BRIGHTNESS.get());
		registerDropSelfLootTable(ModBlocks.MAX_HEALTH_SETTER.get());
		registerDropSelfLootTable(ModBlocks.MAX_HEALTH_GETTER.get());
		registerDropSelfLootTable(ModBlocks.SMALL_COLLISION_TEST.get());
		registerDropSelfLootTable(ModBlocks.CHEST.get());
		registerDropSelfLootTable(ModBlocks.HIDDEN.get());
		registerDropSelfLootTable(ModBlocks.BASIC_PIPE.get());
		registerDropSelfLootTable(ModBlocks.FLUID_PIPE.get());
		registerDropSelfLootTable(ModBlocks.SURVIVAL_COMMAND_BLOCK.get());
		registerDropSelfLootTable(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK.get());
		registerDropSelfLootTable(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK.get());
		registerDropSelfLootTable(ModBlocks.OAK_SAPLING.get());
		registerDropSelfLootTable(ModBlocks.SPRUCE_SAPLING.get());
		registerDropSelfLootTable(ModBlocks.BIRCH_SAPLING.get());
		registerDropSelfLootTable(ModBlocks.JUNGLE_SAPLING.get());
		registerDropSelfLootTable(ModBlocks.ACACIA_SAPLING.get());
		registerDropSelfLootTable(ModBlocks.DARK_OAK_SAPLING.get());
		registerDropSelfLootTable(ModBlocks.INVISIBLE.get());
		registerLootTable(ModBlocks.FLUID_TANK_RESTRICTED.get(), TestMod3BlockLootTables::droppingWithFluidTankContents);
		registerDropSelfLootTable(ModBlocks.PLANKS.get());

		ModBlocks.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> registerDropSelfLootTable(block.get()));

		ModBlocks.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> registerDropSelfLootTable(block.get()));

		ModBlocks.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(block -> registerDropSelfLootTable(block.get()));

		ModBlocks.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(slab -> registerLootTable(slab.get(), BlockLootTables::droppingSlab));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS);
	}

	protected static LootTable.Builder droppingWithFluidTankContents(final Block block) {
		return LootTable.builder()
				.addLootPool(
						withSurvivesExplosion(block,
								LootPool.builder()
										.rolls(ConstantRange.of(1))
										.addEntry(
												ItemLootEntry.builder(block)
														.acceptFunction(
																SetFluidTankContents.builder()
																		.addLootEntry(DynamicLootEntry.func_216162_a(FluidTankBlock.FLUID_TANK_CONTENTS))
														)
										)
						)
				);
	}
}
