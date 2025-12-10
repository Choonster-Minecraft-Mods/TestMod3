package choonster.testmod3.client.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.text.TestMod3Lang;
import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

/**
 * Registers this mod's {@link KeyMapping}s.
 *
 * @author Choonster
 */
@Mod.EventBusSubscriber(modid = TestMod3.MODID, value = Dist.CLIENT, bus = Bus.FORGE)
public class ModKeyMappings {

	public static final KeyMapping.Category CATEGORY = KeyMapping.Category.register(
			Identifier.fromNamespaceAndPath(TestMod3.MODID, "general")
	);

	public static final KeyMapping PLACE_HELD_BLOCK = new KeyMapping(
			TestMod3Lang.KEY_PLACE_HELD_BLOCK.getTranslationKey(),
			KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_L,
			CATEGORY,
			0
	);

	public static final KeyMapping PRINT_POTIONS = new KeyMapping(
			TestMod3Lang.KEY_PRINT_POTIONS.getTranslationKey(),
			KeyConflictContext.IN_GAME,
			InputConstants.Type.KEYSYM,
			InputConstants.KEY_K,
			CATEGORY,
			1
	);

	@SubscribeEvent
	public static void registerKeyMappings(final RegisterKeyMappingsEvent event) {
		event.register(PLACE_HELD_BLOCK);
		event.register(PRINT_POTIONS);
	}
}
