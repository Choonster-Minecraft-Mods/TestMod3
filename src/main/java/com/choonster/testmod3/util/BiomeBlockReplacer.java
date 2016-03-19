package com.choonster.testmod3.util;

import com.choonster.testmod3.Logger;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Replaces village blocks according to biome.
 * <p>
 * Adapted from FloraSoma (https://github.com/LogicTechCorp/FloraSoma)
 * <p>
 * Used for example in this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2418536-get-stair-state-and-apply-it-to-new-stair-block
 *
 * @author LogicTechCorp, Choonster
 */
public class BiomeBlockReplacer {

	private final Map<Block, List<Pair<String, Block>>> replacements;
	private final Map<Block, List<Pair<String, Integer>>> metadata;

	public BiomeBlockReplacer() {
		replacements = Collections.emptyMap(); /* ConfigVillage.getReplacements(); */
		metadata = Collections.emptyMap(); /* ConfigVillage.getMetadata(); */
	}

	@SubscribeEvent
	public void onGetVillageBlockID(BiomeEvent.GetVillageBlockID event) {
		if (replacements.containsKey(event.original.getBlock())) {
			for (Pair<String, Block> pair : replacements.get(event.original.getBlock())) {
				if (metadata.containsKey(event.original.getBlock())) {
					for (Pair<String, Integer> pair1 : metadata.get(event.original.getBlock())) {
						if (checkCondition(event.biome, pair.left)) {
							if (event.original.getBlock() instanceof BlockStairs) {
								event.replacement = copyStairsState(event.original, pair.right);
							} else if (event.original.getBlock() instanceof BlockSlab) {
								event.replacement = copySlabState(event.original, pair.right);
							} else {
								if (checkCondition(event.biome, pair1.left)) {
									if (pair1.right == 0) {
										event.replacement = pair.right.getDefaultState();
									} else {
										event.replacement = pair.right.getStateFromMeta(pair1.right);
									}
								}
							}
							event.setResult(Event.Result.DENY);
						}
					}
				}
			}
		}

	}

	private static boolean checkCondition(BiomeGenBase biome, String condition) {
		if (biome == null || condition == null)
			return false;
		try {
			if (condition.startsWith("b:")) {
				String identifier = condition.substring("b:".length());
				return identifier.equalsIgnoreCase(biome.getBiomeName());
			}
			if (condition.startsWith("t:")) {
				String identifier = condition.substring("t:".length());
				Type type = Type.valueOf(identifier);
				return BiomeDictionary.isBiomeOfType(biome, type);
			}
			return false;
		} catch (NullPointerException e) {
			Logger.error(e, "NullPointerException when replacing blocks:");
			Logger.error("Something was null when replacing blocks.");
			Logger.error("Biome class: %s Condition: %s", biome.getClass(), condition);
			return false;
		}
	}

	private static IBlockState copyStairsState(IBlockState original, Block replacement) {
		return replacement.getDefaultState()
				.withProperty(BlockStairs.FACING, original.getValue(BlockStairs.FACING))
				.withProperty(BlockStairs.HALF, original.getValue(BlockStairs.HALF))
				.withProperty(BlockStairs.SHAPE, original.getValue(BlockStairs.SHAPE));
	}

	private static IBlockState copySlabState(IBlockState original, Block replacement) {
		return replacement.getDefaultState()
				.withProperty(BlockSlab.HALF, original.getValue(BlockSlab.HALF));
	}
}