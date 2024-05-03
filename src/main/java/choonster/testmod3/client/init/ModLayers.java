package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.gui.ChunkEnergyGuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Registers this mod's {@link net.minecraft.client.gui.LayeredDraw.Layer}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModLayers {
	private static final Field LAYERS = ObfuscationReflectionHelper.findField(Gui.class, "layers");

	@SubscribeEvent
	public static void registerLayers(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			final var minecraft = Minecraft.getInstance();
			final var gui = minecraft.gui;

			try {
				final var layers = (LayeredDraw) LAYERS.get(gui);
				registerLayers(minecraft, layers);
			} catch (final IllegalAccessException e) {
				throw new RuntimeException("Failed to get Gui layers", e);
			}
		});
	}

	private static void registerLayers(final Minecraft minecraft, final LayeredDraw layers) {
		layers.add(new ChunkEnergyGuiOverlay(minecraft));
	}
}
