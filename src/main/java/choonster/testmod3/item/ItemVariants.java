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

	public ItemVariants(final EnumType type, final ItemVariantGroup<EnumType, ItemVariants> variantGroup, final Item.Properties properties) {
		super(properties);
		this.type = type;
		this.variantGroup = variantGroup;
	}

	public enum EnumType implements IStringSerializable {
		VARIANT_A("a"),
		VARIANT_B("b"),
		VARIANT_C("c");

		private final String name;

		EnumType(final String name) {
			this.name = name;
		}

		@Override
		public String getName() {
			return name;
		}
	}
}
