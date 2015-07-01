package com.choonster.testmod3.proxy;


import com.choonster.testmod3.client.cape.CapeEventHandler;
import com.choonster.testmod3.client.util.ModModelManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();

		ModModelManager.INSTANCE.registerFluidModels();
		MinecraftForge.EVENT_BUS.register(new CapeEventHandler());
	}

	@Override
	public void init() {
		super.init();

		ModModelManager.INSTANCE.registerItemModels();
	}
}
