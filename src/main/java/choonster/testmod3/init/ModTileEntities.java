package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.tileentity.*;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.function.Supplier;

@ObjectHolder(TestMod3.MODID)
public class ModTileEntities {
	private static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, TestMod3.MODID);

	private static boolean isInitialised;

	public static final RegistryObject<TileEntityType<SurvivalCommandBlockTileEntity>> SURVIVAL_COMMAND_BLOCK = registerTileEntityType("survival_command_block",
			SurvivalCommandBlockTileEntity::new,
			ModBlocks.SURVIVAL_COMMAND_BLOCK
	);

	public static final RegistryObject<TileEntityType<FluidTankTileEntity>> FLUID_TANK = registerTileEntityType("fluid_tank",
			FluidTankTileEntity::new,
			ModBlocks.FLUID_TANK
	);

	public static final RegistryObject<TileEntityType<RestrictedFluidTankTileEntity>> FLUID_TANK_RESTRICTED = registerTileEntityType("fluid_tank_restricted",
			RestrictedFluidTankTileEntity::new,
			ModBlocks.FLUID_TANK_RESTRICTED
	);

	public static final RegistryObject<TileEntityType<PotionEffectTileEntity>> POTION_EFFECT = registerTileEntityType("potion_effect",
			PotionEffectTileEntity::new,
			ModBlocks.POTION_EFFECT
	);

	public static final RegistryObject<TileEntityType<ModChestTileEntity>> MOD_CHEST = registerTileEntityType("mod_chest",
			ModChestTileEntity::new,
			ModBlocks.CHEST
	);

	public static final RegistryObject<TileEntityType<HiddenTileEntity>> HIDDEN = registerTileEntityType("hidden",
			HiddenTileEntity::new,
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

		TILE_ENTITY_TYPES.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Registers a tile entity type with the specified tile entity factory and valid block.
	 *
	 * @param name              The registry name of the tile entity type
	 * @param tileEntityFactory The factory used to create the tile entity instances
	 * @param validBlock        The valid block for the tile entity
	 * @param <T>               The tile entity class
	 * @return A RegistryObject reference to the tile entity type
	 */
	private static <T extends TileEntity> RegistryObject<TileEntityType<T>> registerTileEntityType(final String name, final Supplier<T> tileEntityFactory, final RegistryObject<? extends Block> validBlock) {
		return TILE_ENTITY_TYPES.register(name, () -> {
			@SuppressWarnings("ConstantConditions")
			// dataFixerType will always be null until mod data fixers are implemented
			final TileEntityType<T> tileEntityType = TileEntityType.Builder
					.create(tileEntityFactory, validBlock.get())
					.build(null);

			return tileEntityType;
		});
	}
}
