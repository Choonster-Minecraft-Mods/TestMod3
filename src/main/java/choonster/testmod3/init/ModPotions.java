package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link Potion}s.
 *
 * @author Choonster
 */
public class ModPotions {
	private static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, TestMod3.MODID);

	private static boolean isInitialised;

	private static final String LONG_PREFIX = "long_";
	private static final String STRONG_PREFIX = "strong_";

	private static final int HELPFUL_DURATION_STANDARD = 3600;
	private static final int HELPFUL_DURATION_LONG = 9600;
	private static final int HELPFUL_DURATION_STRONG = 1800;

	private static final int HARMFUL_DURATION_STANDARD = 1800;
	private static final int HARMFUL_DURATION_LONG = 4800;
	private static final int HARMFUL_DURATION_STRONG = 900;

	public static final RegistryObject<Potion> TEST = registerPotion("test",
			() -> new MobEffectInstance(ModMobEffects.TEST.get(), HELPFUL_DURATION_STANDARD)
	);

	public static final RegistryObject<Potion> LONG_TEST = registerPotion("test",
			() -> new MobEffectInstance(ModMobEffects.TEST.get(), HELPFUL_DURATION_LONG),
			LONG_PREFIX
	);

	public static final RegistryObject<Potion> STRONG_TEST = registerPotion("test",
			() -> new MobEffectInstance(ModMobEffects.TEST.get(), HELPFUL_DURATION_STRONG, 1),
			STRONG_PREFIX
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

		POTIONS.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Registers a {@link Potion} from the specified {@link MobEffectInstance}.
	 * <p>
	 * Uses the specified name as the {@link Potion}'s registry name and base name.
	 *
	 * @param name                  The base name of the potion
	 * @param effectInstanceFactory The factory used to create the potion's effect instance
	 * @return A RegistryObject reference to the potion
	 */
	private static RegistryObject<Potion> registerPotion(final String name, final Supplier<MobEffectInstance> effectInstanceFactory) {
		return registerPotion(name, effectInstanceFactory, null);
	}

	/**
	 * Registers a {@link Potion} from the specified {@link MobEffectInstance}
	 * <p>
	 * Uses the {@link MobEffect}'s registry name as the {@link Potion}'s registry name (with an optional prefix) and base name (with no prefix).
	 *
	 * @param name                  The base name of the potion
	 * @param effectInstanceFactory The factory used to create the potion's effect instance
	 * @param namePrefix            The name prefix, if any
	 * @return The PotionType
	 */
	private static RegistryObject<Potion> registerPotion(final String name, final Supplier<MobEffectInstance> effectInstanceFactory, @Nullable final String namePrefix) {
		final String fullName = namePrefix != null ? namePrefix + name : name;

		return POTIONS.register(fullName, () -> {
			// Based on net.minecraft.util.Util.makeTranslationKey. This ensures that the base name is valid in ResourceLocation paths.
			final String potionBaseName = TestMod3.MODID + "." + name.replace('/', '.');

			return new Potion(potionBaseName, effectInstanceFactory.get());
		});
	}
}
