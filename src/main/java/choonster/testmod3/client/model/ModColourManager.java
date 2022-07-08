package choonster.testmod3.client.model;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.GrassColor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers {@link BlockColor}/{@link ItemColor} handlers for this mod's blocks/items.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModColourManager {

	/**
	 * Register the {@link BlockColor} handlers.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerBlockColourHandlers(final RegisterColorHandlersEvent.Block event) {
		// Use the grass colour of the biome or the default grass colour
		final BlockColor grassColourHandler = (state, blockAccess, pos, tintIndex) -> {
			if (blockAccess != null && pos != null) {
				return BiomeColors.getAverageGrassColor(blockAccess, pos);
			}

			return GrassColor.get(0.5d, 1.0d);
		};

		event.register(grassColourHandler, ModBlocks.WATER_GRASS.get());
	}

	/**
	 * Register the {@link ItemColor} handlers
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerItemColourHandlers(final RegisterColorHandlersEvent.Item event) {
		final BlockColors blockColors = event.getBlockColors();

		// Use the Block's colour handler for an ItemBlock
		final ItemColor itemBlockColourHandler = (stack, tintIndex) -> {
			final BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
			return blockColors.getColor(state, null, null, tintIndex);
		};

		event.register(itemBlockColourHandler, ModBlocks.WATER_GRASS.get());
	}
}
