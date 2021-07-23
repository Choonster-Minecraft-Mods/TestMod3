package choonster.testmod3.world.gen.feature;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import net.minecraft.block.BannerBlock;
import net.minecraft.item.DyeColor;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.ISeedReader;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Random;

/**
 * Generates Banners with a specific pattern.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class BannerFeature extends Feature<BannerFeatureConfig> {
	private static final Field ITEM_PATTERNS = ObfuscationReflectionHelper.findField(BannerTileEntity.class, /* itemPatterns */ "field_175118_f");
	private static final Field BASE_COLOR = ObfuscationReflectionHelper.findField(BannerTileEntity.class, /* baseColor */ "field_175120_a");
	private static final Field PATTERNS = ObfuscationReflectionHelper.findField(BannerTileEntity.class, /* patterns */ "field_175122_h");
	private static final Field RECEIVED_DATA = ObfuscationReflectionHelper.findField(BannerTileEntity.class, /* receivedData */ "field_175119_g");
	private static final Field NAME = ObfuscationReflectionHelper.findField(BannerTileEntity.class, /* name */ "field_190617_a");

	public BannerFeature(final Codec<BannerFeatureConfig> codec) {
		super(codec);
	}

	/**
	 * Creates a list of pattern tags to apply to the generated banners.
	 *
	 * @return A list tag containing the patterns
	 */
	protected ListNBT createPatternList(final List<Pair<BannerPattern, DyeColor>> patterns) {
		final ListNBT patternList = new ListNBT();

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
	protected CompoundNBT createPatternTag(final BannerPattern pattern, final DyeColor color) {
		final CompoundNBT tag = new CompoundNBT();
		tag.putString("Pattern", pattern.getHashname());
		tag.putInt("Color", color.getId());
		return tag;
	}

	@Override
	public boolean place(final ISeedReader world, final ChunkGenerator generator, final Random rand, final BlockPos pos, final BannerFeatureConfig config) {
		world.setBlock(pos, BannerBlock.byColor(config.getColor()).defaultBlockState(), Constants.BlockFlags.DEFAULT);

		final TileEntity tileEntity = world.getBlockEntity(pos);
		if (tileEntity instanceof BannerTileEntity) {
			loadFromItemStack((BannerTileEntity) tileEntity, createPatternList(config.getPatterns()), DyeColor.PINK);
		}

		return true;
	}

	// Adapted from BannerTileEntity.fromItem, which is client-only
	private static void loadFromItemStack(final BannerTileEntity tileEntity, final ListNBT patterns, final DyeColor color) {
		try {
			ITEM_PATTERNS.set(tileEntity, patterns);
			BASE_COLOR.set(tileEntity, color);
			PATTERNS.set(tileEntity, null);
			RECEIVED_DATA.set(tileEntity, true);
			NAME.set(tileEntity, null);
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to add banner data to TileEntity", e);
		}
	}
}
