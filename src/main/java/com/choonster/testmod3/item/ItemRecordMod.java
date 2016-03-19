package com.choonster.testmod3.item;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.util.Constants;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemRecord;
import net.minecraft.util.ResourceLocation;

/**
 * A record.
 * <p>
 * Test for this thread:
 * http://www.minecraftforum.net/forums/mapping-and-modding/minecraft-mods/modification-development/2408066-try-creating-a-music-disc-in-my-1-8-mod-please
 *
 * @author Choonster
 */
public class ItemRecordMod extends ItemRecord {
	public ItemRecordMod(String recordName) {
		// TODO: Use proper SoundEvent when Forge gets sounds working
		super(Constants.RESOURCE_PREFIX + recordName, SoundEvents.record_13);
		setRegistryName("record_" + recordName);
		setUnlocalizedName("record");
		setCreativeTab(TestMod3.creativeTab);
	}

	@Override
	public ResourceLocation getRecordResource(String name) {
		return new ResourceLocation(TestMod3.MODID, name);
	}
}
