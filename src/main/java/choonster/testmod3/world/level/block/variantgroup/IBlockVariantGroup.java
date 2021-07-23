package choonster.testmod3.world.level.block.variantgroup;

import choonster.testmod3.registry.IVariantGroup;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fmllegacy.RegistryObject;

import java.util.Collection;

/**
 * A group consisting of a collection of variants with one or more blocks registered for each one.
 *
 * @author Choonster
 */
public interface IBlockVariantGroup<VARIANT extends Enum<VARIANT> & StringRepresentable, BLOCK extends Block> extends IVariantGroup<VARIANT, Block, BLOCK> {
	/**
	 * Gets this group's blocks.
	 *
	 * @return The blocks
	 */
	default Collection<RegistryObject<BLOCK>> getBlocks() {
		return getEntries();
	}
}
