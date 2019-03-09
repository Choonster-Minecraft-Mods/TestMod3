package choonster.testmod3.tileentity;

import choonster.testmod3.block.BlockHidden;
import choonster.testmod3.capability.hiddenblockrevealer.HiddenBlockManager;
import choonster.testmod3.init.ModTileEntities;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;

/**
 * Runs client-side updates for {@link BlockHidden}.
 *
 * @author Choonster
 */
public class TileEntityHidden extends TileEntity implements ITickable {
	public TileEntityHidden() {
		super(ModTileEntities.HIDDEN);
	}

	@Override
	public void tick() {
		if (world.isRemote) {
			HiddenBlockManager.refresh(world, pos);
		}
	}
}
