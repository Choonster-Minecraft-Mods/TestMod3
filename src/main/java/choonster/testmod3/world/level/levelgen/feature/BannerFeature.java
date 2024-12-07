package choonster.testmod3.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.BannerBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Generates Banners with a specific pattern.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class BannerFeature extends Feature<BannerFeatureConfig> {
	private static final Field NAME = ObfuscationReflectionHelper.findField(BannerBlockEntity.class, /* name */ "f_58473_");

	public BannerFeature(final Codec<BannerFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(final FeaturePlaceContext<BannerFeatureConfig> context) {
		final var level = context.level();
		final var origin = context.origin();
		final var config = context.config();

		final var color = config.color();
		final var bannerBlock = BannerBlock.byColor(color);
		level.setBlock(origin, bannerBlock.defaultBlockState(), Block.UPDATE_ALL);

		final var blockEntity = level.getBlockEntity(origin);
		if (blockEntity instanceof final BannerBlockEntity bannerBlockEntity) {
			final var bannerItem = new ItemStack(bannerBlock);
			bannerItem.set(DataComponents.BANNER_PATTERNS, config.patterns());
			bannerBlockEntity.applyComponentsFromItemStack(bannerItem);
		}

		return true;
	}
}
