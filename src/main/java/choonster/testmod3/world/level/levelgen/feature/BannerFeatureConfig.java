package choonster.testmod3.world.level.levelgen.feature;

import choonster.testmod3.serialization.VanillaCodecs;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

import java.util.Arrays;
import java.util.List;

public record BannerFeatureConfig(
		DyeColor color,
		List<Pair<BannerPattern, DyeColor>> patterns
) implements FeatureConfiguration {
	public static final Codec<BannerFeatureConfig> CODEC = RecordCodecBuilder.create((builder) ->
			builder.group(

					VanillaCodecs.DYE_COLOR
							.fieldOf("color")
							.forGetter(config -> config.color),

					Codec.mapPair(
									VanillaCodecs.BANNER_PATTERN.fieldOf("pattern"),
									VanillaCodecs.DYE_COLOR.fieldOf("color")
							)
							.codec()
							.listOf()
							.fieldOf("patterns")
							.forGetter(config -> config.patterns)

			).apply(builder, BannerFeatureConfig::new)
	);

	public BannerFeatureConfig(final DyeColor color, final List<Pair<BannerPattern, DyeColor>> patterns) {
		this.color = color;
		this.patterns = ImmutableList.copyOf(patterns);
	}

	@SafeVarargs
	public static BannerFeatureConfig create(final DyeColor color, final Pair<BannerPattern, DyeColor>... patterns) {
		return new BannerFeatureConfig(color, Arrays.asList(patterns));
	}
}
