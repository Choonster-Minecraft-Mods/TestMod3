package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.capability.chunkenergy.ChunkEnergyCapability;
import choonster.testmod3.client.gui.ChunkEnergyGuiOverlay;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link  IGuiOverlay}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModGuiOverlays {
	public static void registerGuiOverlays(final RegisterGuiOverlaysEvent event) {
		event.registerAboveAll(ChunkEnergyCapability.ID.toString(), new ChunkEnergyGuiOverlay(Minecraft.getInstance()));
	}
}
