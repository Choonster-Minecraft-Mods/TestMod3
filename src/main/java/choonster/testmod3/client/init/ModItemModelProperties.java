package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.client.item.RevealHiddenBlocksItemPropertyFunction;
import choonster.testmod3.client.item.TicksSinceLastUseItemPropertyFunction;
import choonster.testmod3.init.ModItems;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Registers this mod's {@link ClampedItemPropertyFunction}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD, value = Dist.CLIENT)
public class ModItemModelProperties {
	@SubscribeEvent
	public static void registerItemModelProperties(final FMLClientSetupEvent event) {
		event.enqueueWork(() -> {
			RevealHiddenBlocksItemPropertyFunction.registerForItem(ModItems.HIDDEN_BLOCK_REVEALER.get());
			TicksSinceLastUseItemPropertyFunction.registerForItem(ModItems.MODEL_TEST.get());
			TicksSinceLastUseItemPropertyFunction.registerForItem(ModItems.SLINGSHOT.get());
		});
	}
}
