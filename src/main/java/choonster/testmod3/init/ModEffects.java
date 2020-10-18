package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.potion.TestMod3Effect;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link Effect}s.
 *
 * @author Choonster
 */
public class ModEffects {
	private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<TestMod3Effect> TEST = EFFECTS.register("test",
			() -> new TestMod3Effect(EffectType.BENEFICIAL, 2, 2, 2)
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		EFFECTS.register(modEventBus);

		isInitialised = true;
	}
}
