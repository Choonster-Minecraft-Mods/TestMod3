package com.choonster.testmod3.client.util;

import com.choonster.testmod3.TestMod3;
import com.choonster.testmod3.fluid.FluidTestMod3;
import com.choonster.testmod3.init.ModFluids;
import com.choonster.testmod3.init.ModItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemModelMesher;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.util.IRegistry;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;


public class ModModelManager {
	public static final ModModelManager INSTANCE = new ModModelManager();

	public static final String RESOURCE_PREFIX = TestMod3.MODID + ":";

	private ModModelManager() {

	}

	public void registerVariants() {
		registerFluidBlock(ModFluids.blockStatic);
		registerFluidBlock(ModFluids.blockStaticGas);
		registerFluidBlock(ModFluids.blockNormal);
		registerFluidBlock(ModFluids.blockNormalGas);
	}

	private void registerFluidBlock(BlockFluidBase fluidBlock) {
		ModelLoader.setCustomStateMapper(fluidBlock, (new StateMap.Builder()).addPropertiesToIgnore(BlockFluidBase.LEVEL).build());
	}


	private ItemModelMesher itemModelMesher;

	public void registerItemModels() {
		itemModelMesher = Minecraft.getMinecraft().getRenderItem().getItemModelMesher();

		registerItemModel(ModItems.solarisRecord);
		registerItemModel(ModItems.woodenAxe);

		itemModelMesher = null;
	}

	private void registerItemModel(Item item) {
		itemModelMesher.register(item, 0, new ModelResourceLocation((ResourceLocation) Item.itemRegistry.getNameForObject(item), "inventory"));
	}

	private IRegistry modelRegistry;

	/**
	 * The fluid rendering setup associated with this class was originally created by kirderf1 for www.github.com/mraof/minestuck
	 * When copying this code, please keep this comment or refer back to the original source in another way, if possible.
	 */
	@SubscribeEvent
	public void onModelBakeEvent(ModelBakeEvent event) {
		modelRegistry = event.modelRegistry;

		registerFluidModelAndIcons(ModFluids.fluidStatic);
		registerFluidModelAndIcons(ModFluids.fluidStaticGas);
		registerFluidModelAndIcons(ModFluids.fluidNormal);
		registerFluidModelAndIcons(ModFluids.fluidNormalGas);

		modelRegistry = null;
	}

	private void registerFluidModelAndIcons(FluidTestMod3 fluid) {
		String stateName = fluid.getUnlocalizedName(); // fluid.foo.bar
		String textureName = RESOURCE_PREFIX + "blocks/" + stateName.replace('.', '_'); // testmod3:blocks/fluid_foo_bar

		modelRegistry.putObject(new ModelResourceLocation(RESOURCE_PREFIX + stateName, "normal"), new FluidBlockModel());

		TextureMap textureMap = Minecraft.getMinecraft().getTextureMapBlocks();

		if (fluid.hasFlowIcon()) {
			fluid.setIcons(textureMap.getAtlasSprite(textureName + "_still"), textureMap.getAtlasSprite(textureName + "_flow"));
		} else {
			fluid.setIcons(textureMap.getAtlasSprite(textureName));
		}
	}
}
