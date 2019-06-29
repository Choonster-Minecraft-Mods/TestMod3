package choonster.testmod3.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.BannerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraftforge.common.util.Constants;

import java.util.Random;
import java.util.function.Function;

/**
 * Generates Banners with a specific pattern.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class BannerFeature extends Feature<NoFeatureConfig> {
	private final ItemStack bannerStack = createBannerStack();

	public BannerFeature(final Function<Dynamic<?>, ? extends NoFeatureConfig> configFactory) {
		super(configFactory);
	}

	/**
	 * Create the Banner ItemStack used as a template for the generated banners.
	 *
	 * @return A Banner ItemStack with the appropriate NBT data
	 */
	protected ItemStack createBannerStack() {
		final ItemStack bannerStack = new ItemStack(Items.PINK_BANNER);

		final CompoundNBT bannerData = bannerStack.getOrCreateChildTag("BlockEntityTag");

		final ListNBT patternsList = new ListNBT();
		bannerData.put("Patterns", patternsList);
		patternsList.add(createPatternTag(BannerPattern.GRADIENT_UP, DyeColor.MAGENTA));
		patternsList.add(createPatternTag(BannerPattern.FLOWER, DyeColor.BLACK));

		return bannerStack;
	}

	/**
	 * Create a compound tag for the specified pattern and colour.
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
	public boolean place(final IWorld world, final ChunkGenerator<? extends GenerationSettings> chunkGenerator, final Random random, final BlockPos pos, final NoFeatureConfig featureConfig) {
		world.setBlockState(pos, Blocks.PINK_BANNER.getDefaultState(), Constants.BlockFlags.DEFAULT);

		final TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof BannerTileEntity) {
			((BannerTileEntity) tileEntity).loadFromItemStack(bannerStack, DyeColor.PINK);
		}

		return true;
	}
}
