package choonster.testmod3.init;

import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BlockPipeBasic;
import choonster.testmod3.block.pipe.BlockPipeFluid;
import choonster.testmod3.item.block.ItemFluidTank;
import choonster.testmod3.tileentity.*;
import choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.IStringSerializable;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;

@SuppressWarnings("WeakerAccess")
public class ModBlocks {

	public static final Set<Block> BLOCKS = new HashSet<>();

	public static final BlockWaterGrass WATER_GRASS;
	public static final BlockLargeCollisionTest LARGE_COLLISION_TEST;
	public static final BlockRightClickTest RIGHT_CLICK_TEST;
	public static final BlockClientPlayerRightClick CLIENT_PLAYER_RIGHT_CLICK;
	public static final BlockRotatableLamp ROTATABLE_LAMP;
	public static final BlockItemCollisionTest ITEM_COLLISION_TEST;
	public static final BlockFluidTank FLUID_TANK;
	public static final BlockItemDebugger ITEM_DEBUGGER;
	public static final Block END_PORTAL_FRAME_FULL;
	public static final BlockColoredRotatable COLORED_ROTATABLE;
	public static final BlockColoredMultiRotatable COLORED_MULTI_ROTATABLE;
	public static final BlockPotionEffect POTION_EFFECT;
	public static final BlockVariants VARIANTS;
	public static final BlockClientPlayerRotation CLIENT_PLAYER_ROTATION;
	public static final BlockPigSpawnerRefiller PIG_SPAWNER_REFILLER;
	public static final BlockPlane MIRROR_PLANE;
	public static final BlockTestMod3 VANILLA_MODEL_TEST;
	public static final BlockTestMod3 FULLBRIGHT;
	public static final BlockTestMod3 NORMAL_BRIGHTNESS;
	public static final BlockMaxHealthSetter MAX_HEALTH_SETTER;
	public static final BlockMaxHealthGetter MAX_HEALTH_GETTER;
	public static final BlockSmallCollisionTest SMALL_COLLISION_TEST;
	public static final BlockModChest CHEST;

	public static final BlockPipeBasic PIPE_BASIC;
	public static final BlockPipeFluid PIPE_FLUID;

	public static final BlockSurvivalCommandBlock SURVIVAL_COMMAND_BLOCK;
	public static final BlockSurvivalCommandBlock REPEATING_SURVIVAL_COMMAND_BLOCK;
	public static final BlockSurvivalCommandBlock CHAIN_SURVIVAL_COMMAND_BLOCK;

	public static final BlockColouredSlab.ColouredSlabGroup STAINED_CLAY_SLABS;

	static {
		WATER_GRASS = registerBlock(new BlockWaterGrass());
		LARGE_COLLISION_TEST = registerBlock(new BlockLargeCollisionTest());
		RIGHT_CLICK_TEST = registerBlock(new BlockRightClickTest());
		CLIENT_PLAYER_RIGHT_CLICK = registerBlock(new BlockClientPlayerRightClick());
		ROTATABLE_LAMP = registerBlock(new BlockRotatableLamp());
		ITEM_COLLISION_TEST = registerBlock(new BlockItemCollisionTest());
		FLUID_TANK = registerBlock(new BlockFluidTank(), ItemFluidTank::new);
		ITEM_DEBUGGER = registerBlock(new BlockItemDebugger());
		END_PORTAL_FRAME_FULL = registerBlock(new BlockTestMod3(Material.ROCK, "endPortalFrameFull"));
		COLORED_ROTATABLE = registerBlock(new BlockColoredRotatable(Material.CLOTH, "coloredRotatable"), ItemCloth::new);
		COLORED_MULTI_ROTATABLE = registerBlock(new BlockColoredMultiRotatable(Material.CLOTH, "coloredMultiRotatable"), ItemCloth::new);
		POTION_EFFECT = registerBlock(new BlockPotionEffect());
		VARIANTS = registerBlock(new BlockVariants(Material.IRON), block -> new ItemMultiTexture(block, block, BlockVariants.EnumType.getNames()));
		CLIENT_PLAYER_ROTATION = registerBlock(new BlockClientPlayerRotation());
		PIG_SPAWNER_REFILLER = registerBlock(new BlockPigSpawnerRefiller());
		MIRROR_PLANE = registerBlock(new BlockPlane(Material.IRON, "mirrorPlane"));
		VANILLA_MODEL_TEST = registerBlock(new BlockTestMod3(Material.IRON, "vanillaModelTest"));
		FULLBRIGHT = registerBlock(new BlockTestMod3(Material.ROCK, "fullbright"));
		NORMAL_BRIGHTNESS = registerBlock(new BlockTestMod3(Material.ROCK, "normalBrightness"));
		MAX_HEALTH_SETTER = registerBlock(new BlockMaxHealthSetter());
		MAX_HEALTH_GETTER = registerBlock(new BlockMaxHealthGetter());
		SMALL_COLLISION_TEST = registerBlock(new BlockSmallCollisionTest());
		CHEST = registerBlock(new BlockModChest());

		PIPE_BASIC = registerBlock(new BlockPipeBasic("basicPipe"));
		PIPE_FLUID = registerBlock(new BlockPipeFluid());

		STAINED_CLAY_SLABS = new BlockColouredSlab.ColouredSlabGroup("stainedClaySlab", Material.ROCK);
		registerSlabGroup(STAINED_CLAY_SLABS.low);
		registerSlabGroup(STAINED_CLAY_SLABS.high);

		SURVIVAL_COMMAND_BLOCK = registerBlock(new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.REDSTONE, "survivalCommandBlock"));
		REPEATING_SURVIVAL_COMMAND_BLOCK = registerBlock(new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.AUTO, "repeatingSurvivalCommandBlock"));
		CHAIN_SURVIVAL_COMMAND_BLOCK = registerBlock(new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.SEQUENCE, "chainSurvivalCommandBlock"));
	}

	public static void registerBlocks() {
		// Dummy method to make sure the static initialiser runs
	}

	/**
	 * Register a Block with the default ItemBlock class.
	 *
	 * @param block   The Block instance
	 * @param <BLOCK> The Block type
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

		BLOCKS.add(block);
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
		registerTileEntity(TileEntityModChest.class);
	}

	private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
		GameRegistry.registerTileEntity(tileEntityClass, Constants.RESOURCE_PREFIX + tileEntityClass.getSimpleName().replaceFirst("TileEntity", ""));
	}
}
