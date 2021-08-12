package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.Tag;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public class ModTags {
	public static class Blocks {
		public static final Tag.Named<Block> SAPLINGS = tag("saplings");

		private static Tag.Named<Block> tag(final String name) {
			return BlockTags.bind(new ResourceLocation(TestMod3.MODID, name).toString());
		}
	}

	public static class Items {
		public static final Tag.Named<Item> VANILLA_DYES = tag("vanilla_dyes");
		public static final Tag.Named<Item> VANILLA_TERRACOTTA = tag("vanilla_terracotta");

		private static Tag.Named<Item> tag(final String name) {
			return ItemTags.bind(new ResourceLocation(TestMod3.MODID, name).toString());
		}
	}
}
