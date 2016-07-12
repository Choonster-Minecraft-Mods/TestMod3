package choonster.testmod3.init;

import choonster.testmod3.potion.PotionTestMod3;
import net.minecraft.potion.Potion;
import net.minecraftforge.fml.common.registry.GameRegistry;

/**
 * Registers this mod's {@link Potion}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
public class ModPotions {
	public static final PotionTestMod3 TEST;

	static {
		TEST = registerPotion(new PotionTestMod3(false, 2, 2, 2, "test"));
	}

	/**
	 * Dummy method to ensure the static initialiser runs.
	 */
	public static void registerPotions() {

	}

	/**
	 * Register a {@link Potion}.
	 *
	 * @param potion   The Potion instance
	 * @param <POTION> The Potion type
	 * @return The Potion instance
	 */
	private static <POTION extends Potion> POTION registerPotion(POTION potion) {
		return GameRegistry.register(potion);
	}
}
