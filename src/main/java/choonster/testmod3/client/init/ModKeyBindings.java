package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.lwjgl.glfw.GLFW;

/**
 * Registers this mod's {@link KeyBinding}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModKeyBindings {

	private static final String CATEGORY = TestMod3Lang.KEY_CATEGORY_GENERAL.getTranslationKey();

	public static final KeyBinding PLACE_HELD_BLOCK = new KeyBinding(TestMod3Lang.KEY_PLACE_HELD_BLOCK.getTranslationKey(), KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_L, CATEGORY);
	public static final KeyBinding PRINT_POTIONS = new KeyBinding(TestMod3Lang.KEY_PRINT_POTIONS.getTranslationKey(), KeyConflictContext.IN_GAME, InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);

	@SubscribeEvent
	public static void registerKeyBindings(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(PLACE_HELD_BLOCK);
		ClientRegistry.registerKeyBinding(PRINT_POTIONS);
	}
}
