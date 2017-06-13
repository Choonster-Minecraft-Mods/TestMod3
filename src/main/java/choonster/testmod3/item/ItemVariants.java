package choonster.testmod3.item;

import choonster.testmod3.util.IVariant;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An item with multiple variants.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,40652.0.html
 *
 * @author Choonster
 */
public class ItemVariants extends ItemTestMod3 {
	public ItemVariants() {
		super("variants_item");
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return super.getUnlocalizedName(stack) + "." + EnumType.byMetadata(stack.getMetadata()).getName();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void getSubItems(final Item itemIn, final CreativeTabs tab, final NonNullList<ItemStack> subItems) {
		final List<ItemStack> items = Stream.of(EnumType.values())
				.map(enumType -> new ItemStack(itemIn, 1, enumType.getMeta()))
				.collect(Collectors.toList());

		subItems.addAll(items);
	}

	public enum EnumType implements IVariant {
		VARIANT_A(0, "a"),
		VARIANT_B(1, "b"),
		VARIANT_C(2, "c");

		private static final EnumType[] META_LOOKUP = Stream.of(values()).sorted(Comparator.comparing(EnumType::getMeta)).toArray(EnumType[]::new);

		private final int meta;
		private final String name;

		EnumType(final int meta, final String name) {
			this.meta = meta;
			this.name = name;
		}

		public int getMeta() {
			return meta;
		}

		@Override
		public String getName() {
			return name;
		}

		public static EnumType byMetadata(int meta) {
			if (meta < 0 || meta >= META_LOOKUP.length) {
				meta = 0;
			}

			return META_LOOKUP[meta];
		}
	}
}
