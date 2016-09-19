package choonster.testmod3.proxy;


import choonster.testmod3.client.model.ModColourManager;
import choonster.testmod3.client.render.entity.RenderModArrow;
import choonster.testmod3.entity.EntityModArrow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.client.registry.RenderingRegistry;

import javax.annotation.Nullable;

public class CombinedClientProxy implements IProxy {

	private final Minecraft MINECRAFT = Minecraft.getMinecraft();

	@Override
	public void preInit() {
		RenderingRegistry.registerEntityRenderingHandler(EntityModArrow.class, RenderModArrow::new);
	}

	@Override
	public void init() {
		ModColourManager.registerColourHandlers();
	}

	@Override
	public void postInit() {

	}

	@Override
	public void doClientRightClick() {
		// Press the Use Item keybinding
		KeyBinding.onTick(MINECRAFT.gameSettings.keyBindUseItem.getKeyCode());
	}

	@Nullable
	@Override
	public EntityPlayer getClientPlayer() {
		return MINECRAFT.thePlayer;
	}
}
