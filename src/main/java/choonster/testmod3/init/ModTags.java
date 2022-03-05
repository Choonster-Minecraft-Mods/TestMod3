package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.versions.forge.ForgeVersion;

import java.util.UUID;

public class ModTags {
	public static class Blocks {
		public static final TagKey<Block> SAPLINGS = tag("saplings");
		public static final TagKey<Block> EMPTY = tag("empty_" + UUID.randomUUID());

		private static TagKey<Block> tag(final String name) {
			return BlockTags.create(new ResourceLocation(TestMod3.MODID, name));
		}
	}

	public static class Items {
		public static final TagKey<Item> VANILLA_DYES = tag("vanilla_dyes");
		public static final TagKey<Item> VANILLA_TERRACOTTA = tag("vanilla_terracotta");

		public static final TagKey<Item> RUBBER = forgeTag("rubber");

		private static TagKey<Item> tag(final String name) {
			return ItemTags.create(new ResourceLocation(TestMod3.MODID, name));
		}

		private static TagKey<Item> forgeTag(final String name) {
			return ItemTags.create(new ResourceLocation(ForgeVersion.MOD_ID, name));
		}
	}
}
