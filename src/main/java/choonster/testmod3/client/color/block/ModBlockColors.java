package choonster.testmod3.client.color.block;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.world.level.GrassColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers {@link BlockColor} handlers for this mod's blocks.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModBlockColors {
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

			return GrassColor.getDefaultColor();
		};

		event.register(grassColourHandler, ModBlocks.WATER_GRASS.get());
	}
}
