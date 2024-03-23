package choonster.testmod3.world.level.biome;

import choonster.testmod3.init.levelgen.ModBiomes;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.Region;
import terrablender.api.RegionType;

import java.util.function.Consumer;

/**
 * TerraBlender region that adds this mod's biomes.
 *
 * @author Choonster
 */
public class TestMod3OverworldRegion extends Region {
	public TestMod3OverworldRegion(final ResourceLocation name, final int weight) {
		super(name, RegionType.OVERWORLD, weight);
	}

	@Override
	public void addBiomes(final Registry<Biome> registry, final Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
		addBiomeSimilar(mapper, Biomes.DESERT, ModBiomes.DESERT_TEST);
	}
}
