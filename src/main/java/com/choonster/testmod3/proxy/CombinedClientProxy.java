package com.choonster.testmod3.proxy;


import com.choonster.testmod3.client.cape.CapeEventHandler;
import com.choonster.testmod3.client.event.ClientEventHandler;
import com.choonster.testmod3.client.model.ModModelManager;
import com.choonster.testmod3.client.render.entity.RenderModArrow;
import com.choonster.testmod3.entity.EntityModArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CombinedClientProxy extends CommonProxy {

	private final Minecraft minecraft = Minecraft.getMinecraft();

	@Override
	public void preInit() {
		super.preInit();

		ModModelManager.INSTANCE.registerAllModels();
		MinecraftForge.EVENT_BUS.register(new CapeEventHandler());
		MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
	}

	@Override
	public void init() {
		super.init();

		RenderManager renderManager = Minecraft.getMinecraft().getRenderManager();
		RenderingRegistry.registerEntityRenderingHandler(EntityModArrow.class, new RenderModArrow(renderManager));
	}

	@Override
	public void doClientRightClick() {
		super.doClientRightClick();

		// Press the Use Item keybinding
		KeyBinding.onTick(minecraft.gameSettings.keyBindUseItem.getKeyCode());
	}

	@Override
	public EntityPlayer getClientPlayer() {
		return minecraft.thePlayer;
	}
}
