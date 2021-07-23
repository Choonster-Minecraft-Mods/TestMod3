package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.ResourceLocation;

public class ModTags {
	public static class Blocks {

	}

	public static class Items {
		public static final ITag.INamedTag<Item> VANILLA_DYES = tag("vanilla_dyes");
		public static final ITag.INamedTag<Item> VANILLA_TERRACOTTA = tag("vanilla_terracotta");

		private static ITag.INamedTag<Item> tag(final String name) {
			return ItemTags.bind(new ResourceLocation(TestMod3.MODID, name).toString());
		}
	}
}
