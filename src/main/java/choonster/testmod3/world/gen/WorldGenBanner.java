package choonster.testmod3.world.gen;

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
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

/**
 * Generates Banners with a specific pattern in chunks with coordinates divisible by 16. Only generates in dimensions that return true from {@link WorldProvider#isSurfaceWorld}.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2535868-banner-nbt-tags
 *
 * @author Choonster
 */
public class WorldGenBanner implements IWorldGenerator {
	private final ItemStack bannerStack = createBannerStack();

	/**
	 * Create the Banner ItemStack used as a template for the generated banners.
	 *
	 * @return A Banner ItemStack with the appropriate NBT data
	 */
	protected ItemStack createBannerStack() {
		final ItemStack bannerStack = new ItemStack(Items.BANNER);

		final NBTTagCompound bannerData = bannerStack.getOrCreateSubCompound("BlockEntityTag");

		final NBTTagList patternsList = new NBTTagList();
		bannerData.setTag("Patterns", patternsList);
		patternsList.appendTag(createPatternTag(BannerPattern.GRADIENT_UP, EnumDyeColor.MAGENTA));
		patternsList.appendTag(createPatternTag(BannerPattern.FLOWER, EnumDyeColor.BLACK));

		bannerData.setInteger("Base", EnumDyeColor.PINK.getDyeDamage());

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
		tag.setString("Pattern", pattern.getHashname());
		tag.setInteger("Color", color.getDyeDamage());
		return tag;
	}

	/**
	 * Generate a Banner on top of the topmost block at the specified x and z position
	 *
	 * @param world The world
	 * @param pos   The position
	 */
	private void generateBanner(final World world, final BlockPos pos) {
		final BlockPos newPos = world.getTopSolidOrLiquidBlock(pos);

		world.setBlockState(newPos, Blocks.STANDING_BANNER.getDefaultState());

		final TileEntity tileEntity = world.getTileEntity(newPos);
		if (tileEntity instanceof TileEntityBanner) {
			((TileEntityBanner) tileEntity).setItemValues(bannerStack, true); // Read the base colour from NBT rather than metadata
		}
	}

	@Override
	public void generate(final Random random, final int chunkX, final int chunkZ, final World world, final IChunkGenerator chunkGenerator, final IChunkProvider chunkProvider) {
		if (world.provider.isSurfaceWorld() && chunkX % 16 == 0 && chunkZ % 16 == 0) {
			final BlockPos basePos = new BlockPos(chunkX * 16, 0, chunkZ * 16);

			generateBanner(world, basePos);
		}
	}
}
