package choonster.testmod3.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.projectile.Arrow;

public class RenderModArrow extends TippableArrowRenderer {
	private final ResourceLocation entityTexture;

	public RenderModArrow(final EntityRendererProvider.Context context, final ResourceLocation entityTexture) {
		super(context);
		this.entityTexture = entityTexture;
	}

	@Override
	public ResourceLocation getTextureLocation(final Arrow entity) {
		return entityTexture;
	}
}
