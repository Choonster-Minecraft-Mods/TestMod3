package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.RenderTypeLookup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Sets the render layers for this mod's {@link Block}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModRenderLayerSetup {
	@SubscribeEvent
	public static void setRenderLayers(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_TANK.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_TANK_RESTRICTED.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.FLUID_PIPE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.MIRROR_PLANE.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.OAK_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.SPRUCE_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.BIRCH_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.JUNGLE_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.ACACIA_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.DARK_OAK_SAPLING.get(), RenderType.getCutout());
			RenderTypeLookup.setRenderLayer(ModBlocks.WATER_GRASS.get(), RenderType.getCutout());
		});
	}
}
