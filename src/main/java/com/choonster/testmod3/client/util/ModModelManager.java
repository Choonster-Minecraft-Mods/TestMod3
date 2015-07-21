package com.choonster.testmod3.client.util;

import com.choonster.testmod3.init.ModFluids;
import com.choonster.testmod3.init.ModItems;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.IFluidBlock;


public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	private static final String FLUID_MODEL_PATH = Constants.RESOURCE_PREFIX + "fluid";

	private ModModelManager() {
	}

	public void registerAllModels() {
		registerFluidModels();
		registerBlockModels();

		registerItemVariants();
		registerItemModels();
	}

	private void registerFluidModels() {
		for (IFluidBlock fluidBlock : ModFluids.fluidBlocks) {
			registerFluidModel(fluidBlock);
		}
	}

	private void registerFluidModel(IFluidBlock fluidBlock) {
		Item item = Item.getItemFromBlock((Block) fluidBlock);

		ModelBakery.addVariantName(item);

		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, stack -> modelResourceLocation);

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	private void registerBlockModels() {
	}

	private void registerBlockItemModel(Block block) {
		registerItemModel(Item.getItemFromBlock(block));
	}

	private void registerBlockItemModel(Block block, String modelLocation) {
		registerItemModel(Item.getItemFromBlock(block), modelLocation);
	}

	private void registerItemVariants() {
		ModelBakery.addVariantName(ModItems.modelTest, "testmod3:modeltest"); // Add a variant for the default model
		for (int stage = 0; stage < 3; stage++) { // Add a variant for each stage's model
			ModelBakery.addVariantName(ModItems.modelTest, "testmod3:modeltest_" + stage);
		}
	}

	private void registerItemModels() {
		registerItemModel(ModItems.entityInteractionTest);
		registerItemModel(ModItems.solarisRecord);
		registerItemModel(ModItems.woodenAxe);
		registerItemModel(ModItems.modelTest); // Only use the default model, the stages are handled by ItemModelTest#getModel
	}

	private void registerItemModel(Item item) {
		registerItemModel(item, Item.itemRegistry.getNameForObject(item).toString());
	}

	private void registerItemModel(Item item, String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		ModelLoader.setCustomMeshDefinition(item, stack -> fullModelLocation);
	}
}
