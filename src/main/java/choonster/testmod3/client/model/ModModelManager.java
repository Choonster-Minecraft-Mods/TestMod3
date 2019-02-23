package choonster.testmod3.client.model;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.slab.BlockColouredSlab;
import choonster.testmod3.block.variantgroup.IBlockVariantGroup;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.EnumFaceRotation;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(value = Side.CLIENT, modid = TestMod3.MODID)
public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	private static final String FLUID_MODEL_PATH = Constants.RESOURCE_PREFIX + "fluid";

	private ModModelManager() {
	}

	/**
	 * Register this mod's {@link Fluid}, {@link Block} and {@link Item} models.
	 *
	 * @param event The event
	 */
	@SubscribeEvent
	public static void registerAllModels(final ModelRegistryEvent event) {
		INSTANCE.registerFluidModels();
		INSTANCE.registerBlockModels();
		INSTANCE.registerItemModels();
	}

	/**
	 * Register this mod's {@link Fluid} models.
	 */
	private void registerFluidModels() {
		ModFluids.MOD_FLUID_BLOCKS.forEach(this::registerFluidModel);
	}

	/**
	 * Register the block and item model for a {@link Fluid}.
	 *
	 * @param fluidBlock The Fluid's Block
	 */
	private void registerFluidModel(final IFluidBlock fluidBlock) {
		final Item item = Item.getItemFromBlock((Block) fluidBlock);
		assert item != Items.AIR;

		ModelBakery.registerItemVariants(item);

		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(final IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	/**
	 * A {@link StateMapperBase} used to create property strings.
	 */
	private final StateMapperBase propertyStringMapper = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(final IBlockState state) {
			return new ModelResourceLocation("minecraft:air");
		}
	};

	/**
	 * Register this mod's {@link Block} models.
	 */
	private void registerBlockModels() {
		ModelLoader.setCustomStateMapper(ModBlocks.WATER_GRASS, new StateMap.Builder().ignore(BlockLiquid.LEVEL).build());

		registerBlockItemModel(
				ModBlocks.RIGHT_CLICK_TEST.getDefaultState()
						.withProperty(BlockRightClickTest.HAS_ENDER_EYE, false)
		);

		registerBlockVariantGroupItemModels(
				ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS,
				block -> block.getDefaultState()
						.withProperty(BlockColoredRotatable.FACING, EnumFacing.NORTH)
		);

		registerBlockVariantGroupItemModels(
				ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS,
				block -> block.getDefaultState()
						.withProperty(BlockColoredMultiRotatable.FACING, EnumFacing.NORTH)
						.withProperty(BlockColoredMultiRotatable.FACE_ROTATION, EnumFaceRotation.UP)
		);

		registerBlockVariantGroupItemModels(
				ModBlocks.VariantGroups.TERRACOTTA_SLABS,
				block -> {
					if (block.isDouble()) {
						return block.getDefaultState();
					} else {
						return block.getDefaultState()
								.withProperty(BlockColouredSlab.HALF, BlockColouredSlab.EnumBlockHalf.BOTTOM);
					}
				}
		);

		registerBlockVariantGroupItemModels(
				ModBlocks.VariantGroups.VARIANTS_BLOCKS,
				Block::getDefaultState
		);

		registerBlockItemModel(
				ModBlocks.MIRROR_PLANE.getDefaultState()
						.withProperty(BlockPlane.HORIZONTAL_ROTATION, EnumFacing.NORTH)
						.withProperty(BlockPlane.VERTICAL_ROTATION, BlockPlane.EnumVerticalRotation.UP)
		);

		registerBlockItemModel(
				ModBlocks.CHEST.getDefaultState()
						.withProperty(BlockModChest.FACING, EnumFacing.NORTH)
		);

		registerVariantBlockItemModels(
				ModBlocks.SAPLING.getDefaultState()
						.withProperty(BlockSaplingTestMod3.STAGE, 0)
						.withProperty(BlockSaplingTestMod3.ITEM, true),
				BlockSaplingTestMod3.TYPE, BlockPlanks.EnumType::getMetadata
		);

		ModBlocks.RegistrationHandler.ITEM_BLOCKS.stream().filter(item -> !itemsRegistered.contains(item)).forEach(this::registerItemModel);
	}

	/**
	 * Register a single model for the {@link Block}'s {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and the {@link IBlockState} as the variant.
	 *
	 * @param state The state to use as the variant
	 */
	private void registerBlockItemModel(final IBlockState state) {
		final Block block = state.getBlock();
		final Item item = Item.getItemFromBlock(block);

		if (item != Items.AIR) {
			final ResourceLocation registryName = Objects.requireNonNull(block.getRegistryName());
			final ModelResourceLocation fullModelLocation = new ModelResourceLocation(registryName, propertyStringMapper.getPropertyString(state.getProperties()));
			ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
			registerItemModel(item, stack -> fullModelLocation);
		}
	}

	/**
	 * Register a model for a metadata value of the {@link Block}'s {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and the {@link IBlockState} as the variant.
	 *
	 * @param state    The state to use as the variant
	 * @param metadata The item metadata to register the model for
	 */
	private void registerBlockItemModelForMeta(final IBlockState state, final int metadata) {
		final Item item = Item.getItemFromBlock(state.getBlock());

		if (item != Items.AIR) {
			registerItemModelForMeta(item, metadata, propertyStringMapper.getPropertyString(state.getProperties()));
		}
	}

	/**
	 * Register a model for each of the variant group's items.
	 * <p>
	 * For each item:
	 * <li>The domain/path is the registry name</li>
	 * <li>The variant is the result of calling {@code stateFactory} with the block</li>
	 */
	private <VARIANT extends Enum<VARIANT> & IStringSerializable, BLOCK extends Block>
	void registerBlockVariantGroupItemModels(final IBlockVariantGroup<VARIANT, BLOCK> variantGroup, final Function<BLOCK, IBlockState> stateFactory) {
		variantGroup.getBlocks().stream()
				.map(stateFactory)
				.forEach(this::registerBlockItemModel);
	}

	/**
	 * Register a model for each metadata value of the {@link Block}'s {@link Item} corresponding to the values of an {@link IProperty}.
	 * <p>
	 * For each value:
	 * <li>The domain/path is the registry name</li>
	 * <li>The variant is {@code baseState} with the {@link IProperty} set to the value</li>
	 * <p>
	 * The {@code getMeta} function is used to get the metadata of each value.
	 *
	 * @param baseState The base state to use for the variant
	 * @param property  The property whose values should be used
	 * @param getMeta   A function to get the metadata of each value
	 * @param <T>       The value type
	 */
	private <T extends Comparable<T>> void registerVariantBlockItemModels(final IBlockState baseState, final IProperty<T> property, final ToIntFunction<T> getMeta) {
		property.getAllowedValues().forEach(value -> registerBlockItemModelForMeta(baseState.withProperty(property, value), getMeta.applyAsInt(value)));
	}

	/**
	 * The {@link Item}s that have had models registered so far.
	 */
	private final Set<Item> itemsRegistered = new HashSet<>();

	/**
	 * Register this mod's {@link Item} models.
	 */
	private void registerItemModels() {
		// Register items with custom model names first
		registerItemModel(ModItems.SNOWBALL_LAUNCHER, "minecraft:fishing_rod");
		registerItemModel(ModItems.UNICODE_TOOLTIPS, "minecraft:rabbit");
		registerItemModel(ModItems.SWAP_TEST_A, "minecraft:brick");
		registerItemModel(ModItems.SWAP_TEST_B, "minecraft:netherbrick");
		registerItemModel(ModItems.BLOCK_DEBUGGER, "minecraft:nether_star");
		registerItemModel(ModItems.WOODEN_HARVEST_SWORD, "minecraft:wooden_sword");
		registerItemModel(ModItems.DIAMOND_HARVEST_SWORD, "minecraft:diamond_sword");
		registerItemModel(ModItems.CLEARER, "minecraft:nether_star");
		registerItemModel(ModItems.HEIGHT_TESTER, "minecraft:compass");
		registerItemModel(ModItems.HEAVY, "minecraft:brick");
		registerItemModel(ModItems.ENTITY_TEST, "minecraft:porkchop");
		registerItemModel(ModItems.BLOCK_DESTROYER, "minecraft:tnt_minecart");
		registerItemModel(ModItems.REPLACEMENT_HELMET, "minecraft:chainmail_helmet");
		registerItemModel(ModItems.REPLACEMENT_CHESTPLATE, "minecraft:chainmail_chestplate");
		registerItemModel(ModItems.REPLACEMENT_LEGGINGS, "minecraft:chainmail_leggings");
		registerItemModel(ModItems.REPLACEMENT_BOOTS, "minecraft:chainmail_boots");
		registerItemModel(ModItems.PIG_SPAWNER_FINITE, "minecraft:porkchop");
		registerItemModel(ModItems.PIG_SPAWNER_INFINITE, "minecraft:porkchop");
		registerItemModel(ModItems.RESPAWNER, "minecraft:clock");
		registerItemModel(ModItems.LOOT_TABLE_TEST, "minecraft:gold_ingot");
		registerItemModel(ModItems.SADDLE, "minecraft:saddle");
		registerItemModel(ModItems.WOODEN_SLOW_SWORD, "minecraft:wooden_sword");
		registerItemModel(ModItems.DIAMOND_SLOW_SWORD, "minecraft:diamond_sword");
		registerItemModel(ModItems.NO_MOD_NAME, "minecraft:bread");
		registerItemModel(ModItems.SATURATION_HELMET, "minecraft:chainmail_helmet");
		registerItemModel(ModItems.ENTITY_CHECKER, "minecraft:bone");

		// Then register items with default model names
		ModItems.RegistrationHandler.ITEMS.stream().filter(item -> !itemsRegistered.contains(item)).forEach(this::registerItemModel);
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and {@code "inventory"} as the variant.
	 *
	 * @param item The Item
	 */
	private void registerItemModel(final Item item) {
		final ResourceLocation registryName = Objects.requireNonNull(item.getRegistryName());
		registerItemModel(item, registryName.toString());
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code modelLocation} as the domain/path and {@link "inventory"} as the variant.
	 *
	 * @param item          The Item
	 * @param modelLocation The model location
	 */
	private void registerItemModel(final Item item, final String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
		registerItemModel(item, stack -> fullModelLocation);
	}

	/**
	 * Register an {@link ItemMeshDefinition} for an {@link Item}.
	 *
	 * @param item           The Item
	 * @param meshDefinition The ItemMeshDefinition
	 */
	private void registerItemModel(final Item item, final ItemMeshDefinition meshDefinition) {
		itemsRegistered.add(item);
		ModelLoader.setCustomMeshDefinition(item, meshDefinition);
	}

	/**
	 * Register a model for a metadata value an {@link Item}.
	 * <p>
	 * Uses the registry name as the domain/path and {@code variant} as the variant.
	 *
	 * @param item     The Item
	 * @param metadata The metadata
	 * @param variant  The variant
	 */
	private void registerItemModelForMeta(final Item item, final int metadata, final String variant) {
		itemsRegistered.add(item);
		ModelLoader.setCustomModelResourceLocation(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
	}
}
