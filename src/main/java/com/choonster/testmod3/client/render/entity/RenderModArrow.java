package com.choonster.testmod3.client.render.entity;

import com.choonster.testmod3.TestMod3;
import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderModArrow extends RenderArrow {
	public static final ResourceLocation ENTITY_TEXTURE = new ResourceLocation(TestMod3.MODID, "textures/entity/modArrow.png");

	public RenderModArrow(RenderManager renderManager) {
		super(renderManager);
	}

	@Override
	protected ResourceLocation getEntityTexture(EntityArrow p_180550_1_) {
		return ENTITY_TEXTURE;
	}
}
