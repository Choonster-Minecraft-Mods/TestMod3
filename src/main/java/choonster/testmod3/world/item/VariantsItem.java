package choonster.testmod3.world.item;

import choonster.testmod3.world.item.variantgroup.ItemVariantGroup;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;

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

	public enum Type implements StringRepresentable {
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
