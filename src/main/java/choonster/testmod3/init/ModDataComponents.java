package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.fluid.ItemFluidTank;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.world.item.*;
import choonster.testmod3.world.item.component.lastusetime.LastUseTimeProperties;
import choonster.testmod3.world.item.component.pigspawner.IPigSpawner;
import com.mojang.serialization.Codec;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Unit;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.UnaryOperator;

/**
 * Registers this mod's {@link net.minecraft.core.component.DataComponentType DataComponentTypes}.
 *
 * @author Choonster
 */
public class ModDataComponents {
	private static final DeferredRegister<DataComponentType<?>> DATA_COMPONENT_TYPES =
			DeferredRegister.create(Registries.DATA_COMPONENT_TYPE, TestMod3.MODID);

	private static boolean isInitialised = false;

	/**
	 * The mode for {@link ClearerItem}.
	 */
	public static final RegistryObject<DataComponentType<ClearerItem.ClearerMode>> CLEARER_MODE = register("clearer_mode",
			builder -> builder
					.persistent(ClearerItem.ClearerMode.CODEC)
					.networkSynchronized(ClearerItem.ClearerMode.STREAM_CODEC)
	);

	/**
	 * Used by {@link DimensionReplacementItem} to indicate that the replacement logic has been run.
	 * <p>
	 * This is needed to ensure that items crafted in a dimension without a replacement don't get replaced as soon as the player enters a dimension with a replacement.
	 */
	public static final RegistryObject<DataComponentType<Unit>> DIMENSION_REPLACER_REPLACED = register("dimension_replacer_replaced",
			ModDataComponents::unit
	);


	/**
	 * The properties for {@link EntityCheckerItem}.
	 */
	public static final RegistryObject<DataComponentType<EntityCheckerItem.EntityCheckerProperties>> ENTITY_CHECKER_PROPERTIES = register("entity_checker_properties",
			builder -> builder
					.persistent(EntityCheckerItem.EntityCheckerProperties.CODEC)
					.networkSynchronized(EntityCheckerItem.EntityCheckerProperties.NETWORK_CODEC)
					.cacheEncoding()
	);

	/**
	 * The entity interaction count for {@link  EntityInteractionTestItem}.
	 */
	public static final RegistryObject<DataComponentType<Integer>> ENTITY_INTERACTION_COUNT = register("entity_interaction_count",
			builder -> builder
					.persistent(Codec.INT)
					.networkSynchronized(ByteBufCodecs.INT)
	);

	/**
	 * The {@link FluidStack} represented by {@link FluidStackItem}
	 */
	public static final RegistryObject<DataComponentType<FluidStack>> FLUID_STACK = register("fluid_stack",
			builder -> builder
					.persistent(FluidStack.CODEC)
					.networkSynchronized(VanillaCodecs.FLUID_STACK_OPTIONAL_STREAM_CODEC)
					.cacheEncoding()
	);

	/**
	 * Used by {@link HiddenBlockRevealerItem} to indicate whether hidden blocks should be revealed.
	 */
	public static final RegistryObject<DataComponentType<Unit>> REVEAL_HIDDEN_BLOCKS = register("reveal_hidden_blocks",
			ModDataComponents::unit
	);

	/**
	 * @see LastUseTimeProperties
	 */
	public static final RegistryObject<DataComponentType<LastUseTimeProperties>> LAST_USE_TIME_PROPERTIES = register("last_use_time_properties",
			builder -> builder
					.persistent(LastUseTimeProperties.CODEC)
					.networkSynchronized(LastUseTimeProperties.NETWORK_CODEC)
					.cacheEncoding()
	);

	/**
	 * The armour replaced by {@link ReplacementArmourItem}.
	 */
	public static final RegistryObject<DataComponentType<ReplacementArmourItem.ReplacedArmour>> REPLACED_ARMOUR = register("replaced_armour",
			builder -> builder
					.persistent(ReplacementArmourItem.ReplacedArmour.CODEC)
					.networkSynchronized(ReplacementArmourItem.ReplacedArmour.STREAM_CODEC)
					.cacheEncoding()
	);

	/**
	 * The number displayed by {@link ScriptsItem}.
	 */
	public static final RegistryObject<DataComponentType<Integer>> SCRIPTS_NUMBER = register("scripts_number",
			builder -> builder
					.persistent(Codec.INT)
					.networkSynchronized(ByteBufCodecs.VAR_INT)

	);

	/**
	 * @see IPigSpawner
	 */
	public static final RegistryObject<DataComponentType<IPigSpawner>> PIG_SPAWNER = register("pig_spawner",
			builder -> builder
					.persistent(IPigSpawner.CODEC)
					.networkSynchronized(IPigSpawner.STREAM_CODEC)
					.cacheEncoding()
	);

	/**
	 * @see ItemFluidTank
	 */
	public static final RegistryObject<DataComponentType<FluidStack>> CONTAINED_FLUID = register("contained_fluid",
			builder -> builder
					.persistent(FluidStack.CODEC)
					.networkSynchronized(VanillaCodecs.FLUID_STACK_OPTIONAL_STREAM_CODEC)
					.cacheEncoding()
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

		DATA_COMPONENT_TYPES.register(modBusGroup);

		isInitialised = true;
	}

	private static <T> RegistryObject<DataComponentType<T>> register(final String name, final UnaryOperator<DataComponentType.Builder<T>> builder) {
		return DATA_COMPONENT_TYPES.register(name, () -> builder.apply(DataComponentType.builder()).build());
	}

	private static DataComponentType.Builder<Unit> unit(final DataComponentType.Builder<Unit> builder) {
		return builder
				.persistent(Codec.unit(Unit.INSTANCE))
				.networkSynchronized(StreamCodec.unit(Unit.INSTANCE));
	}
}
