package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;

import javax.annotation.Nullable;

/**
 * Registers this mod's {@link PotionType}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModPotionTypes {

	public static final PotionType TEST;

	public static final PotionType LONG_TEST;

	public static final PotionType STRONG_TEST;

	static {
		final String LONG_PREFIX = "long_";
		final String STRONG_PREFIX = "strong_";

		final int HELPFUL_DURATION_STANDARD = 3600;
		final int HELPFUL_DURATION_LONG = 9600;
		final int HELPFUL_DURATION_STRONG = 1800;

		final int HARMFUL_DURATION_STANDARD = 1800;
		final int HARMFUL_DURATION_LONG = 4800;
		final int HARMFUL_DURATION_STRONG = 900;

		TEST = createPotionType(new PotionEffect(ModPotions.TEST, HELPFUL_DURATION_STANDARD));
		LONG_TEST = createPotionType(new PotionEffect(ModPotions.TEST, HELPFUL_DURATION_LONG), LONG_PREFIX);
		STRONG_TEST = createPotionType(new PotionEffect(ModPotions.TEST, HELPFUL_DURATION_STRONG, 1), STRONG_PREFIX);
	}

	/**
	 * Create a {@link PotionType} from the specified {@link PotionEffect}.
	 * <p>
	 * Uses the {@link Potion}'s registry name as the {@link PotionType}'s registry name and name.
	 *
	 * @param potionEffect The PotionEffect
	 * @return The PotionType
	 */
	private static PotionType createPotionType(final PotionEffect potionEffect) {
		return createPotionType(potionEffect, null);
	}

	/**
	 * Create a {@link PotionType} from the specified {@link PotionEffect}
	 * <p>
	 * Uses the {@link Potion}'s registry name as the {@link PotionType}'s registry name (with an optional prefix) and name (with no prefix).
	 *
	 * @param potionEffect The PotionEffect
	 * @param namePrefix   The name prefix, if any
	 * @return The PotionType
	 */
	private static PotionType createPotionType(final PotionEffect potionEffect, @Nullable final String namePrefix) {
		final ResourceLocation potionName = potionEffect.getPotion().getRegistryName();

		final ResourceLocation potionTypeName;
		if (namePrefix != null) {
			potionTypeName = new ResourceLocation(potionName.getResourceDomain(), namePrefix + potionName.getResourcePath());
		} else {
			potionTypeName = potionName;
		}

		return new PotionType(potionName.toString(), potionEffect).setRegistryName(potionTypeName);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link PotionType}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerPotionTypes(final RegistryEvent.Register<PotionType> event) {
			event.getRegistry().registerAll(
					TEST,
					LONG_TEST,
					STRONG_TEST
			);
		}
	}
}
