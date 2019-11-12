package choonster.testmod3.data.loot;

import choonster.testmod3.block.RightClickTestBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.collect.Sets;
import net.minecraft.block.Block;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.loot.BlockLootTables;
import net.minecraft.item.Items;
import net.minecraft.state.properties.SlabType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.*;
import net.minecraft.world.storage.loot.conditions.BlockStateProperty;
import net.minecraft.world.storage.loot.conditions.SurvivesExplosion;
import net.minecraft.world.storage.loot.functions.ExplosionDecay;
import net.minecraft.world.storage.loot.functions.SetCount;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

/**
 * Generates this mod's block loot tables.
 *
 * @author Choonster
 */
public class TestMod3BlockLootTables extends BlockLootTables {
	private static final Field BUILDERS = ObfuscationReflectionHelper.findField(BlockLootTables.class, "field_218581_i" /* TODO: No MCP name yet */);

	@Override
	public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
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
				.forEach(slab -> registerLootTable(slab, this::getSlabLootTable));

		final Map<ResourceLocation, LootTable.Builder> builders = getBuilders();
		final Set<ResourceLocation> registryNames = Sets.newHashSet();

		for (final Block block : RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS)) {
			final ResourceLocation lootTable = block.getLootTable();
			if (lootTable != LootTables.EMPTY && registryNames.add(lootTable)) {
				final LootTable.Builder lootTableBuilder = builders.remove(lootTable);
				if (lootTableBuilder == null) {
					throw new IllegalStateException(String.format("Missing loot table '%s' for '%s'", lootTable, block.getRegistryName()));
				}

				consumer.accept(lootTable, lootTableBuilder);
			}
		}

		if (!builders.isEmpty()) {
			throw new IllegalStateException("Created block loot tables for non-blocks: " + builders.keySet());
		}
	}

	private static <T> T withExplosionDecay(final ILootFunctionConsumer<T> lootFunctionConsumer) {
		return lootFunctionConsumer.acceptFunction(ExplosionDecay.builder());
	}

	private static <T> T withSurvivesExplosion(final ILootConditionConsumer<T> lootConditionConsumer) {
		return lootConditionConsumer.acceptCondition(SurvivesExplosion.builder());
	}

	private LootTable.Builder getSlabLootTable(final Block block) {
		return LootTable.builder().addLootPool(
				LootPool.builder()
						.rolls(ConstantRange.of(1))
						.addEntry(
								withExplosionDecay(
										ItemLootEntry.builder(block)
												.acceptFunction(
														SetCount.builder(ConstantRange.of(2))
																.acceptCondition(BlockStateProperty.builder(block).with(SlabBlock.TYPE, SlabType.DOUBLE))
												)
								)
						)
		);
	}

	private Map<ResourceLocation, LootTable.Builder> getBuilders() {
		try {
			@SuppressWarnings("unchecked")
			final Map<ResourceLocation, LootTable.Builder> builders = (Map<ResourceLocation, LootTable.Builder>) BUILDERS.get(this);
			return builders;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get builders Map", e);
		}
	}
}
