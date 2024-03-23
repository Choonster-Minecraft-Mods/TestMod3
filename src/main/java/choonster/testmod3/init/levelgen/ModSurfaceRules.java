package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.surfacerule.TestMod3OverworldSurfaceRuleData;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import terrablender.api.SurfaceRuleManager;

/**
 * Registers this mod's {@link SurfaceRules.RuleSource SurfaceRule Sources} with TerraBlender.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
public class ModSurfaceRules {
	@SubscribeEvent
	public static void commonSetup(final FMLCommonSetupEvent event) {
		event.enqueueWork(() -> {
			SurfaceRuleManager.addSurfaceRules(SurfaceRuleManager.RuleCategory.OVERWORLD, TestMod3.MODID, TestMod3OverworldSurfaceRuleData.makeRules());
		});
	}
}
