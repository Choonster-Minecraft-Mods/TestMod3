package choonster.testmod3.data.loot;

import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.RegistryUtil;
import choonster.testmod3.world.level.block.FluidTankBlock;
import choonster.testmod3.world.level.block.RightClickTestBlock;
import choonster.testmod3.world.level.storage.loot.functions.SetFluidTankContents;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.Set;

/**
 * Generates this mod's block loot tables.
 *
 * @author Choonster
 */
public class TestMod3BlockLoot extends BlockLootSubProvider {
	public TestMod3BlockLoot() {
		super(Set.of(), FeatureFlags.REGISTRY.allFlags());
	}

	@Override
	protected void generate() {
		dropSelf(ModBlocks.WATER_GRASS.get());
		dropSelf(ModBlocks.LARGE_COLLISION_TEST.get());

		add(ModBlocks.RIGHT_CLICK_TEST.get(), block -> (
				createSingleItemTable(block)
						.withPool(
								applyExplosionCondition(Items.ENDER_EYE, LootPool.lootPool()
										.when(
												LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
														.setProperties(
																StatePropertiesPredicate.Builder.properties()
																		.hasProperty(RightClickTestBlock.HAS_ENDER_EYE, true)
														)
										)
										.setRolls(ConstantValue.exactly(1))
										.add(LootItem.lootTableItem(Items.ENDER_EYE))
								)
						)
		));

		dropSelf(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get());
		dropSelf(ModBlocks.ROTATABLE_LAMP.get());
		dropSelf(ModBlocks.ITEM_COLLISION_TEST.get());
		add(ModBlocks.FLUID_TANK.get(), this::droppingWithFluidTankContents);
		dropSelf(ModBlocks.ITEM_DEBUGGER.get());
		dropSelf(ModBlocks.END_PORTAL_FRAME_FULL.get());
		dropSelf(ModBlocks.POTION_EFFECT.get());
		dropSelf(ModBlocks.CLIENT_PLAYER_ROTATION.get());
		dropSelf(ModBlocks.PIG_SPAWNER_REFILLER.get());
		dropSelf(ModBlocks.MIRROR_PLANE.get());
		dropSelf(ModBlocks.VANILLA_MODEL_TEST.get());
		dropSelf(ModBlocks.FULLBRIGHT.get());
		dropSelf(ModBlocks.NORMAL_BRIGHTNESS.get());
		dropSelf(ModBlocks.MAX_HEALTH_SETTER.get());
		dropSelf(ModBlocks.MAX_HEALTH_GETTER.get());
		dropSelf(ModBlocks.SMALL_COLLISION_TEST.get());
		dropSelf(ModBlocks.CHEST.get());
		dropSelf(ModBlocks.HIDDEN.get());
		dropSelf(ModBlocks.BASIC_PIPE.get());
		dropSelf(ModBlocks.FLUID_PIPE.get());
		dropSelf(ModBlocks.SURVIVAL_COMMAND_BLOCK.get());
		dropSelf(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK.get());
		dropSelf(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK.get());
		dropSelf(ModBlocks.OAK_SAPLING.get());
		dropSelf(ModBlocks.SPRUCE_SAPLING.get());
		dropSelf(ModBlocks.BIRCH_SAPLING.get());
		dropSelf(ModBlocks.JUNGLE_SAPLING.get());
		dropSelf(ModBlocks.ACACIA_SAPLING.get());
		dropSelf(ModBlocks.DARK_OAK_SAPLING.get());
		dropSelf(ModBlocks.INVISIBLE.get());
		add(ModBlocks.FLUID_TANK_RESTRICTED.get(), this::droppingWithFluidTankContents);
		dropSelf(ModBlocks.PLANKS.get());

		ModBlocks.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> dropSelf(block.get()));

		ModBlocks.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> dropSelf(block.get()));

		ModBlocks.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(block -> dropSelf(block.get()));

		ModBlocks.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(slab -> add(slab.get(), this::createSlabItemTable));
	}

	@Override
	protected Iterable<Block> getKnownBlocks() {
		return RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS);
	}

	protected LootTable.Builder droppingWithFluidTankContents(final Block block) {
		return LootTable.lootTable()
				.withPool(
						applyExplosionCondition(block,
								LootPool.lootPool()
										.setRolls(ConstantValue.exactly(1))
										.add(
												LootItem.lootTableItem(block)
														.apply(
																SetFluidTankContents.builder()
																		.addLootEntry(DynamicLoot.dynamicEntry(FluidTankBlock.FLUID_TANK_CONTENTS))
														)
										)
						)
				);
	}
}
