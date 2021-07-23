package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fmlclient.registry.ClientRegistry;
import org.lwjgl.glfw.GLFW;

/**
 * Registers this mod's {@link KeyMapping}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.MOD)
public class ModKeyMappings {

	private static final String CATEGORY = TestMod3Lang.KEY_CATEGORY_GENERAL.getTranslationKey();

	public static final KeyMapping PLACE_HELD_BLOCK = new KeyMapping(TestMod3Lang.KEY_PLACE_HELD_BLOCK.getTranslationKey(), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_L, CATEGORY);
	public static final KeyMapping PRINT_POTIONS = new KeyMapping(TestMod3Lang.KEY_PRINT_POTIONS.getTranslationKey(), KeyConflictContext.IN_GAME, InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_K, CATEGORY);

	@SubscribeEvent
	public static void registerKeyMappings(final FMLClientSetupEvent event) {
		ClientRegistry.registerKeyBinding(PLACE_HELD_BLOCK);
		ClientRegistry.registerKeyBinding(PRINT_POTIONS);
	}
}
