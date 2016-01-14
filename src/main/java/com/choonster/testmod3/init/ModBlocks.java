package com.choonster.testmod3.init;

import com.choonster.testmod3.block.*;
import com.choonster.testmod3.block.pipe.BlockPipeBasic;
import com.choonster.testmod3.block.pipe.BlockPipeFluid;
import com.choonster.testmod3.item.block.ItemColoredMod;
import com.choonster.testmod3.item.block.ItemFluidTank;
import com.choonster.testmod3.item.block.ItemMultiTextureMod;
import com.choonster.testmod3.tileentity.*;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.HashSet;
import java.util.Set;

public class ModBlocks {

	public static final Set<Block> blocks = new HashSet<>();

	public static BlockWaterGrass waterGrass;
	public static BlockLargeCollisionTest largeCollisionTest;
	public static BlockRightClickTest rightClickTest;
	public static BlockClientPlayerRightClick clientPlayerRightClick;
	public static BlockRotatableLamp rotatableLamp;
	public static BlockItemCollisionTest itemCollisionTest;
	public static BlockSurvivalCommandBlock survivalCommandBlock;
	public static BlockFluidTank fluidTank;
	public static BlockItemDebugger itemDebugger;
	public static Block endPortalFrameFull;
	public static BlockColoredRotatable coloredRotatable;
	public static BlockColoredMultiRotatable coloredMultiRotatable;
	public static BlockPotionEffect potionEffect;
	public static BlockVariants variants;
	public static BlockClientPlayerRotation clientPlayerRotation;

	public static BlockPipeBasic pipeBasic;
	public static BlockPipeFluid pipeFluid;

	public static void registerBlocks() {
		waterGrass = registerBlock(new BlockWaterGrass(), ItemColoredMod.class, true);
		largeCollisionTest = registerBlock(new BlockLargeCollisionTest());
		rightClickTest = registerBlock(new BlockRightClickTest());
		clientPlayerRightClick = registerBlock(new BlockClientPlayerRightClick());
		rotatableLamp = registerBlock(new BlockRotatableLamp());
		itemCollisionTest = registerBlock(new BlockItemCollisionTest());
		survivalCommandBlock = registerBlock(new BlockSurvivalCommandBlock());
		fluidTank = registerBlock(new BlockFluidTank(), ItemFluidTank.class);
		itemDebugger = registerBlock(new BlockItemDebugger());
		endPortalFrameFull = registerBlock(new BlockTestMod3(Material.rock, "endPortalFrameFull"));
		coloredRotatable = registerBlock(new BlockColoredRotatable(Material.cloth, "coloredRotatable"), ItemCloth.class);
		coloredMultiRotatable = registerBlock(new BlockColoredMultiRotatable(Material.cloth, "coloredMultiRotatable"), ItemCloth.class);
		potionEffect = registerBlock(new BlockPotionEffect());
		variants = registerBlock(new BlockVariants(Material.iron), ItemMultiTextureMod.class, BlockVariants.EnumType.getNames(), true);
		clientPlayerRotation = registerBlock(new BlockClientPlayerRotation());

		pipeBasic = registerBlock(new BlockPipeBasic("basicPipe"));
		pipeFluid = registerBlock(new BlockPipeFluid());
	}

	/**
	 * Register a Block with the default ItemBlock class.
	 *
	 * @param block The Block instance
	 * @param <T>   The Block type
	 * @return The Block instance
	 */
	private static <T extends Block> T registerBlock(T block) {
		GameRegistry.registerBlock(block);
		blocks.add(block);
		return block;
	}

	/**
	 * Register a Block with a custom ItemBlock class.
	 *
	 * @param block           The Block instance
	 * @param itemClass       The ItemBlock class
	 * @param constructorArgs Arguments to pass to the ItemBlock constructor
	 * @param <T>             The Block type
	 * @return The Block instance
	 */
	private static <T extends Block> T registerBlock(T block, Class<? extends ItemBlock> itemClass, Object... constructorArgs) {
		GameRegistry.registerBlock(block, itemClass, constructorArgs);
		blocks.add(block);
		return block;
	}

	public static void registerTileEntities() {
		registerTileEntity(TileEntitySurvivalCommandBlock.class);
		registerTileEntity(TileEntityFluidTank.class);
		registerTileEntity(TileEntityColoredRotatable.class);
		registerTileEntity(TileEntityColoredMultiRotatable.class);
		registerTileEntity(TileEntityPotionEffect.class);
	}

	private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
		GameRegistry.registerTileEntity(tileEntityClass, Constants.RESOURCE_PREFIX + tileEntityClass.getSimpleName().replaceFirst("TileEntity", ""));
	}
}
