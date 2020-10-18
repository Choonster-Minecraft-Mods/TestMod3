package choonster.testmod3.block.variantgroup;

import choonster.testmod3.registry.IVariantGroup;
import net.minecraft.block.Block;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.RegistryObject;

import java.util.Collection;

/**
 * A group consisting of a collection of variants with one or more blocks registered for each one.
 *
 * @author Choonster
 */
public interface IBlockVariantGroup<VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block> extends IVariantGroup<VARIANT, Block, BLOCK> {
	/**
	 * Gets this group's blocks.
	 *
	 * @return The blocks
	 */
	default Collection<RegistryObject<BLOCK>> getBlocks() {
		return getEntries();
	}
}
