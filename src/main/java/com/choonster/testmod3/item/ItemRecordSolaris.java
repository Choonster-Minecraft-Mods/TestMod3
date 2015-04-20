package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;

// http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
public class ItemRecordSolaris extends ItemRecord {
	public ItemRecordSolaris() {
		super("solaris");
		setCreativeTab(TestMod3.creativeTab);
		setUnlocalizedName("record");
	}

	@Override
	public ResourceLocation getRecordResource(String name) {
		return new ResourceLocation(TestMod3.MODID, name);
	}
}
