package choonster.testmod3.client.model;

import choonster.testmod3.init.ModBlocks;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.color.ItemColors;
import net.minecraft.item.ItemBlock;
import net.minecraft.world.ColorizerGrass;
import net.minecraft.world.biome.BiomeColorHelper;

/**
 * Registers {@link IBlockColor}/{@link IItemColor} handlers for this mod's blocks/items.
 *
 * @author Choonster
 */
public class ModColourManager {
	private static final Minecraft MINECRAFT = Minecraft.getMinecraft();

	/**
	 * Register the colour handlers.
	 */
	public static void registerColourHandlers() {
		final BlockColors blockColors = MINECRAFT.getBlockColors();
		final ItemColors itemColors = MINECRAFT.getItemColors();

		registerBlockColourHandlers(blockColors);
		registerItemColourHandlers(blockColors, itemColors);
	}

	/**
	 * Register the {@link IBlockColor} handlers.
	 *
	 * @param blockColors The BlockColors instance
	 */
	private static void registerBlockColourHandlers(final BlockColors blockColors) {
		// Use the grass colour of the biome or the default grass colour
		final IBlockColor grassColourHandler = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null) {
				return BiomeColorHelper.getGrassColorAtPos(blockAccess, pos);
			}

			return ColorizerGrass.getGrassColor(0.5D, 1.0D);
		};

		blockColors.registerBlockColorHandler(grassColourHandler, ModBlocks.WATER_GRASS);
	}

	/**
	 * Register the {@link IItemColor} handlers
	 *
	 * @param blockColors The BlockColors instance
	 * @param itemColors  The ItemColors instance
	 */
	private static void registerItemColourHandlers(final BlockColors blockColors, final ItemColors itemColors) {
		// Use the Block's colour handler for an ItemBlock
		final IItemColor itemBlockColourHandler = (stack, tintIndex) -> {
			@SuppressWarnings("deprecation")
			final IBlockState state = ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getMetadata());
			return blockColors.colorMultiplier(state, null, null, tintIndex);
		};

		itemColors.registerItemColorHandler(itemBlockColourHandler, ModBlocks.WATER_GRASS);
	}
}
