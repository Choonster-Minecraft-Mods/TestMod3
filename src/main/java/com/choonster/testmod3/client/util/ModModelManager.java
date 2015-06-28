package com.choonster.testmod3.client.util;

import com.choonster.testmod3.init.ModFluids;
import com.choonster.testmod3.init.ModItems;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.IFluidBlock;


public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	private static final String FLUID_MODEL_PATH = Constants.RESOURCE_PREFIX + "fluid";

	private ModModelManager() {

	}

	public void registerFluidModels() {
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

	private ItemModelMesher itemModelMesher;

	public void registerItemModels() {
		itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		registerItemModel(ModItems.entityInteractionTest);
		registerItemModel(ModItems.solarisRecord);
		registerItemModel(ModItems.woodenAxe);

		itemModelMesher = null;
	}

	private void registerItemModel(Item item) {
		itemModelMesher.register(item, 0, new ModelResourceLocation((ResourceLocation) Item.itemRegistry.getNameForObject(item), "inventory"));
	}
}
