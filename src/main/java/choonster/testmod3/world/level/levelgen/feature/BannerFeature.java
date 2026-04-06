package choonster.testmod3.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BannerBlockEntity;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

/**
 * Generates Banners with a specific pattern.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class BannerFeature extends Feature<BannerFeatureConfig> {
	public BannerFeature(final Codec<BannerFeatureConfig> codec) {
		super(codec);
	}

	@Override
	public boolean place(final FeaturePlaceContext<BannerFeatureConfig> context) {
		final var level = context.level();
		final var origin = context.origin();
		final var config = context.config();

		final var bannerState = config.bannerProvider().getState(level, context.random(), origin);
		level.setBlock(origin, bannerState, Block.UPDATE_ALL);

		final var blockEntity = level.getBlockEntity(origin);
		if (blockEntity instanceof final BannerBlockEntity bannerBlockEntity) {
			final var bannerItem = new ItemStack(bannerState.getBlock());
			bannerItem.set(DataComponents.BANNER_PATTERNS, config.patterns());
			bannerBlockEntity.applyComponentsFromItemStack(bannerItem);
		}

		return true;
	}
}
