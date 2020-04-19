package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.lock.LockCapability;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.client.gui.GuiSurvivalCommandBlock;
import choonster.testmod3.client.gui.LockScreen;
import choonster.testmod3.network.OpenClientScreenMessage;
import choonster.testmod3.tileentity.SurvivalCommandBlockTileEntity;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * Handles this mod's client-side GUI factories
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModGuiFactories {
	private static Map<ResourceLocation, Function<OpenClientScreenMessage, Screen>> CLIENT_GUI_FACTORIES;

	/**
	 * Returned from factories to indicate that no GUI should be shown.
	 * <p>
	 * This is required because {@link LazyOptional#orElse(Object)} doesn't allow nulls.
	 */
	private static final Screen NULL_SCREEN = new Screen(new StringTextComponent("")) {
	};

	@Nullable
	public static Screen getClientScreen(final OpenClientScreenMessage message) {
		final Function<OpenClientScreenMessage, Screen> factory = CLIENT_GUI_FACTORIES.get(message.getId());
		final Screen screen = factory != null ? factory.apply(message) : null;
		return screen == NULL_SCREEN ? null : screen;
	}

	@SubscribeEvent
	public static void registerClientGuiFactories(final FMLClientSetupEvent event) {
		CLIENT_GUI_FACTORIES = ImmutableMap.<ResourceLocation, Function<OpenClientScreenMessage, Screen>>builder()
				.put(GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, message -> {
					final BlockPos pos = message.getAdditionalData().readBlockPos();
					final SurvivalCommandBlockTileEntity tileEntity = getTileEntity(pos, SurvivalCommandBlockTileEntity.class);

					return new GuiSurvivalCommandBlock(tileEntity);
				})
				.put(GuiIDs.Client.LOCK, message -> {
					final BlockPos pos = message.getAdditionalData().readBlockPos();
					final boolean hasFacing = message.getAdditionalData().readBoolean();

					final Direction facing;
					if (hasFacing) {
						facing = message.getAdditionalData().readEnumValue(Direction.class);
					} else {
						facing = null;
					}

					return LockCapability.getLock(Minecraft.getInstance().world, pos, facing)
							.<Screen>map(lock -> new LockScreen(lock, pos, facing))
							.orElse(NULL_SCREEN);
				})
				.build();
	}

	@SuppressWarnings("unchecked")
	private static <T extends TileEntity> T getTileEntity(final BlockPos pos, final Class<T> tileEntityClass) {
		final TileEntity tileEntity = Minecraft.getInstance().world.getTileEntity(pos);
		Preconditions.checkNotNull(tileEntity, "No TileEntity found at %s", pos);
		Preconditions.checkState(tileEntityClass.isInstance(tileEntity), "Invalid TileEntity at %s: expected %s, got %s", pos, tileEntityClass, tileEntity.getClass());
		return (T) tileEntity;
	}
}
