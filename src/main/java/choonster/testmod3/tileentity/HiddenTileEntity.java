package choonster.testmod3.tileentity;

import choonster.testmod3.block.HiddenBlock;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockManager;
import choonster.testmod3.init.ModTileEntities;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;

/**
 * Runs client-side updates for {@link HiddenBlock}.
 *
 * @author Choonster
 */
public class HiddenTileEntity extends TileEntity implements ITickableTileEntity {
	public HiddenTileEntity() {
		super(ModTileEntities.HIDDEN.get());
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			HiddenBlockManager.refresh(world, pos);
		}
	}
}
