package choonster.testmod3.capability.lock;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.wrapper.LockableTileEntityWrapper;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.tileentity.LockableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
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

	public static void register() {
		CapabilityManager.INSTANCE.register(ILock.class, new Capability.IStorage<ILock>() {
			@Override
			public INBT writeNBT(final Capability<ILock> capability, final ILock instance, final Direction side) {
				final CompoundNBT tagCompound = new CompoundNBT();

				final LockCode lockCode = instance.getLockCode();
				lockCode.write(tagCompound);

				return tagCompound;
			}

			@Override
			public void readNBT(final Capability<ILock> capability, final ILock instance, final Direction side, final INBT nbt) {
				if (!(instance instanceof Lock))
					throw new RuntimeException("Can not deserialise to an instance that isn't the default implementation");

				final Lock lock = (Lock) instance;
				final CompoundNBT tagCompound = (CompoundNBT) nbt;

				lock.setLockCode(LockCode.read(tagCompound));
			}
		}, () -> new Lock(() -> new TranslationTextComponent("container.inventory")));
	}

	/**
	 * Get the {@link ILock} from a block.
	 *
	 * @param world The world
	 * @param pos   The position
	 * @param side  The side
	 * @return A lazy optional containing the ILock, or an empty lazy optional if there isn't one
	 */
	public static LazyOptional<ILock> getLock(final IWorldReader world, final BlockPos pos, @Nullable final Direction side) {
		final BlockState state = world.getBlockState(pos);

		if (state.getBlock().hasTileEntity(state)) {
			final TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity != null) {
				final LazyOptional<ILock> optionalLock = tileEntity.getCapability(LOCK_CAPABILITY, side);
				if (optionalLock.isPresent()) {
					return optionalLock;
				} else if (tileEntity instanceof LockableTileEntity) {
					return LazyOptional.of(() -> new LockableTileEntityWrapper((LockableTileEntity) tileEntity));
				}
			}
		}

		return LazyOptional.empty();
	}
}
