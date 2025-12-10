package choonster.testmod3.capability.lock;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.wrapper.BaseContainerBlockEntityWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

/**
 * Capability for {@link ILock}.
 *
 * @author Choonster
 */
public final class LockCapability {
	/**
	 * The {@link Capability} instance.
	 */
	public static final Capability<ILock> LOCK_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	/**
	 * The ID of this capability.
	 */
	public static final Identifier ID = Identifier.fromNamespaceAndPath(TestMod3.MODID, "lock");

	/**
	 * Get the {@link ILock} from a block.
	 *
	 * @param world The world
	 * @param pos   The position
	 * @param side  The side
	 * @return A lazy optional containing the ILock, or an empty lazy optional if there isn't one
	 */
	public static LazyOptional<ILock> getLock(final LevelReader world, final BlockPos pos, @Nullable final Direction side) {
		final var state = world.getBlockState(pos);

		if (state.getBlock() instanceof EntityBlock) {
			final var blockEntity = world.getBlockEntity(pos);
			if (blockEntity != null) {
				final var optionalLock = blockEntity.getCapability(LOCK_CAPABILITY, side);
				if (optionalLock.isPresent()) {
					return optionalLock;
				} else if (blockEntity instanceof final BaseContainerBlockEntity baseContainerBlockEntity) {
					return LazyOptional.of(() -> new BaseContainerBlockEntityWrapper(baseContainerBlockEntity));
				}
			}
		}

		return LazyOptional.empty();
	}
}
