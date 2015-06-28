package com.choonster.testmod3.proxy;


import com.choonster.testmod3.client.util.ModModelManager;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit() {
		super.preInit();

		ModModelManager.INSTANCE.registerFluidModels();
	}

	@Override
	public void init() {
		super.init();

		ModModelManager.INSTANCE.registerItemModels();
	}
}
