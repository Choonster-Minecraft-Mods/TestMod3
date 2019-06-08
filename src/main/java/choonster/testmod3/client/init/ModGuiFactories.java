package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.lock.CapabilityLock;
import choonster.testmod3.client.gui.GuiIDs;
import choonster.testmod3.client.gui.GuiLock;
import choonster.testmod3.client.gui.GuiSurvivalCommandBlock;
import choonster.testmod3.client.gui.inventory.GuiModChest;
import choonster.testmod3.network.MessageOpenClientGui;
import choonster.testmod3.tileentity.TileEntityModChest;
import choonster.testmod3.tileentity.TileEntitySurvivalCommandBlock;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ExtensionPoint;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.network.FMLPlayMessages;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;

/**
 * Registers this mod's GUI factories with {@link ExtensionPoint#GUIFACTORY}.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModGuiFactories {
	private static Map<ResourceLocation, Function<MessageOpenClientGui, GuiScreen>> CLIENT_GUI_FACTORIES;

	/**
	 * Returned from factories to indicate that no GUI should be shown.
	 * <p>
	 * This is required because {@link LazyOptional#orElse(Object)} doesn't allow nulls.
	 */
	private static final GuiScreen NULL_GUI = new GuiScreen() {
	};

	@Nullable
	public static GuiScreen getClientGui(final MessageOpenClientGui message) {
		final Function<MessageOpenClientGui, GuiScreen> factory = CLIENT_GUI_FACTORIES.get(message.getId());
		final GuiScreen gui = factory != null ? factory.apply(message) : null;
		return gui == NULL_GUI ? null : gui;
	}

	@SubscribeEvent
	public static void registerContainerGuiFactories(final FMLClientSetupEvent event) {
		final Map<ResourceLocation, Function<FMLPlayMessages.OpenContainer, GuiScreen>> factories = ImmutableMap.<ResourceLocation, Function<FMLPlayMessages.OpenContainer, GuiScreen>>builder()
				.put(GuiIDs.Container.MOD_CHEST, message -> {
					final BlockPos pos = message.getAdditionalData().readBlockPos();
					final EntityPlayerSP player = getPlayer();
					final TileEntityModChest tileEntity = getTileEntity(pos, TileEntityModChest.class);

					return new GuiModChest(tileEntity.createContainer(player.inventory, player));
				})
				.build();

		final ModLoadingContext context = ModLoadingContext.get();

		context.registerExtensionPoint(ExtensionPoint.GUIFACTORY, () -> message -> {
			final Function<FMLPlayMessages.OpenContainer, GuiScreen> factory = factories.get(message.getId());
			final GuiScreen gui = factory != null ? factory.apply(message) : null;
			return gui == NULL_GUI ? null : gui;
		});
	}

	@SubscribeEvent
	public static void registerClientGuiFactories(final FMLClientSetupEvent event) {
		CLIENT_GUI_FACTORIES = ImmutableMap.<ResourceLocation, Function<MessageOpenClientGui, GuiScreen>>builder()
				.put(GuiIDs.Client.SURVIVAL_COMMAND_BLOCK, message -> {
					final BlockPos pos = message.getAdditionalData().readBlockPos();
					final TileEntitySurvivalCommandBlock tileEntity = getTileEntity(pos, TileEntitySurvivalCommandBlock.class);

					return new GuiSurvivalCommandBlock(tileEntity);
				})
				.put(GuiIDs.Client.LOCK, message -> {
					final BlockPos pos = message.getAdditionalData().readBlockPos();
					final boolean hasFacing = message.getAdditionalData().readBoolean();

					final EnumFacing facing;
					if (hasFacing) {
						facing = message.getAdditionalData().readEnumValue(EnumFacing.class);
					} else {
						facing = null;
					}

					return CapabilityLock.getLock(Minecraft.getInstance().world, pos, facing)
							.<GuiScreen>map(lock -> new GuiLock(lock, pos, facing))
							.orElse(NULL_GUI);
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

	private static EntityPlayerSP getPlayer() {
		return Minecraft.getInstance().player;
	}
}
