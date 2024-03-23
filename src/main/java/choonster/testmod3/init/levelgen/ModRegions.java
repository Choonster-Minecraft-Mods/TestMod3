package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.biome.TestMod3OverworldRegion;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.Regions;

/**
 * Registers this mod's TerraBlender {@link terrablender.api.Region Regions}.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModRegions {
	public static final ResourceLocation OVERWORLD = new ResourceLocation(TestMod3.MODID, "overworld");

	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			Regions.register(new TestMod3OverworldRegion(OVERWORLD, 2));
		});
	}
}
