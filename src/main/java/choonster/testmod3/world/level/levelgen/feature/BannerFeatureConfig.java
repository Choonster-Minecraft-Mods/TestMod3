package choonster.testmod3.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record BannerFeatureConfig(
		BlockStateProvider bannerProvider,
		BannerPatternLayers patterns
) implements FeatureConfiguration {
	public static final Codec<BannerFeatureConfig> CODEC = RecordCodecBuilder.create((builder) ->
			builder.group(

					BlockStateProvider.CODEC
							.fieldOf("banner_provider")
							.forGetter(BannerFeatureConfig::bannerProvider),

					BannerPatternLayers.CODEC
							.fieldOf("patterns")
							.forGetter(BannerFeatureConfig::patterns)

			).apply(builder, BannerFeatureConfig::new)
	);
}
