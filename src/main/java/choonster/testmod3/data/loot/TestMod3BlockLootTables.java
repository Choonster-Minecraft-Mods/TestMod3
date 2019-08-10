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
import java.util.function.Function;

/**
 * Generates this mod's block loot tables.
 *
 * @author Choonster
 */
public class TestMod3BlockLootTables extends BlockLootTables {
	private static final Field BUILDERS = ObfuscationReflectionHelper.findField(BlockLootTables.class, "field_218581_i" /* TODO: No MCP name yet */);

	@Override
	public void accept(final BiConsumer<ResourceLocation, LootTable.Builder> consumer) {
		func_218492_c(ModBlocks.WATER_GRASS);
		func_218492_c(ModBlocks.LARGE_COLLISION_TEST);

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

		func_218492_c(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK);
		func_218492_c(ModBlocks.ROTATABLE_LAMP);
		func_218492_c(ModBlocks.ITEM_COLLISION_TEST);
		func_218492_c(ModBlocks.FLUID_TANK);
		func_218492_c(ModBlocks.ITEM_DEBUGGER);
		func_218492_c(ModBlocks.END_PORTAL_FRAME_FULL);
		func_218492_c(ModBlocks.POTION_EFFECT);
		func_218492_c(ModBlocks.CLIENT_PLAYER_ROTATION);
		func_218492_c(ModBlocks.PIG_SPAWNER_REFILLER);
		func_218492_c(ModBlocks.MIRROR_PLANE);
		func_218492_c(ModBlocks.VANILLA_MODEL_TEST);
		func_218492_c(ModBlocks.FULLBRIGHT);
		func_218492_c(ModBlocks.NORMAL_BRIGHTNESS);
		func_218492_c(ModBlocks.MAX_HEALTH_SETTER);
		func_218492_c(ModBlocks.MAX_HEALTH_GETTER);
		func_218492_c(ModBlocks.SMALL_COLLISION_TEST);
		func_218492_c(ModBlocks.CHEST);
		func_218492_c(ModBlocks.HIDDEN);
		func_218492_c(ModBlocks.BASIC_PIPE);
		func_218492_c(ModBlocks.FLUID_PIPE);
		func_218492_c(ModBlocks.SURVIVAL_COMMAND_BLOCK);
		func_218492_c(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK);
		func_218492_c(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK);
		func_218492_c(ModBlocks.OAK_SAPLING);
		func_218492_c(ModBlocks.SPRUCE_SAPLING);
		func_218492_c(ModBlocks.BIRCH_SAPLING);
		func_218492_c(ModBlocks.JUNGLE_SAPLING);
		func_218492_c(ModBlocks.ACACIA_SAPLING);
		func_218492_c(ModBlocks.DARK_OAK_SAPLING);
		func_218492_c(ModBlocks.INVISIBLE);
		func_218492_c(ModBlocks.FLUID_TANK_RESTRICTED);
		func_218492_c(ModBlocks.PLANKS);

		ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(this::func_218492_c);

		ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(this::func_218492_c);

		ModBlocks.VariantGroups.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(this::func_218492_c);

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
		return lootFunctionConsumer.acceptFunction(ExplosionDecay.func_215863_b());
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
														SetCount.func_215932_a(ConstantRange.of(2))
																.acceptCondition(BlockStateProperty.builder(block).with(SlabBlock.TYPE, SlabType.DOUBLE))
												)
								)
						)
		);
	}

	private void registerLootTable(final Block block, final Function<Block, LootTable.Builder> createLootTable) {
		registerLootTable(block, createLootTable.apply(block));
	}

	private void registerLootTable(final Block block, final LootTable.Builder lootTableBuilder) {
		getBuilders().put(block.getLootTable(), lootTableBuilder);
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
