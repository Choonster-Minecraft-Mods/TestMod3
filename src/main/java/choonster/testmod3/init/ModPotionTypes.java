package choonster.testmod3.init;

import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;

/**
 * Registers this mod's {@link PotionType}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
public class ModPotionTypes {
	private static final String LONG_PREFIX = "long_";
	private static final String STRONG_PREFIX = "strong_";

	private static final int HELPFUL_DURATION_STANDARD = 3600;
	private static final int HELPFUL_DURATION_LONG = 9600;
	private static final int HELPFUL_DURATION_STRONG = 1800;

	private static final int HARMFUL_DURATION_STANDARD = 1800;
	private static final int HARMFUL_DURATION_LONG = 4800;
	private static final int HARMFUL_DURATION_STRONG = 900;

	public static final PotionType TEST;
	public static final PotionType LONG_TEST;
	public static final PotionType STRONG_TEST;

	static {
		TEST = createPotionType(new PotionEffect(ModPotions.TEST, HELPFUL_DURATION_STANDARD));
		LONG_TEST = createPotionType(new PotionEffect(ModPotions.TEST, HELPFUL_DURATION_LONG), LONG_PREFIX);
		STRONG_TEST = createPotionType(new PotionEffect(ModPotions.TEST, HELPFUL_DURATION_STRONG, 1), STRONG_PREFIX);
	}

	/**
	 * Dummy method to ensure the static initialiser runs.
	 */
	public static void registerPotionTypes() {

	}

	/**
	 * Create a {@link PotionType} from the specified {@link PotionEffect}.
	 * <p>
	 * Uses the {@link Potion}'s registry name as the {@link PotionType}'s registry name and name.
	 *
	 * @param potionEffect The PotionEffect
	 * @return The PotionType
	 */
	private static PotionType createPotionType(PotionEffect potionEffect) {
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
	private static PotionType createPotionType(PotionEffect potionEffect, @Nullable String namePrefix) {
		final ResourceLocation potionName = potionEffect.getPotion().getRegistryName();

		final ResourceLocation potionTypeName;
		if (namePrefix != null) {
			potionTypeName = new ResourceLocation(potionName.getResourceDomain(), namePrefix + potionName.getResourcePath());
		} else {
			potionTypeName = potionName;
		}

		return GameRegistry.register(new PotionType(potionName.toString(), potionEffect).setRegistryName(potionTypeName));
	}
}
