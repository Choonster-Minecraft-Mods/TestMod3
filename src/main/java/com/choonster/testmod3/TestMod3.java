package com.choonster.testmod3;

import com.choonster.testmod3.item.ItemEntityTest;
import com.choonster.testmod3.item.ToolWoodAxe;
import com.choonster.testmod3.recipe.ShapelessCuttingRecipe;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;

@Mod(modid = TestMod3.MODID, version = TestMod3.VERSION)
public class TestMod3 {
	public static final String MODID = "testmod3";
	public static final String VERSION = "1.0";

	public static CreativeTabs creativeTab;

	public static Item woodenAxe;
	public static Item entityTest;

	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {
		creativeTab = new CreativeTabExample();

		woodenAxe = new ToolWoodAxe(Item.ToolMaterial.WOOD).setCreativeTab(creativeTab).setUnlocalizedName("woodenAxe");
		GameRegistry.registerItem(woodenAxe, "wooden_axe_test");

		entityTest = new ItemEntityTest();
		GameRegistry.registerItem(entityTest, "entity_test");
	}

	@EventHandler
	public void init(FMLInitializationEvent event) {
		GameRegistry.addRecipe(new ShapelessCuttingRecipe(new ItemStack(Blocks.planks, 2, BlockPlanks.EnumType.OAK.getMetadata()), new ItemStack(Items.wooden_axe, 1, OreDictionary.WILDCARD_VALUE), new ItemStack(Blocks.log, 1, BlockPlanks.EnumType.OAK.getMetadata())));
	}
}
