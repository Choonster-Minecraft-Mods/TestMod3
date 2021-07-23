package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.effect.TestMod3MobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fmllegacy.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Registers this mod's {@link Effect}s.
 *
 * @author Choonster
 */
public class ModEffects {
	private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<TestMod3MobEffect> TEST = EFFECTS.register("test",
			() -> new TestMod3MobEffect(MobEffectCategory.BENEFICIAL, 2, 2, 2)
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
