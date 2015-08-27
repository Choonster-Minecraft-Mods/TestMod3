package com.choonster.testmod3.proxy;


import com.choonster.testmod3.client.cape.CapeEventHandler;
import com.choonster.testmod3.client.util.ModModelManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;

public class CombinedClientProxy extends CommonProxy {

	private final Minecraft minecraft = Minecraft.getMinecraft();

	@Override
	public void preInit() {
		super.preInit();

		ModModelManager.INSTANCE.registerAllModels();
		MinecraftForge.EVENT_BUS.register(new CapeEventHandler());
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
