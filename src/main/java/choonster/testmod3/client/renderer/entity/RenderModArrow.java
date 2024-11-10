package choonster.testmod3.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
import net.minecraft.resources.ResourceLocation;

public class RenderModArrow extends TippableArrowRenderer {
	private final ResourceLocation entityTexture;

	public RenderModArrow(final EntityRendererProvider.Context context, final ResourceLocation entityTexture) {
		super(context);
		this.entityTexture = entityTexture;
	}

	@Override
	protected ResourceLocation getTextureLocation(final TippableArrowRenderState renderState) {
		return entityTexture;
	}
}
