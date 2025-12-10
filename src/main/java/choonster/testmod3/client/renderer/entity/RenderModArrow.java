package choonster.testmod3.client.renderer.entity;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.TippableArrowRenderer;
import net.minecraft.client.renderer.entity.state.TippableArrowRenderState;
import net.minecraft.resources.Identifier;

public class RenderModArrow extends TippableArrowRenderer {
	private final Identifier entityTexture;

	public RenderModArrow(final EntityRendererProvider.Context context, final Identifier entityTexture) {
		super(context);
		this.entityTexture = entityTexture;
	}

	@Override
	protected Identifier getTextureLocation(final TippableArrowRenderState renderState) {
		return entityTexture;
	}
}
