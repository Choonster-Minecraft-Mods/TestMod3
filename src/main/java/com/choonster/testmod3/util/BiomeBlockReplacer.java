package com.choonster.testmod3.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.state.IBlockState;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.IEventListener;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.List;
import java.util.Map;

// Adapted from FloraSoma (https://github.com/LogicTechCorp/FloraSoma)
// Used for example in this thread: http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2418536-get-stair-state-and-apply-it-to-new-stair-block
public class BiomeBlockReplacer implements IEventListener
{

	private Map<Block, List<Pair<String, Block>>> replacements;
	private Map<Block, List<Pair<String, Integer>>> metadata;

	public BiomeBlockReplacer()
	{
		replacements = null; /* ConfigVillage.getReplacements(); */
		metadata = null; /* ConfigVillage.getMetadata(); */
	}

	@Override
	@SubscribeEvent
	public void invoke(Event event)
	{
		if(event instanceof BiomeEvent.GetVillageBlockID)
		{
			BiomeEvent.GetVillageBlockID ev = (BiomeEvent.GetVillageBlockID) event;
			if(replacements.containsKey(ev.original.getBlock()))
			{
				for(Pair<String, Block> pair : replacements.get(ev.original.getBlock()))
				{
					if (checkCondition(ev.biome, pair.left))
					{
						if (ev.original.getBlock() instanceof BlockStairs)
						{
							ev.replacement = copyStairsState(ev.original, pair.right);
						}
						else
						{
							ev.replacement = pair.right.getDefaultState();
						}

						ev.setResult(Event.Result.DENY);
					}
				}
			}
		}
	}

	private static boolean checkCondition(BiomeGenBase biome, String condition)
	{
		if (biome == null || condition == null)
			return false;
		try
		{
			if (condition.startsWith("b:"))
			{
				String identifier = condition.substring("b:".length());
				return identifier.equalsIgnoreCase(biome.biomeName) || identifier.equals(String.valueOf(biome.biomeID));
			}
			if (condition.startsWith("t:"))
			{
				String identifier = condition.substring("t:".length());
				Type type = Type.valueOf(identifier);
				return type != null && BiomeDictionary.isBiomeOfType(biome, type);
			}
			return false;
		} catch (NullPointerException e)
		{
			/*FloraSoma.instance.log.warn("NullPointerException when replacing blocks:");
			e.printStackTrace();
			FloraSoma.instance.log.warn("Something was NULL when replacing blocks.");
			FloraSoma.instance.log.warn("Biome class: " + biome.getClass() + "; Condition: " + condition);*/
			return false;
		}
	}

	private static IBlockState copyStairsState(IBlockState original, Block replacement)
	{
		return replacement.getDefaultState()
				.withProperty(BlockStairs.FACING, original.getValue(BlockStairs.FACING))
				.withProperty(BlockStairs.HALF, original.getValue(BlockStairs.HALF))
				.withProperty(BlockStairs.SHAPE, original.getValue(BlockStairs.SHAPE));
	}
}