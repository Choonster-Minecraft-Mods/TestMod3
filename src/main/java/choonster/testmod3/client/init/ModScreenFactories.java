package choonster.testmod3.client.init;

import choonster.testmod3.api.capability.lock.ILock;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.ClientScreenManager;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.client.gui.LockScreen;
import choonster.testmod3.client.gui.SurvivalCommandBlockEditScreen;
import choonster.testmod3.client.gui.inventory.ModChestScreen;
import choonster.testmod3.init.ModMenuTypes;
import choonster.testmod3.util.CapabilityNotPresentException;
import choonster.testmod3.world.level.block.entity.SurvivalCommandBlockEntity;
import com.google.common.base.Preconditions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Registers this mod's {@link MenuScreens.ScreenConstructor} and {@link ClientScreenManager.IScreenConstructor} implementations.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(bus = Bus.MOD, value = Dist.CLIENT)
public class ModScreenFactories {
	@SubscribeEvent
	public static void registerConstructors(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			registerMenuScreenConstructors();
			registerClientScreenConstructors();
		});
	}

	private static void registerMenuScreenConstructors() {
		MenuScreens.register(ModMenuTypes.CHEST.get(), ModChestScreen::new);
	}

	private static void registerClientScreenConstructors() {
		ClientScreenManager.registerScreenConstructor(GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, (id, additionalData) -> {
			final BlockPos pos = additionalData.readBlockPos();
			final SurvivalCommandBlockEntity blockEntity = getBlockEntity(pos, SurvivalCommandBlockEntity.class);

			return new SurvivalCommandBlockEditScreen(blockEntity);
		});

		ClientScreenManager.registerScreenConstructor(GuiIDs.Client.LOCK, (id, additionalData) -> {
			final ClientLevel world = getClientLevel();

			final BlockPos pos = additionalData.readBlockPos();
			final boolean hasFacing = additionalData.readBoolean();

			final Direction facing;
			if (hasFacing) {
				facing = additionalData.readEnum(Direction.class);
			} else {
				facing = null;
			}

			final ILock lock = LockCapability.getLock(world, pos, facing)
					.orElseThrow(CapabilityNotPresentException::new);

			return new LockScreen(lock, pos, facing);
		});
	}

	@SuppressWarnings("unchecked")
	private static <T extends BlockEntity> T getBlockEntity(final BlockPos pos, final Class<T> blockEntityClass) {
		final ClientLevel level = getClientLevel();

		final BlockEntity blockEntity = level.getBlockEntity(pos);

		Preconditions.checkNotNull(blockEntity, "No BlockEntity found at %s", pos);
		Preconditions.checkState(blockEntityClass.isInstance(blockEntity), "Invalid BlockEntity at %s: expected %s, got %s", pos, blockEntityClass, blockEntity.getClass());

		return (T) blockEntity;
	}

	private static ClientLevel getClientLevel() {
		return Preconditions.checkNotNull(Minecraft.getInstance().level, "Client level is null");
	}
}
