package com.choonster.testmod3.client.model;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.init.ModBlocks;
import com.choonster.testmod3.init.ModFluids;
import com.choonster.testmod3.init.ModItems;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCommandBlock;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashSet;
import java.util.Set;

@SideOnly(Side.CLIENT)
public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	private static final String FLUID_MODEL_PATH = Constants.RESOURCE_PREFIX + "fluid";

	private ModModelManager() {
	}

	public void registerAllModels() {
		registerFluidModels();
		registerBlockModels();
		registerItemModels();
	}

	private void registerFluidModels() {
		ModFluids.modFluidBlocks.forEach(this::registerFluidModel);
	}

	private void registerFluidModel(IFluidBlock fluidBlock) {
		Item item = Item.getItemFromBlock((Block) fluidBlock);

		ModelBakery.registerItemVariants(item);

		ModelResourceLocation modelResourceLocation = new ModelResourceLocation(FLUID_MODEL_PATH, fluidBlock.getFluid().getName());

		ModelLoader.setCustomMeshDefinition(item, MeshDefinitionFix.create(stack -> modelResourceLocation));

		ModelLoader.setCustomStateMapper((Block) fluidBlock, new StateMapperBase() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState p_178132_1_) {
				return modelResourceLocation;
			}
		});
	}

	private void registerBlockModels() {
		ModelLoader.setCustomStateMapper(ModBlocks.waterGrass, new StateMap.Builder().ignore(BlockLiquid.LEVEL).build());
		ModelLoader.setCustomStateMapper(ModBlocks.survivalCommandBlock, new StateMap.Builder().ignore(BlockCommandBlock.TRIGGERED).build());

		registerBlockItemModel(ModBlocks.waterGrass, "minecraft:tall_grass");
		registerBlockItemModel(ModBlocks.largeCollisionTest, "minecraft:white_wool");
		registerBlockItemModel(ModBlocks.rightClickTest, "minecraft:black_stained_glass");
		registerBlockItemModel(ModBlocks.clientPlayerRightClick, "minecraft:heavy_weighted_pressure_plate");

		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.coloredRotatable)); // Don't load the default item model
		ModelBakery.registerItemVariants(Item.getItemFromBlock(ModBlocks.coloredMultiRotatable));
		for (EnumDyeColor color : EnumDyeColor.values()) {
			registerBlockItemModel(ModBlocks.coloredRotatable, color.getMetadata(), new ModelResourceLocation(Constants.RESOURCE_PREFIX + "coloredRotatable", String.format("color=%s,facing=north", color.getName())));
			registerBlockItemModel(ModBlocks.coloredMultiRotatable, color.getMetadata(), new ModelResourceLocation(Constants.RESOURCE_PREFIX + "coloredMultiRotatable", String.format("color=%s,face_rotation=up,facing=north", color.getName())));
		}

		ModBlocks.blocks.forEach(this::registerBlockItemModel);
	}

	private void registerBlockItemModel(Block block) {
		registerItemModel(Item.getItemFromBlock(block));
	}

	private void registerBlockItemModel(Block block, String modelLocation) {
		registerItemModel(Item.getItemFromBlock(block), modelLocation);
	}

	private void registerBlockItemModel(Block block, int metadata, ModelResourceLocation modelResourceLocation) {
		registerItemModel(Item.getItemFromBlock(block), metadata, modelResourceLocation);
	}

	private final Set<Item> itemsRegistered = new HashSet<>();

	private void registerItemModels() {
		for (int stage = 0; stage < 3; stage++) { // Add a variant for each stage's model
			ModelBakery.registerItemVariants(ModItems.modelTest, new ResourceLocation(TestMod3.MODID, "modeltest_" + stage));
			ModelBakery.registerItemVariants(ModItems.modBow, new ResourceLocation(TestMod3.MODID, "bow_pulling_" + stage));
		}

		ModelBakery.registerItemVariants(ModItems.slingshot, new ResourceLocation(TestMod3.MODID, "slingshot_pulled"));
		ModelBakery.registerItemVariants(ModItems.bucket, ModelDynBucket.LOCATION);

		// Register items with custom model names first
		registerItemModel(ModItems.snowballLauncher, "minecraft:fishing_rod");
		registerItemModel(ModItems.unicodeTooltips, "minecraft:rabbit");
		registerItemModel(ModItems.swapTestA, "minecraft:brick");
		registerItemModel(ModItems.swapTestB, "minecraft:netherbrick");
		registerItemModel(ModItems.blockDebugger, "minecraft:nether_star");
		registerItemModel(ModItems.woodenHarvestSword, "minecraft:wooden_sword");
		registerItemModel(ModItems.diamondHarvestSword, "minecraft:diamond_sword");
		registerItemModel(ModItems.clearer, "minecraft:nether_star");
		registerItemModel(ModItems.bucket, ModelDynBucket.LOCATION);

		// Then register items with default model names
		ModItems.items.forEach(this::registerItemModel);
	}

	private void registerItemModel(Item item) {
		registerItemModel(item, Item.itemRegistry.getNameForObject(item).toString());
	}

	private void registerItemModel(Item item, String modelLocation) {
		final ModelResourceLocation fullModelLocation = new ModelResourceLocation(modelLocation, "inventory");
		registerItemModel(item, fullModelLocation);
	}

	private void registerItemModel(Item item, ModelResourceLocation fullModelLocation) {
		ModelBakery.registerItemVariants(item, fullModelLocation); // Ensure the custom model is loaded and prevent the default model from being loaded
		registerItemModel(item, MeshDefinitionFix.create(stack -> fullModelLocation));
	}

	private void registerItemModel(Item item, ItemMeshDefinition meshDefinition) {
		if (!itemsRegistered.contains(item)) { // Don't replace an existing registration
			itemsRegistered.add(item);
			ModelLoader.setCustomMeshDefinition(item, meshDefinition);
		}
	}

	private void registerItemModel(Item item, int metadata, ModelResourceLocation modelResourceLocation) {
		if (!itemsRegistered.contains(item)) {
			itemsRegistered.add(item);
		}

		ModelLoader.setCustomModelResourceLocation(item, metadata, modelResourceLocation);
	}
}
