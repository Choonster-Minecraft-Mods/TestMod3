package choonster.testmod3.client.init;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.ClientScreenManager;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.client.gui.GuiSurvivalCommandBlock;
import choonster.testmod3.client.gui.LockScreen;
import choonster.testmod3.client.gui.inventory.ModChestScreen;
import choonster.testmod3.init.ModContainerTypes;
import choonster.testmod3.tileentity.SurvivalCommandBlockTileEntity;
import choonster.testmod3.util.CapabilityNotPresentException;
import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Registers this mod's {@link ScreenManager.IScreenFactory} and {@link ClientScreenManager.IScreenFactory} implementations.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class ModScreenFactories {
	@SubscribeEvent
	public static void registerFactories(final FMLClientSetupEvent event) {
		registerContainerScreenFactories();
		registerClientScreenFactories();
	}

	private static void registerContainerScreenFactories() {
		ScreenManager.registerFactory(ModContainerTypes.CHEST.get(), ModChestScreen::new);
	}

	private static void registerClientScreenFactories() {
		ClientScreenManager.registerScreenFactory(GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, (id, additionalData) -> {
			final BlockPos pos = additionalData.readBlockPos();
			final SurvivalCommandBlockTileEntity tileEntity = getTileEntity(pos, SurvivalCommandBlockTileEntity.class);

			return new GuiSurvivalCommandBlock(tileEntity);
		});

		ClientScreenManager.registerScreenFactory(GuiIDs.Client.LOCK, (id, additionalData) -> {
			final ClientWorld world = getClientWorld();

			final BlockPos pos = additionalData.readBlockPos();
			final boolean hasFacing = additionalData.readBoolean();

			final Direction facing;
			if (hasFacing) {
				facing = additionalData.readEnumValue(Direction.class);
			} else {
				facing = null;
			}

			final ILock lock = LockCapability.getLock(world, pos, facing)
					.orElseThrow(CapabilityNotPresentException::new);

			return new LockScreen(lock, pos, facing);
		});
	}

	@SuppressWarnings("unchecked")
	private static <T extends TileEntity> T getTileEntity(final BlockPos pos, final Class<T> tileEntityClass) {
		final ClientWorld world = getClientWorld();

		final TileEntity tileEntity = world.getTileEntity(pos);

		Preconditions.checkNotNull(tileEntity, "No TileEntity found at %s", pos);
		Preconditions.checkState(tileEntityClass.isInstance(tileEntity), "Invalid TileEntity at %s: expected %s, got %s", pos, tileEntityClass, tileEntity.getClass());

		return (T) tileEntity;
	}

	private static ClientWorld getClientWorld() {
		return Preconditions.checkNotNull(Minecraft.getInstance().world, "Client world is null");
	}
}
