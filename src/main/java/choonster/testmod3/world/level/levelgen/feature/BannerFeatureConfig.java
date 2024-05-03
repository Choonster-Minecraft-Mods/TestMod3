package choonster.testmod3.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPatternLayers;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record BannerFeatureConfig(
		DyeColor color,
		BannerPatternLayers patterns
) implements FeatureConfiguration {
	public static final Codec<BannerFeatureConfig> CODEC = RecordCodecBuilder.create((builder) ->
			builder.group(

					DyeColor.CODEC
							.fieldOf("color")
							.forGetter(BannerFeatureConfig::color),

					BannerPatternLayers.CODEC
							.fieldOf("patterns")
							.forGetter(BannerFeatureConfig::patterns)

			).apply(builder, BannerFeatureConfig::new)
	);
}
