package choonster.testmod3.capability.lock;

import choonster.testmod3.TestMod3;
import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.wrapper.LockableContainerWrapper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.nbt.INBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.ILockableContainer;
import net.minecraft.world.IWorldReaderBase;
import net.minecraft.world.LockCode;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.Objects;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Capability for {@link ILock}.
 *
 * @author Choonster
 */
public final class CapabilityLock {
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
			public INBTBase writeNBT(final Capability<ILock> capability, final ILock instance, final EnumFacing side) {
				final NBTTagCompound tagCompound = new NBTTagCompound();

				final LockCode lockCode = instance.getLockCode();
				lockCode.write(tagCompound);

				if (instance.hasCustomName()) {
					tagCompound.putString("DisplayName", ITextComponent.Serializer.toJson(instance.getDisplayName()));
				}

				return tagCompound;
			}

			@Override
			public void readNBT(final Capability<ILock> capability, final ILock instance, final EnumFacing side, final INBTBase nbt) {
				if (!(instance instanceof Lock))
					throw new RuntimeException("Can not deserialise to an instance that isn't the default implementation");

				final Lock lock = (Lock) instance;
				final NBTTagCompound tagCompound = (NBTTagCompound) nbt;

				lock.setLockCode(LockCode.read(tagCompound));

				if (tagCompound.contains("DisplayName")) {
					final ITextComponent displayName = Objects.requireNonNull(ITextComponent.Serializer.fromJson(tagCompound.getString("DisplayName")));
					lock.setDisplayName(displayName);
				}
			}
		}, () -> new Lock(new TextComponentTranslation("container.inventory")));
	}

	/**
	 * Get the {@link ILock} from a block.
	 *
	 * @param world The world
	 * @param pos   The position
	 * @param side  The side
	 * @return A lazy optional containing the ILock, or an empty lazy optional if there isn't one
	 */
	public static LazyOptional<ILock> getLock(final IWorldReaderBase world, final BlockPos pos, @Nullable final EnumFacing side) {
		final IBlockState state = world.getBlockState(pos);

		if (state.getBlock().hasTileEntity(state)) {
			final TileEntity tileEntity = world.getTileEntity(pos);
			if (tileEntity != null) {
				final LazyOptional<ILock> optionalLock = tileEntity.getCapability(LOCK_CAPABILITY, side);
				if (optionalLock.isPresent()) {
					return optionalLock;
				} else if (tileEntity instanceof ILockableContainer) {
					return LazyOptional.of(() -> new LockableContainerWrapper((ILockableContainer) tileEntity));
				}
			}
		}

		return LazyOptional.empty();
	}
}
