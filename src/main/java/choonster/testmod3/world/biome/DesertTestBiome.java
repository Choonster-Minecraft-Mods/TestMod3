package choonster.testmod3.world.biome;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.DefaultBiomeFeatures;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.IFeatureConfig;
import net.minecraft.world.gen.feature.structure.MineshaftConfig;
import net.minecraft.world.gen.feature.structure.MineshaftStructure;
import net.minecraft.world.gen.feature.structure.PillagerOutpostConfig;
import net.minecraft.world.gen.feature.structure.VillageConfig;
import net.minecraft.world.gen.surfacebuilders.ConfiguredSurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.ISurfaceBuilderConfig;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilder;
import net.minecraft.world.gen.surfacebuilders.SurfaceBuilderConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Random;

/**
 * A Desert-like biome with Red Sand as the top block and Brick Block as the filler block.
 *
 * @author Choonster
 */
public final class DesertTestBiome extends Biome {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final SurfaceBuilderConfig RED_SAND_RED_SAND_BRICKS_SURFACE = new SurfaceBuilderConfig(
			Blocks.RED_SAND.getDefaultState(),
			Blocks.RED_SAND.getDefaultState(),
			Blocks.BRICKS.getDefaultState()
	);

	public DesertTestBiome() {
		super(new Biome.Builder()
				.surfaceBuilder(new LoggingConfiguredSurfaceBuilder<>(SurfaceBuilder.DEFAULT, RED_SAND_RED_SAND_BRICKS_SURFACE))
				.precipitation(RainType.NONE)
				.category(Category.DESERT)
				.depth(0.125F)
				.scale(0.05F)
				.temperature(2.0F)
				.downfall(0.0F)
				.waterColor(4159204)
				.waterFogColor(329011)
				.parent(null)
		);

		addStructure(Feature.VILLAGE, new VillageConfig("village/desert/town_centers", 6));
		addStructure(Feature.PILLAGER_OUTPOST, new PillagerOutpostConfig(0.004D));
		addStructure(Feature.DESERT_PYRAMID, IFeatureConfig.NO_FEATURE_CONFIG);
		addStructure(Feature.MINESHAFT, new MineshaftConfig(0.004D, MineshaftStructure.Type.NORMAL));
		addStructure(Feature.STRONGHOLD, IFeatureConfig.NO_FEATURE_CONFIG);

		DefaultBiomeFeatures.addCarvers(this);
		DefaultBiomeFeatures.addStructures(this);
		DefaultBiomeFeatures.addDesertLakes(this);
		DefaultBiomeFeatures.addMonsterRooms(this);
		DefaultBiomeFeatures.addStoneVariants(this);
		DefaultBiomeFeatures.addOres(this);
		DefaultBiomeFeatures.addSedimentDisks(this);
		DefaultBiomeFeatures.addDefaultFlowers(this);
		DefaultBiomeFeatures.addSparseGrass(this);
		DefaultBiomeFeatures.addDeadBushes(this);
		DefaultBiomeFeatures.addMushrooms(this);
		DefaultBiomeFeatures.addExtraReedsPumpkinsCactus(this);
		DefaultBiomeFeatures.addSprings(this);
		DefaultBiomeFeatures.addDesertFeatures(this);
		DefaultBiomeFeatures.addFreezeTopLayer(this);

		addSpawn(EntityClassification.CREATURE, new SpawnListEntry(EntityType.RABBIT, 4, 2, 3));
		addSpawn(EntityClassification.AMBIENT, new SpawnListEntry(EntityType.BAT, 10, 8, 8));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SPIDER, 100, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SKELETON, 100, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.CREEPER, 100, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.SLIME, 100, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ENDERMAN, 10, 1, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.WITCH, 5, 1, 1));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE, 19, 4, 4));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.ZOMBIE_VILLAGER, 1, 1, 1));
		addSpawn(EntityClassification.MONSTER, new SpawnListEntry(EntityType.HUSK, 80, 4, 4));
	}

	private static class LoggingConfiguredSurfaceBuilder<C extends ISurfaceBuilderConfig> extends ConfiguredSurfaceBuilder<C> {
		private boolean logged = false;

		private LoggingConfiguredSurfaceBuilder(final SurfaceBuilder<C> surfaceBuilder, final C config) {
			super(surfaceBuilder, config);
		}

		@Override
		public void buildSurface(
				final Random random, final IChunk chunk, final Biome biome,
				final int x, final int z, final int startHeight, final double noise,
				final BlockState defaultBlock, final BlockState defaultFluid,
				final int seaLevel, final long seed
		) {
			super.buildSurface(random, chunk, biome, x, z, startHeight, noise, defaultBlock, defaultFluid, seaLevel, seed);

			if (!logged) {
				logged = true;
				final ChunkPos chunkPos = chunk.getPos();
				LOGGER.info("Generating desert test at {},{}", chunkPos.getXStart(), chunkPos.getZStart());
			}
		}
	}
}
