package choonster.testmod3.world.level.biome.modifier;

import choonster.testmod3.init.levelgen.ModBiomeModifierSerializers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

/**
 * Biome modifier that adds features.
 *
 * @author Choonster
 */
public record AddFeaturesBiomeModifier(
		HolderSet<Biome> biomes,
		HolderSet<PlacedFeature> features,
		GenerationStep.Decoration step
) implements BiomeModifier {
	public static AddFeaturesBiomeModifier create(
			final HolderSet<Biome> biomes,
			final HolderSet<PlacedFeature> features,
			final GenerationStep.Decoration step
	) {
		return new AddFeaturesBiomeModifier(biomes, features, step);
	}

	@Override
	public void modify(final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD && biomes.contains(biome)) {
			features.forEach(feature -> builder.getGenerationSettings().addFeature(step, feature));
		}
	}

	@Override
	public Codec<AddFeaturesBiomeModifier> codec() {
		return ModBiomeModifierSerializers.ADD_FEATURES.get();
	}
}
