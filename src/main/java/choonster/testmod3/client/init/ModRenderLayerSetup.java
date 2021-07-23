package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModBlocks;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.Block;
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
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_TANK.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_TANK_RESTRICTED.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.FLUID_PIPE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.MIRROR_PLANE.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.OAK_SAPLING.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.SPRUCE_SAPLING.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.BIRCH_SAPLING.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.JUNGLE_SAPLING.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.ACACIA_SAPLING.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.DARK_OAK_SAPLING.get(), RenderType.cutout());
			ItemBlockRenderTypes.setRenderLayer(ModBlocks.WATER_GRASS.get(), RenderType.cutout());
		});
	}
}
