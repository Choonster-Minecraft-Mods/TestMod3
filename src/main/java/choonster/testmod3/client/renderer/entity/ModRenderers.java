package choonster.testmod3.client.renderer.entity;

import choonster.testmod3.TestMod3;
import choonster.testmod3.entity.EntityModArrow;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

/**
 * Registers this mod's {@link Render}s.
 *
 * @author Choonster
 */
public class ModRenderers {
	public static void register() {
		RenderingRegistry.registerEntityRenderingHandler(EntityModArrow.class, renderManager -> new RenderModArrow(renderManager, new ResourceLocation(TestMod3.MODID, "textures/entity/mod_arrow.png")));
	}
}
