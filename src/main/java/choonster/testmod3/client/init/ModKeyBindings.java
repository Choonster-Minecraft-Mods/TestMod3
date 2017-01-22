package choonster.testmod3.client.init;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import org.lwjgl.input.Keyboard;

/**
 * Registers this mod's {@link KeyBinding}s.
 *
 * @author Choonster
 */
public class ModKeyBindings {

	private static final String CATEGORY = "key.category.testmod3:general";

	public static final KeyBinding PLACE_HELD_BLOCK = new KeyBinding("key.testmod3:place_held_block", KeyConflictContext.IN_GAME, Keyboard.KEY_L, CATEGORY);

	public static void registerKeyBindings() {
		ClientRegistry.registerKeyBinding(PLACE_HELD_BLOCK);
	}
}
