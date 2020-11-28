package choonster.testmod3.world.gen.feature;

import choonster.testmod3.serialization.VanillaCodecs;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.DyeColor;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.world.gen.feature.IFeatureConfig;

import java.util.Arrays;
import java.util.List;

public class BannerFeatureConfig implements IFeatureConfig {
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

	private final DyeColor color;
	private final List<Pair<BannerPattern, DyeColor>> patterns;

	private BannerFeatureConfig(final DyeColor color, final List<Pair<BannerPattern, DyeColor>> patterns) {
		this.color = color;
		this.patterns = ImmutableList.copyOf(patterns);
	}

	public DyeColor getColor() {
		return color;
	}

	public List<Pair<BannerPattern, DyeColor>> getPatterns() {
		return patterns;
	}

	@SafeVarargs
	public static BannerFeatureConfig create(final DyeColor color, final Pair<BannerPattern, DyeColor>... patterns) {
		return new BannerFeatureConfig(color, Arrays.asList(patterns));
	}
}
