package choonster.testmod3.capability.lock;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.wrapper.BaseContainerBlockEntityWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability for {@link ILock}.
 *
 * @author Choonster
 */
public final class LockCapability {
	/**
	 * The {@link Capability} instance.
	 */
	@CapabilityInject(ILock.class)
	public static final Capability<ILock> LOCK_CAPABILITY = Null();

	/**
	 * The ID of this capability.
	 */
	public static final ResourceLocation ID = new ResourceLocation(TestMod3.MODID, "lock");

	public static void register(final RegisterCapabilitiesEvent event) {
		event.register(ILock.class);
	}

	/**
	 * Get the {@link ILock} from a block.
	 *
	 * @param world The world
	 * @param pos   The position
	 * @param side  The side
	 * @return A lazy optional containing the ILock, or an empty lazy optional if there isn't one
	 */
	public static LazyOptional<ILock> getLock(final LevelReader world, final BlockPos pos, @Nullable final Direction side) {
		final BlockState state = world.getBlockState(pos);

		if (state.getBlock() instanceof EntityBlock) {
			final BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity != null) {
				final LazyOptional<ILock> optionalLock = blockEntity.getCapability(LOCK_CAPABILITY, side);
				if (optionalLock.isPresent()) {
					return optionalLock;
				} else if (blockEntity instanceof BaseContainerBlockEntity) {
					return LazyOptional.of(() -> new BaseContainerBlockEntityWrapper((BaseContainerBlockEntity) blockEntity));
				}
			}
		}

		return LazyOptional.empty();
	}
}
