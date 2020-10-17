package choonster.testmod3.client.renderer.entity;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModEntities;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Registers this mod's {@link EntityRenderer}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModRenderers {
	@SubscribeEvent
	public static void register(final FMLClientSetupEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.MOD_ARROW, renderManager -> new RenderModArrow(renderManager, new ResourceLocation(TestMod3.MODID, "textures/entity/arrow.png")));
		RenderingRegistry.registerEntityRenderingHandler(ModEntities.BLOCK_DETECTION_ARROW, renderManager -> new RenderModArrow(renderManager, new ResourceLocation(TestMod3.MODID, "textures/entity/block_detection_arrow.png")));
	}
}
