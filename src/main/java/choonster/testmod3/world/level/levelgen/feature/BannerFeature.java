package choonster.testmod3.world.level.levelgen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.block.entity.BannerPattern;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Generates Banners with a specific pattern.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class BannerFeature extends Feature<BannerFeatureConfig> {
	private static final Field BASE_COLOR = ObfuscationReflectionHelper.findField(BannerBlockEntity.class, /* baseColor */ "f_58474_");
	private static final Field ITEM_PATTERNS = ObfuscationReflectionHelper.findField(BannerBlockEntity.class, /* itemPatterns */ "f_58475_");
	private static final Field PATTERNS = ObfuscationReflectionHelper.findField(BannerBlockEntity.class, /* patterns */ "f_58477_");
	private static final Field NAME = ObfuscationReflectionHelper.findField(BannerBlockEntity.class, /* name */ "f_58473_");

	public BannerFeature(final Codec<BannerFeatureConfig> codec) {
		super(codec);
	}

	/**
	 * Creates a list of pattern tags to apply to the generated banners.
	 *
	 * @return A list tag containing the patterns
	 */
	protected ListTag createPatternList(final List<Pair<BannerPattern, DyeColor>> patterns) {
		final ListTag patternList = new ListTag();

		for (final Pair<BannerPattern, DyeColor> pattern : patterns) {
			patternList.add(createPatternTag(pattern.getFirst(), pattern.getSecond()));
		}

		return patternList;
	}

	/**
	 * Creates a compound tag for the specified pattern and colour.
	 *
	 * @param pattern The pattern
	 * @param color   The colour
	 * @return The compound tag
	 */
	protected CompoundTag createPatternTag(final BannerPattern pattern, final DyeColor color) {
		final CompoundTag tag = new CompoundTag();
		tag.putString("Pattern", pattern.getHashname());
		tag.putInt("Color", color.getId());
		return tag;
	}

	@Override
	public boolean place(final FeaturePlaceContext<BannerFeatureConfig> context) {
		final WorldGenLevel level = context.level();
		final BlockPos origin = context.origin();
		final BannerFeatureConfig config = context.config();

		level.setBlock(origin, BannerBlock.byColor(config.color()).defaultBlockState(), Block.UPDATE_ALL);

		final BlockEntity blockEntity = level.getBlockEntity(origin);
		if (blockEntity instanceof BannerBlockEntity) {
			loadFromItemStack((BannerBlockEntity) blockEntity, createPatternList(config.patterns()), DyeColor.PINK);
		}

		return true;
	}

	// Adapted from BannerBlockEntity.fromItem
	private static void loadFromItemStack(final BannerBlockEntity blockEntity, final ListTag patterns, final DyeColor color) {
		try {
			BASE_COLOR.set(blockEntity, color);
			ITEM_PATTERNS.set(blockEntity, patterns);
			PATTERNS.set(blockEntity, null);
			NAME.set(blockEntity, null);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to add banner data to BlockEntity", e);
		}
	}
}
