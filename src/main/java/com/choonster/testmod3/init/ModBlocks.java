package com.choonster.testmod3.init;

import com.choonster.testmod3.block.*;
import com.choonster.testmod3.block.pipe.BlockPipeBasic;
import com.choonster.testmod3.block.pipe.BlockPipeFluid;
import com.choonster.testmod3.item.block.ItemFluidTank;
import com.choonster.testmod3.tileentity.*;
import com.choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
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
	public static BlockPigSpawnerRefiller pigSpawnerRefiller;
	public static BlockPlane mirrorPlane;
	public static BlockTestMod3 vanillaModelTest;
	public static BlockTestMod3 fullbright;
	public static BlockTestMod3 normalBrightness;

	public static BlockPipeBasic pipeBasic;
	public static BlockPipeFluid pipeFluid;

	public static BlockColouredSlab.ColouredSlabGroup stainedClaySlabs;

	public static void registerBlocks() {
		waterGrass = registerBlock(new BlockWaterGrass(), block -> new ItemColored(block, true));
		largeCollisionTest = registerBlock(new BlockLargeCollisionTest());
		rightClickTest = registerBlock(new BlockRightClickTest());
		clientPlayerRightClick = registerBlock(new BlockClientPlayerRightClick());
		rotatableLamp = registerBlock(new BlockRotatableLamp());
		itemCollisionTest = registerBlock(new BlockItemCollisionTest());
		survivalCommandBlock = registerBlock(new BlockSurvivalCommandBlock());
		fluidTank = registerBlock(new BlockFluidTank(), ItemFluidTank::new);
		itemDebugger = registerBlock(new BlockItemDebugger());
		endPortalFrameFull = registerBlock(new BlockTestMod3(Material.ROCK, "endPortalFrameFull"));
		coloredRotatable = registerBlock(new BlockColoredRotatable(Material.CLOTH, "coloredRotatable"), ItemCloth::new);
		coloredMultiRotatable = registerBlock(new BlockColoredMultiRotatable(Material.CLOTH, "coloredMultiRotatable"), ItemCloth::new);
		potionEffect = registerBlock(new BlockPotionEffect());
		variants = registerBlock(new BlockVariants(Material.IRON), block -> new ItemMultiTexture(block, block, BlockVariants.EnumType.getNames()));
		clientPlayerRotation = registerBlock(new BlockClientPlayerRotation());
		pigSpawnerRefiller = registerBlock(new BlockPigSpawnerRefiller());
		mirrorPlane = registerBlock(new BlockPlane(Material.IRON, "mirrorPlane"));
		vanillaModelTest = registerBlock(new BlockTestMod3(Material.IRON, "vanillaModelTest"));
		fullbright = registerBlock(new BlockTestMod3(Material.ROCK, "fullbright"));
		normalBrightness = registerBlock(new BlockTestMod3(Material.ROCK, "normalBrightness"));

		pipeBasic = registerBlock(new BlockPipeBasic("basicPipe"));
		pipeFluid = registerBlock(new BlockPipeFluid());

		stainedClaySlabs = new BlockColouredSlab.ColouredSlabGroup("stainedClaySlab", Material.ROCK);
		registerSlabGroup(stainedClaySlabs.low);
		registerSlabGroup(stainedClaySlabs.high);
	}

	/**
	 * Register a Block with the default ItemBlock class.
	 *
	 * @param block The Block instance
	 * @param <BLOCK>   The Block type
	 * @return The Block instance
	 */
	protected static <BLOCK extends Block> BLOCK registerBlock(BLOCK block) {
		return registerBlock(block, ItemBlock::new);
	}

	/**
	 * Register a Block with a custom ItemBlock class.
	 *
	 * @param <BLOCK>     The Block type
	 * @param block       The Block instance
	 * @param itemFactory A function that creates the ItemBlock instance, or null if no ItemBlock should be created
	 * @return The Block instance
	 */
	protected static <BLOCK extends Block> BLOCK registerBlock(BLOCK block, @Nullable Function<BLOCK, ItemBlock> itemFactory) {
		GameRegistry.register(block);

		if (itemFactory != null) {
			final ItemBlock itemBlock = itemFactory.apply(block);

			GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
		}

		blocks.add(block);
		return block;
	}

	/**
	 * Register a group of slab blocks
	 *
	 * @param slabGroup The slab group
	 */
	@SuppressWarnings("unchecked")
	private static <
			VARIANT extends Enum<VARIANT> & IStringSerializable,
			VARIANTS extends Iterable<VARIANT> & IStringSerializable,
			SLAB extends BlockSlabTestMod3<VARIANT, VARIANTS, SLAB>
			> void registerSlabGroup(BlockSlabTestMod3.SlabGroup<VARIANT, VARIANTS, SLAB> slabGroup) {
		registerBlock(slabGroup.singleSlab, slab -> new ItemSlab(slab, slabGroup.singleSlab, slabGroup.doubleSlab));
		registerBlock(slabGroup.doubleSlab, null); // No item form for the double slab
		slabGroup.setItem((ItemSlab) Item.getItemFromBlock(slabGroup.singleSlab));
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
