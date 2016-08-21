package choonster.testmod3.block.fluid;

import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;

/**
 * A fluid block that displaces portals.
 *
 * Test for this thread:
 * http://www.minecraftforge.net/forum/index.php/topic,41593.0.html
 *
 * @author Choonster
 */
public class BlockFluidPortalDisplacement extends BlockFluidClassic {
	public BlockFluidPortalDisplacement(Fluid fluid, Material material) {
		super(fluid, material);
		displacements.put(Blocks.PORTAL, true);
		displacements.put(Blocks.END_PORTAL, true);
	}
}
