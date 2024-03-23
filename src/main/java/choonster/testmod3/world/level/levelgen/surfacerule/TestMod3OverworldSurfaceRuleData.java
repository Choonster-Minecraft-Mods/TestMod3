package choonster.testmod3.world.level.levelgen.surfacerule;

import choonster.testmod3.init.levelgen.ModBiomes;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;

/**
 * TerraBlender SurfaceRule source for this mode's biomes.
 * <p>
 * Adapted from {@link net.minecraft.data.worldgen.SurfaceRuleData#overworldLike}.
 *
 * @author Choonster
 */
public class TestMod3OverworldSurfaceRuleData {
	private static final SurfaceRules.RuleSource RED_SAND = makeStateRule(Blocks.RED_SAND);
	private static final SurfaceRules.RuleSource STONE = makeStateRule(Blocks.STONE);
	private static final SurfaceRules.RuleSource DIRT = makeStateRule(Blocks.DIRT);
	private static final SurfaceRules.RuleSource GRASS_BLOCK = makeStateRule(Blocks.GRASS_BLOCK);
	private static final SurfaceRules.RuleSource GRAVEL = makeStateRule(Blocks.GRAVEL);
	private static final SurfaceRules.RuleSource BRICKS = makeStateRule(Blocks.BRICKS);

	public static SurfaceRules.RuleSource makeRules() {
		final var isAtOrAboveWaterLevel = SurfaceRules.waterBlockCheck(-1, 0);
		final var isAboveWaterLevel = SurfaceRules.waterBlockCheck(0, 0);
		final var isBelowShallowWater = SurfaceRules.waterStartCheck(-6, -1);

		final var grassOrDirt = SurfaceRules.sequence(SurfaceRules.ifTrue(isAboveWaterLevel, GRASS_BLOCK), DIRT);
		final var stoneOrGravel = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, STONE), GRAVEL);
		final var bricksOrRedSand = SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_CEILING, BRICKS), RED_SAND);

		final var isDesertTest = SurfaceRules.isBiome(ModBiomes.DESERT_TEST);

		final var commonRules = SurfaceRules.sequence(
				SurfaceRules.ifTrue(isDesertTest, bricksOrRedSand)
		);

		final var allRules = SurfaceRules.sequence(
				SurfaceRules.ifTrue(
						SurfaceRules.ON_FLOOR,
						SurfaceRules.ifTrue(
								isAtOrAboveWaterLevel,
								SurfaceRules.sequence(commonRules, grassOrDirt)
						)
				),
				SurfaceRules.ifTrue(
						isBelowShallowWater,
						SurfaceRules.sequence(
								SurfaceRules.ifTrue(
										SurfaceRules.UNDER_FLOOR,
										SurfaceRules.sequence(commonRules, DIRT)
								),
								SurfaceRules.ifTrue(
										isDesertTest,
										SurfaceRules.ifTrue(SurfaceRules.VERY_DEEP_UNDER_FLOOR, BRICKS)
								)
						)
				),
				SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, stoneOrGravel)
		);

		return SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), allRules);
	}

	private static SurfaceRules.RuleSource makeStateRule(final Block block) {
		return SurfaceRules.state(block.defaultBlockState());
	}
}
