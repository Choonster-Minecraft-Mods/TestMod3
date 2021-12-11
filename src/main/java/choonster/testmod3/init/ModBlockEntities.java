package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.level.block.entity.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<BlockEntityType<SurvivalCommandBlockEntity>> SURVIVAL_COMMAND_BLOCK = registerBlockEntityType("survival_command_block",
			SurvivalCommandBlockEntity::new,
			ModBlocks.SURVIVAL_COMMAND_BLOCK
	);

	public static final RegistryObject<BlockEntityType<FluidTankBlockEntity>> FLUID_TANK = registerBlockEntityType("fluid_tank",
			FluidTankBlockEntity::new,
			ModBlocks.FLUID_TANK
	);

	public static final RegistryObject<BlockEntityType<RestrictedFluidTankBlockEntity>> FLUID_TANK_RESTRICTED = registerBlockEntityType("fluid_tank_restricted",
			RestrictedFluidTankBlockEntity::new,
			ModBlocks.FLUID_TANK_RESTRICTED
	);

	public static final RegistryObject<BlockEntityType<PotionEffectBlockEntity>> POTION_EFFECT = registerBlockEntityType("potion_effect",
			PotionEffectBlockEntity::new,
			ModBlocks.POTION_EFFECT
	);

	public static final RegistryObject<BlockEntityType<ModChestBlockEntity>> MOD_CHEST = registerBlockEntityType("mod_chest",
			ModChestBlockEntity::new,
			ModBlocks.CHEST
	);

	public static final RegistryObject<BlockEntityType<HiddenBlockEntity>> HIDDEN = registerBlockEntityType("hidden",
			HiddenBlockEntity::new,
			ModBlocks.HIDDEN
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

		BLOCK_ENTITY_TYPES.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Registers a block entity type with the specified block entity factory and valid block.
	 *
	 * @param name                The registry name of the block entity type
	 * @param blockEntitySupplier The factory used to create the block entity instances
	 * @param validBlock          The valid block for the block entity
	 * @param <T>                 The block entity class
	 * @return A RegistryObject reference to the block entity type
	 */
	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntityType(final String name, final BlockEntityType.BlockEntitySupplier<T> blockEntitySupplier, final RegistryObject<? extends Block> validBlock) {
		return BLOCK_ENTITY_TYPES.register(name, () -> {
			@SuppressWarnings("ConstantConditions")
			// dataFixerType will always be null until mod data fixers are implemented
			final BlockEntityType<T> blockEntityType = BlockEntityType.Builder
					.of(blockEntitySupplier, validBlock.get())
					.build(null);

			return blockEntityType;
		});
	}
}
