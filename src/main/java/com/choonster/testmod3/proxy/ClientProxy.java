package com.choonster.testmod3.proxy;


import com.choonster.testmod3.client.util.ModModelManager;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();

		ModModelManager.INSTANCE.registerVariants();
		MinecraftForge.EVENT_BUS.register(ModModelManager.INSTANCE);
	}

	@Override
	public void init() {
		super.init();

		ModModelManager.INSTANCE.registerItemModels();
	}
}
