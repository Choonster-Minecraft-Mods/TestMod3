package choonster.testmod3.init.levelgen;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.levelgen.placement.InChunksDivisibleBy16Filter;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.PlacementModifierType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

/**
 * Registers this mod's {@link PlacementModifierType}s.
 *
 * @author Choonster
 */
public class ModPlacementModifierTypes {
	private static final DeferredRegister<PlacementModifierType<?>> PLACEMENT_MODIFIER_TYPES =
			DeferredRegister.create(Registries.PLACEMENT_MODIFIER_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<PlacementModifierType<InChunksDivisibleBy16Filter>> IN_CHUNKS_DIVISIBLE_BY_16 = register(
			"in_chunks_divisible_by_16",
			() -> () -> InChunksDivisibleBy16Filter.CODEC
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

		PLACEMENT_MODIFIER_TYPES.register(modEventBus);

		isInitialised = true;
	}

	private static <P extends PlacementModifier> RegistryObject<PlacementModifierType<P>> register(
			final String name,
			final Supplier<PlacementModifierType<P>> factory
	) {
		return PLACEMENT_MODIFIER_TYPES.register(name, factory);
	}
}
