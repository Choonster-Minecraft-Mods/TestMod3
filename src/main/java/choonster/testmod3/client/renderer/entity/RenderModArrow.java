package choonster.testmod3.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.TippedArrowRenderer;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.util.ResourceLocation;

public class RenderModArrow extends TippedArrowRenderer {
	private final ResourceLocation entityTexture;

	public RenderModArrow(final EntityRendererManager renderManager, final ResourceLocation entityTexture) {
		super(renderManager);
		this.entityTexture = entityTexture;
	}

	@Override
	protected ResourceLocation getEntityTexture(final ArrowEntity entity) {
		return entityTexture;
	}
}
