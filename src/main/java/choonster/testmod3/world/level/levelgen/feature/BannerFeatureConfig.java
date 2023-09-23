package choonster.testmod3.world.level.levelgen.feature;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
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

					DyeColor.CODEC
							.fieldOf("color")
							.forGetter(BannerFeatureConfig::color),

					Codec.mapPair(
									BuiltInRegistries.BANNER_PATTERN.byNameCodec().fieldOf("pattern"),
									DyeColor.CODEC.fieldOf("color")
							)
							.codec()
							.listOf()
							.fieldOf("patterns")
							.forGetter(BannerFeatureConfig::patterns)

			).apply(builder, BannerFeatureConfig::new)
	);

	public BannerFeatureConfig(final DyeColor color, final List<Pair<BannerPattern, DyeColor>> patterns) {
		this.color = color;
		this.patterns = ImmutableList.copyOf(patterns);
	}

	@SafeVarargs
	public static BannerFeatureConfig create(final DyeColor color, final Pair<ResourceKey<BannerPattern>, DyeColor>... patterns) {
		return new BannerFeatureConfig(
				color,
				Arrays.stream(patterns)
						.map(pair -> Pair.of(BuiltInRegistries.BANNER_PATTERN.get(pair.getFirst()), pair.getSecond()))
						.toList()
		);
	}
}
