package choonster.testmod3.world.gen.feature;

import choonster.testmod3.util.Constants;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.BannerPattern;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.IChunkGenSettings;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;

import java.util.Random;

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

	/**
	 * Create the Banner ItemStack used as a template for the generated banners.
	 *
	 * @return A Banner ItemStack with the appropriate NBT data
	 */
	protected ItemStack createBannerStack() {
		final ItemStack bannerStack = new ItemStack(Items.PINK_BANNER);

		final NBTTagCompound bannerData = bannerStack.getOrCreateChildTag("BlockEntityTag");

		final NBTTagList patternsList = new NBTTagList();
		bannerData.put("Patterns", patternsList);
		patternsList.add(createPatternTag(BannerPattern.GRADIENT_UP, EnumDyeColor.MAGENTA));
		patternsList.add(createPatternTag(BannerPattern.FLOWER, EnumDyeColor.BLACK));

		return bannerStack;
	}

	/**
	 * Create a compound tag for the specified pattern and colour.
	 *
	 * @param pattern The pattern
	 * @param color   The colour
	 * @return The compound tag
	 */
	protected NBTTagCompound createPatternTag(final BannerPattern pattern, final EnumDyeColor color) {
		final NBTTagCompound tag = new NBTTagCompound();
		tag.putString("Pattern", pattern.getHashname());
		tag.putInt("Color", color.getId());
		return tag;
	}

	@Override
	public boolean place(final IWorld world, final IChunkGenerator<? extends IChunkGenSettings> chunkGenerator, final Random random, final BlockPos pos, final NoFeatureConfig featureConfig) {
		world.setBlockState(pos, Blocks.PINK_BANNER.getDefaultState(), Constants.BlockFlags.DEFAULT_FLAGS);

		final TileEntity tileEntity = world.getTileEntity(pos);
		if (tileEntity instanceof TileEntityBanner) {
			((TileEntityBanner) tileEntity).loadFromItemStack(bannerStack, EnumDyeColor.PINK);
		}

		return true;
	}
}
