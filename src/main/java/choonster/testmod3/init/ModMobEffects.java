package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.effect.TestMod3MobEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

/**
 * Registers this mod's {@link MobEffect}s.
 *
 * @author Choonster
 */
public class ModMobEffects {
	private static final DeferredRegister<MobEffect> EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<MobEffect> TEST = EFFECTS.register("test",
			() -> new TestMod3MobEffect(MobEffectCategory.BENEFICIAL, 2, 2, 2)
	);

	/**
	 * Registers the {@link DeferredRegister} instance with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modBusGroup The mod bus group
	 */
	public static void initialise(final BusGroup modBusGroup) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		EFFECTS.register(modBusGroup);

		isInitialised = true;
	}
}
