package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.input.Keyboard;

/**
 * Registers this mod's {@link KeyBinding}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModKeyBindings {

	private static final String CATEGORY = "key.category.testmod3:general";

	public static final KeyBinding PLACE_HELD_BLOCK = new KeyBinding("key.testmod3:place_held_block", KeyConflictContext.IN_GAME, Keyboard.KEY_L, CATEGORY);
	public static final KeyBinding PRINT_POTIONS = new KeyBinding("key.testmod3:print_potions", KeyConflictContext.IN_GAME, Keyboard.KEY_K, CATEGORY);

	@SubscribeEvent
	public static void registerKeyBindings(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(PLACE_HELD_BLOCK);
		ClientRegistry.registerKeyBinding(PRINT_POTIONS);
	}
}
