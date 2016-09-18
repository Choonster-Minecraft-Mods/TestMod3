package choonster.testmod3.client.render.entity;

import choonster.testmod3.TestMod3;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.ResourceLocation;

public class RenderModArrow extends RenderTippedArrow {
	private static final ResourceLocation ENTITY_TEXTURE = new ResourceLocation(TestMod3.MODID, "textures/entity/mod_arrow.png");

	public RenderModArrow(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityTippedArrow entity) {
		return ENTITY_TEXTURE;
	}
}
