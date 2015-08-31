package com.choonster.testmod3.compat.nei;

import codechicken.nei.api.API;
import com.choonster.testmod3.Logger;
import com.choonster.testmod3.TestMod3;

public class NEICompat {
	public static void init() {
		Logger.info("Initialising NEI compat");
		API.addItemListEntry(TestMod3.creativeTab.sword);
	}
}
