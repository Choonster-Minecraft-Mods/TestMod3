package choonster.testmod3.client.model;

import choonster.testmod3.block.*;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.init.ModItems;
import choonster.testmod3.item.ItemVariants;
import choonster.testmod3.util.Constants;
import choonster.testmod3.util.EnumFaceRotation;
import choonster.testmod3.util.IVariant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;

import java.util.HashSet;
import java.util.Set;
import java.util.function.ToIntFunction;

@Mod.EventBusSubscriber(Side.CLIENT)
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
	public static void registerAllModels(ModelRegistryEvent event) {
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
	private void registerFluidModel(IFluidBlock fluidBlock) {
		final Item item = Item.getItemFromBlock((Block) fluidBlock);
		assert item != Items.AIR;

		ModelBakery.registerItemVariants(item);

		final ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	/**
	 * A {@link StateMapperBase} used to create property strings.
	 */
	private final StateMapperBase propertyStringMapper = new StateMapperBase() {
		@Override
		protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
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

		registerVariantBlockItemModels(
				ModBlocks.COLORED_ROTATABLE.getDefaultState()
						.withProperty(BlockColoredRotatable.FACING, EnumFacing.NORTH),
				BlockColoredRotatable.COLOR, EnumDyeColor::getMetadata
		);

		registerVariantBlockItemModels(
				ModBlocks.COLORED_MULTI_ROTATABLE.getDefaultState()
						.withProperty(BlockColoredMultiRotatable.FACING, EnumFacing.NORTH)
						.withProperty(BlockColoredMultiRotatable.FACE_ROTATION, EnumFaceRotation.UP),
				BlockColoredMultiRotatable.COLOR, EnumDyeColor::getMetadata
		);

		registerSlabGroupItemModels(ModBlocks.Slabs.STAINED_CLAY_SLABS.high);
		registerSlabGroupItemModels(ModBlocks.Slabs.STAINED_CLAY_SLABS.low);

		registerVariantBlockItemModels(
				ModBlocks.VARIANTS.getDefaultState(),
				BlockVariants.VARIANT
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
	private void registerBlockItemModel(IBlockState state) {
		final Block block = state.getBlock();
		final Item item = Item.getItemFromBlock(block);

		if (item != Items.AIR) {
			registerItemModel(item, new ModelResourceLocation(block.getRegistryName(), propertyStringMapper.getPropertyString(state.getProperties())));
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
	private void registerBlockItemModelForMeta(IBlockState state, int metadata) {
		final Item item = Item.getItemFromBlock(state.getBlock());

		if (item != Items.AIR) {
			registerItemModelForMeta(item, metadata, propertyStringMapper.getPropertyString(state.getProperties()));
		}
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
	private <T extends Comparable<T>> void registerVariantBlockItemModels(IBlockState baseState, IProperty<T> property, ToIntFunction<T> getMeta) {
		property.getAllowedValues().forEach(value -> registerBlockItemModelForMeta(baseState.withProperty(property, value), getMeta.applyAsInt(value)));
	}

	/**
	 * Register a model for each metadata value of the {@link Block}'s {@link Item} corresponding to the values of an {@link IProperty}.
	 * <p>
	 * For each value:
	 * <li>The domain/path is the registry name</li>
	 * <li>The variant is {@code baseState} with the {@link IProperty} set to the value</li>
	 * <p>
	 * {@link IVariant#getMeta()} is used to get the metadata of each value.
	 *
	 * @param baseState The base state to use for the variant
	 * @param property  The property whose values should be used
	 * @param <T>       The value type
	 */
	private <T extends IVariant & Comparable<T>> void registerVariantBlockItemModels(IBlockState baseState, IProperty<T> property) {
		registerVariantBlockItemModels(baseState, property, IVariant::getMeta);
	}

	/**
	 * Register a model for each metadata value of a {@link BlockSlabTestMod3.SlabGroup}'s {@link Item} corresponding to
	 * the values of the slab's variant property ({@link BlockSlab#getVariantProperty()}).
	 * <p>
	 * For each value:
	 * <li>The domain/path is the registry name</li>
	 * <li>The variant is the default state with {@link BlockSlab#HALF} set to {@link BlockSlab.EnumBlockHalf#BOTTOM}
	 * and the variant property set to the value as the variant.</li>
	 * <p>
	 * {@link BlockSlabTestMod3#getMetadata} is used to get the metadata of each value.
	 *
	 * @param slabGroup  The SlabGroup
	 * @param <VARIANT>  The variant type
	 * @param <VARIANTS> The variant collection type
	 * @param <SLAB>     The slab type
	 */
	private <
			VARIANT extends Enum<VARIANT> & IStringSerializable,
			VARIANTS extends Iterable<VARIANT> & IStringSerializable,
			SLAB extends BlockSlabTestMod3<VARIANT, VARIANTS, SLAB>
			>
	void registerSlabGroupItemModels(BlockSlabTestMod3.SlabGroup<VARIANT, VARIANTS, SLAB> slabGroup) {
		final SLAB singleSlab = slabGroup.singleSlab;
		registerVariantBlockItemModels(
				singleSlab.getDefaultState()
						.withProperty(BlockSlab.HALF, BlockSlab.EnumBlockHalf.BOTTOM),
				singleSlab.getVariantProperty(), singleSlab::getMetadata
		);
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

		registerVariantItemModels(ModItems.VARIANTS_ITEM, "variant", ItemVariants.EnumType.values());

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
	private void registerItemModel(Item item) {
		registerItemModel(item, item.getRegistryName().toString());
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code modelLocation} as the domain/path and {@link "inventory"} as the variant.
	 *
	 * @param item          The Item
	 * @param modelLocation The model location
	 */
	private void registerItemModel(Item item, String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		registerItemModel(item, fullModelLocation);
	}

	/**
	 * Register a single model for an {@link Item}.
	 * <p>
	 * Uses {@code fullModelLocation} as the domain, path and variant.
	 *
	 * @param item              The Item
	 * @param fullModelLocation The full model location
	 */
	private void registerItemModel(Item item, ModelResourceLocation fullModelLocation) {
		ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
		registerItemModel(item, MeshDefinitionFix.create(stack -> fullModelLocation));
	}

	/**
	 * Register an {@link ItemMeshDefinition} for an {@link Item}.
	 *
	 * @param item           The Item
	 * @param meshDefinition The ItemMeshDefinition
	 */
	private void registerItemModel(Item item, ItemMeshDefinition meshDefinition) {
		itemsRegistered.add(item);
		ModelLoader.setCustomMeshDefinition(item, meshDefinition);
	}

	/**
	 * Register a model for each metadata value of an {@link Item} corresponding to the values in {@code values}.
	 * <p>
	 * Uses the registry name as the domain/path and {@code "[variantName]=[valueName]"} as the variant.
	 * <p>
	 * Uses {@link IVariant#getMeta()} to determine the metadata of each value.
	 *
	 * @param item        The Item
	 * @param variantName The variant name
	 * @param values      The values
	 * @param <T>         The value type
	 */
	private <T extends IVariant> void registerVariantItemModels(Item item, String variantName, T[] values) {
		for (T value : values) {
			registerItemModelForMeta(item, value.getMeta(), variantName + "=" + value.getName());
		}
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
	private void registerItemModelForMeta(Item item, int metadata, String variant) {
		registerItemModelForMeta(item, metadata, new ModelResourceLocation(item.getRegistryName(), variant));
	}

	/**
	 * Register a model for a metadata value of an {@link Item}.
	 * <p>
	 * Uses {@code modelResourceLocation} as the domain, path and variant.
	 *
	 * @param item                  The Item
	 * @param metadata              The metadata
	 * @param modelResourceLocation The full model location
	 */
	private void registerItemModelForMeta(Item item, int metadata, ModelResourceLocation modelResourceLocation) {
		itemsRegistered.add(item);
		ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
	}
}
