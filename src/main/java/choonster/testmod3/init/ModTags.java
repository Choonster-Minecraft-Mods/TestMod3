package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.item.Item;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.util.ResourceLocation;

public class ModTags {
	public static class Blocks {

	}

	public static class Items {
		public static final Tag<Item> VANILLA_DYES = tag("vanilla_dyes");
		public static final Tag<Item> VANILLA_TERRACOTTA = tag("vanilla_terracotta");

		private static Tag<Item> tag(final String name) {
			return new ItemTags.Wrapper(new ResourceLocation(TestMod3.MODID, name));
		}
	}
}
