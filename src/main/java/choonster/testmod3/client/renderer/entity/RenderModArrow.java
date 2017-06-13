package choonster.testmod3.client.renderer.entity;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.entity.projectile.EntityTippedArrow;
import net.minecraft.util.ResourceLocation;

public class RenderModArrow extends RenderTippedArrow {
	private final ResourceLocation entityTexture;

	public RenderModArrow(final RenderManager renderManager, final ResourceLocation entityTexture) {
		super(renderManager);
		this.entityTexture = entityTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityTippedArrow entity) {
		return entityTexture;
	}
}
