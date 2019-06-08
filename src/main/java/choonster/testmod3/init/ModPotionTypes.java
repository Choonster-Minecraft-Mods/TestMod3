package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import javax.annotation.Nullable;

import static choonster.testmod3.util.InjectionUtil.Null;

/**
 * Registers this mod's {@link PotionType}s.
 *
 * @author Choonster
 */
@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModPotionTypes {

	public static final PotionType TEST = Null();

	public static final PotionType LONG_TEST = Null();

	public static final PotionType STRONG_TEST = Null();

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		/**
		 * Register this mod's {@link PotionType}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerPotionTypes(final RegistryEvent.Register<PotionType> event) {
			final String LONG_PREFIX = "long_";
			final String STRONG_PREFIX = "strong_";

			final int HELPFUL_DURATION_STANDARD = 3600;
			final int HELPFUL_DURATION_LONG = 9600;
			final int HELPFUL_DURATION_STRONG = 1800;

			final int HARMFUL_DURATION_STANDARD = 1800;
			final int HARMFUL_DURATION_LONG = 4800;
			final int HARMFUL_DURATION_STRONG = 900;

			// Can't use the fields from ModPotions because object holders haven't been applied between RegistryEvent.Register<Potion> and now
			// TODO: Check if ObjectHolders are applied between every registry event now
			final IForgeRegistry<Potion> potionRegistry = ForgeRegistries.POTIONS;
			final Potion test = RegistryUtil.getRegistryEntry(potionRegistry, "test");

			final PotionType[] potionTypes = new PotionType[]{
					createPotionType(new PotionEffect(test, HELPFUL_DURATION_STANDARD)),
					createPotionType(new PotionEffect(test, HELPFUL_DURATION_LONG), LONG_PREFIX),
					createPotionType(new PotionEffect(test, HELPFUL_DURATION_STRONG, 1), STRONG_PREFIX),
			};

			event.getRegistry().registerAll(potionTypes);
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
			final ResourceLocation potionName = Preconditions.checkNotNull(potionEffect.getPotion().getRegistryName());

			final ResourceLocation potionTypeName;
			if (namePrefix != null) {
				potionTypeName = new ResourceLocation(potionName.getNamespace(), namePrefix + potionName.getPath());
			} else {
				potionTypeName = potionName;
			}

			return new PotionType(potionName.toString(), potionEffect).setRegistryName(potionTypeName);
		}
	}
}
