package choonster.testmod3.world.gen.structure;

import choonster.testmod3.Logger;
import com.google.common.collect.ImmutableList;
import net.minecraft.util.WeightedRandom;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.ComponentScatteredFeaturePieces;
import net.minecraft.world.gen.structure.MapGenScatteredFeature;
import net.minecraft.world.gen.structure.StructureComponent;
import net.minecraft.world.gen.structure.StructureStart;
import net.minecraftforge.common.BiomeDictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Allows scattered features (jungle/desert temples, witch huts) to spawn in mod biomes equivalent to the vanilla biomes,
 * i.e. any biome registered as JUNGLE, SANDY or SWAMP
 * <p>
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2471489-jungle-and-desert-temple-spawn-biome
 *
 * @author Choonster
 */
public class MapGenScatteredFeatureModBiomes extends MapGenScatteredFeature {
	private final List<BiomeDictionary.Type> biomeTypes = ImmutableList.of(BiomeDictionary.Type.JUNGLE, BiomeDictionary.Type.SANDY, BiomeDictionary.Type.SWAMP);

	@Override
	protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ) {
		// These fields are private in the super class and always constant for non-flat worlds so inline them here
		// Flat worlds don't fire InitMapGenEvent, so this class will never be used in a flat world
		final int minDistanceBetweenScatteredFeatures = 8;
		final int maxDistanceBetweenScatteredFeatures = 32;

		if (chunkX < 0) {
			chunkX -= maxDistanceBetweenScatteredFeatures - 1;
		}

		if (chunkZ < 0) {
			chunkZ -= maxDistanceBetweenScatteredFeatures - 1;
		}

		int i1 = chunkX / maxDistanceBetweenScatteredFeatures;
		int j1 = chunkZ / maxDistanceBetweenScatteredFeatures;
		final Random random = world.setRandomSeed(i1, j1, 14357617);
		i1 *= maxDistanceBetweenScatteredFeatures;
		j1 *= maxDistanceBetweenScatteredFeatures;
		i1 += random.nextInt(maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures);
		j1 += random.nextInt(maxDistanceBetweenScatteredFeatures - minDistanceBetweenScatteredFeatures);

		if (chunkX == i1 && chunkZ == j1) {
			final Biome biome = world.getBiomeProvider().getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));

			for (final BiomeDictionary.Type type : biomeTypes) {
				if (BiomeDictionary.isBiomeOfType(biome, type)) {
					return true;
				}
			}
		}

		return super.canSpawnStructureAtCoords(chunkX, chunkZ);
	}

	@Override
	protected StructureStart getStructureStart(int chunkX, int chunkZ) {
		return new Start(world, rand, chunkX, chunkZ);
	}

	public static class WeightedRandomScatteredFeature extends WeightedRandom.Item {
		public final StructureComponent feature;

		public WeightedRandomScatteredFeature(StructureComponent feature, int itemWeightIn) {
			super(itemWeightIn);
			this.feature = feature;
		}
	}

	public static class Start extends MapGenScatteredFeature.Start {
		@SuppressWarnings("unused")
		public Start() {
		}

		public Start(World worldIn, Random random, int chunkX, int chunkZ) {
			super(worldIn, random, chunkX, chunkZ);

			this.components.clear();

			final Biome biome = worldIn.getBiome(new BlockPos(chunkX * 16 + 8, 0, chunkZ * 16 + 8));

			final List<WeightedRandomScatteredFeature> possibleFeatures = new ArrayList<>();

			if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SANDY)) {
				final ComponentScatteredFeaturePieces.DesertPyramid desertPyramid = new ComponentScatteredFeaturePieces.DesertPyramid(random, chunkX * 16, chunkZ * 16);
				possibleFeatures.add(new WeightedRandomScatteredFeature(desertPyramid, 100));
			}

			if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.JUNGLE)) {
				final ComponentScatteredFeaturePieces.JunglePyramid junglePyramid = new ComponentScatteredFeaturePieces.JunglePyramid(random, chunkX * 16, chunkZ * 16);
				possibleFeatures.add(new WeightedRandomScatteredFeature(junglePyramid, 100));
			}

			if (BiomeDictionary.isBiomeOfType(biome, BiomeDictionary.Type.SWAMP)) {
				final ComponentScatteredFeaturePieces.SwampHut swampHut = new ComponentScatteredFeaturePieces.SwampHut(random, chunkX * 16, chunkZ * 16);
				possibleFeatures.add(new WeightedRandomScatteredFeature(swampHut, 100));
			}

			if (!possibleFeatures.isEmpty()) {
				final WeightedRandomScatteredFeature featureToGenerate = WeightedRandom.getRandomItem(random, possibleFeatures);
				this.components.add(featureToGenerate.feature);
				Logger.info("Scattered feature %s at %d, %d", featureToGenerate.feature.toString(), chunkX * 16, chunkZ * 16);
			}

			this.updateBoundingBox();
		}
	}
}
