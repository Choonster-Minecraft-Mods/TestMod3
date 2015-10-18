package com.choonster.testmod3.client.model;

import com.choonster.testmod3.init.ModBlocks;
import com.choonster.testmod3.init.ModFluids;
import com.choonster.testmod3.init.ModItems;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;

import java.util.HashSet;
import java.util.Set;


public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	private static final String FLUID_MODEL_PATH = Constants.RESOURCE_PREFIX + "fluid";

	private ModModelManager() {
	}

	public void registerAllModels() {
		registerFluidModels();
		registerBucketModels();

		registerBlockModels();

		registerItemVariants();
		registerItemModels();
	}

	private void registerFluidModels() {
		ModFluids.fluidBlocks.forEach(this::registerFluidModel);
	}

	private void registerFluidModel(IFluidBlock fluidBlock) {
		Item item = Item.getItemFromBlock((Block) fluidBlock);

		ModelBakery.addVariantName(item);

		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	private void registerBucketModels() {
		itemsRegistered.add(ModItems.bucket);

		for (FluidStack fluidStack : ModItems.bucket.fluids) {
			ModelBakery.addVariantName(ModItems.bucket, "testmod3:bucket/" + fluidStack.getFluid().getName());
		}

		ModelLoader.setCustomMeshDefinition(ModItems.bucket, MeshDefinitionFix.create(stack -> {
					FluidStack fluidStack = ModItems.bucket.getFluid(stack);
					return fluidStack != null ? new ModelResourceLocation("testmod3:bucket/" + fluidStack.getFluid().getName(), "inventory") : null;
				}
		));
	}

	private void registerBlockModels() {
		ModelLoader.setCustomStateMapper(ModBlocks.waterGrass, new StateMap.Builder().addPropertiesToIgnore(BlockLiquid.LEVEL).build());
		ModelLoader.setCustomStateMapper(ModBlocks.survivalCommandBlock, new StateMap.Builder().addPropertiesToIgnore(BlockCommandBlock.TRIGGERED).build());

		registerBlockItemModel(ModBlocks.waterGrass, "minecraft:tall_grass");
		registerBlockItemModel(ModBlocks.largeCollisionTest, "minecraft:white_wool");
		registerBlockItemModel(ModBlocks.rightClickTest, "minecraft:black_stained_glass");
		registerBlockItemModel(ModBlocks.clientPlayerRightClick, "minecraft:heavy_weighted_pressure_plate");

		ModBlocks.blocks.forEach(this::registerBlockItemModel);
	}

	private void registerBlockItemModel(Block block) {
		registerItemModel(Item.getItemFromBlock(block));
	}

	private void registerBlockItemModel(Block block, String modelLocation) {
		registerItemModel(Item.getItemFromBlock(block), modelLocation);
	}

	private void registerItemVariants() {
		for (int stage = 0; stage < 3; stage++) { // Add a variant for each stage's model
			ModelBakery.addVariantName(ModItems.modelTest, "testmod3:modeltest_" + stage);
		}

		ModelBakery.addVariantName(ModItems.slingshot, "testmod3:slingshot_pulled");
	}

	private Set<Item> itemsRegistered = new HashSet<>();

	private void registerItemModels() {
		// Register items with custom model names first
		registerItemModel(ModItems.snowballLauncher, "minecraft:fishing_rod");
		registerItemModel(ModItems.unicodeTooltips, "minecraft:rabbit");
		registerItemModel(ModItems.swapTestA, "minecraft:brick");
		registerItemModel(ModItems.swapTestB, "minecraft:netherbrick");
		registerItemModel(ModItems.blockDebugger, "minecraft:nether_star");

		// Then register items with default model names
		ModItems.items.forEach(this::registerItemModel);
	}

	private void registerItemModel(Item item) {
		registerItemModel(item, Item.itemRegistry.getNameForObject(item).toString());
	}

	private void registerItemModel(Item item, String modelLocation) {
		if (!itemsRegistered.contains(item)) { // Don't replace an existing registration
			itemsRegistered.add(item);

			final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
			ModelBakery.addVariantName(item, modelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
			ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> fullModelLocation));
		}
	}
}
