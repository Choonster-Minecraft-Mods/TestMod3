package choonster.testmod3.item;

import choonster.testmod3.item.variantgroup.ItemVariantGroup;
import net.minecraft.item.Item;
import net.minecraft.util.IStringSerializable;

/**
 * An item with multiple variants.
 * <p>
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,40652.0.html
 *
 * @author Choonster
 */
public class ItemVariants extends Item {
	private final EnumType type;
	private final ItemVariantGroup<EnumType, ItemVariants> variantGroup;

	public ItemVariants(final EnumType type, final ItemVariantGroup<EnumType, ItemVariants> variantGroup) {
		this.type = type;
		this.variantGroup = variantGroup;
	}

	public enum EnumType implements IStringSerializable {
		VARIANT_A(0, "a"),
		VARIANT_B(1, "b"),
		VARIANT_C(2, "c");

		// TODO: Remove in 1.13
		private final int meta;
		private final String name;

		EnumType(final int meta, final String name) {
			this.meta = meta;
			this.name = name;
		}

		// TODO: Remove in 1.13
		@Deprecated
		public int getMeta() {
			return meta;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
