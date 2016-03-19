package com.choonster.testmod3.world.gen;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntityBanner;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
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
		ItemStack bannerStack = new ItemStack(Items.banner);

		NBTTagCompound stackTagCompound = new NBTTagCompound();
		bannerStack.setTagCompound(stackTagCompound);

		NBTTagCompound bannerData = new NBTTagCompound();
		stackTagCompound.setTag("BlockEntityTag", bannerData);

		NBTTagList patternsList = new NBTTagList();
		bannerData.setTag("Patterns", patternsList);
		patternsList.appendTag(createPatternTag(TileEntityBanner.EnumBannerPattern.GRADIENT_UP, EnumDyeColor.MAGENTA));
		patternsList.appendTag(createPatternTag(TileEntityBanner.EnumBannerPattern.FLOWER, EnumDyeColor.BLACK));

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
	protected NBTTagCompound createPatternTag(TileEntityBanner.EnumBannerPattern pattern, EnumDyeColor color) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("Pattern", pattern.getPatternID());
		tag.setInteger("Color", color.getDyeDamage());
		return tag;
	}

	/**
	 * Generate a Banner on top of the topmost block at the specified x and z position
	 *
	 * @param world The world
	 * @param pos   The position
	 */
	private void generateBanner(World world, BlockPos pos) {
		pos = world.getTopSolidOrLiquidBlock(pos);

		world.setBlockState(pos, Blocks.standing_banner.getDefaultState());

		TileEntityBanner tileEntityBanner = (TileEntityBanner) world.getTileEntity(pos);
		tileEntityBanner.setItemValues(bannerStack);
	}

	@Override
	public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
		if (world.provider.isSurfaceWorld() && chunkX % 16 == 0 && chunkZ % 16 == 0) {
			final BlockPos basePos = new BlockPos(chunkX * 16, 0, chunkZ * 16);

			generateBanner(world, basePos);
		}
	}
}
