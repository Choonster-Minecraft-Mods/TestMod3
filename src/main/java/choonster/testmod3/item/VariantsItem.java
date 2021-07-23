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
public class VariantsItem extends Item {
	private final Type type;
	private final ItemVariantGroup<Type, VariantsItem> variantGroup;

	public VariantsItem(final Type type, final ItemVariantGroup<Type, VariantsItem> variantGroup, final Item.Properties properties) {
		super(properties);
		this.type = type;
		this.variantGroup = variantGroup;
	}

	public Type getType() {
		return type;
	}

	public ItemVariantGroup<Type, VariantsItem> getVariantGroup() {
		return variantGroup;
	}

	public enum Type implements IStringSerializable {
		VARIANT_A("a"),
		VARIANT_B("b"),
		VARIANT_C("c");

		private final String name;

		Type(final String name) {
			this.name = name;
		}

		@Override
		public String getSerializedName() {
			return name;
		}
	}
}
