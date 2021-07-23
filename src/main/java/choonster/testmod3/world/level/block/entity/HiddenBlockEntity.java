package choonster.testmod3.world.level.block.entity;

import choonster.testmod3.client.capability.HiddenBlockManager;
import choonster.testmod3.init.ModBlockEntities;
import choonster.testmod3.world.level.block.HiddenBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

/**
 * Runs client-side updates for {@link HiddenBlock}.
 *
 * @author Choonster
 */
public class HiddenBlockEntity extends BlockEntity {
	public HiddenBlockEntity(final BlockPos pos, final BlockState state) {
		super(ModBlockEntities.HIDDEN.get(), pos, state);
	}

	public static void tick(final Level level, final BlockPos pos, final BlockState state, final HiddenBlockEntity blockEntity) {
		if (level.isClientSide) {
			DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> HiddenBlockManager.refresh(level, pos));
		}
	}
}
