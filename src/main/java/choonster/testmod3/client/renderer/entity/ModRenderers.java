package choonster.testmod3.client.renderer.entity;

import choonster.testmod3.TestMod3;
import choonster.testmod3.init.ModEntities;
import net.minecraft.client.renderer.entity.CreeperRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link EntityRenderer}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModRenderers {
	@SubscribeEvent
	public static void register(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ModEntities.MOD_ARROW.get(), context -> new RenderModArrow(context, new ResourceLocation(TestMod3.MODID, "textures/entity/arrow.png")));
		event.registerEntityRenderer(ModEntities.BLOCK_DETECTION_ARROW.get(), renderManager -> new RenderModArrow(renderManager, new ResourceLocation(TestMod3.MODID, "textures/entity/block_detection_arrow.png")));
		event.registerEntityRenderer(ModEntities.PLAYER_AVOIDING_CREEPER.get(), CreeperRenderer::new);
	}
}
