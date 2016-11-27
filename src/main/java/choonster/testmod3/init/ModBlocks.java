package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BlockPipeBasic;
import choonster.testmod3.block.pipe.BlockPipeFluid;
import choonster.testmod3.item.block.ItemFluidTank;
import choonster.testmod3.tileentity.*;
import choonster.testmod3.util.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.fml.common.registry.IForgeRegistry;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModBlocks {
	@ObjectHolder("water_grass")
	public static final BlockWaterGrass WATER_GRASS = new BlockWaterGrass();

	@ObjectHolder("large_collision_test")
	public static final BlockLargeCollisionTest LARGE_COLLISION_TEST = new BlockLargeCollisionTest();

	@ObjectHolder("right_click_test")
	public static final BlockRightClickTest RIGHT_CLICK_TEST = new BlockRightClickTest();

	@ObjectHolder("client_player_right_click")
	public static final BlockClientPlayerRightClick CLIENT_PLAYER_RIGHT_CLICK = new BlockClientPlayerRightClick();

	@ObjectHolder("rotatable_lamp")
	public static final BlockRotatableLamp ROTATABLE_LAMP = new BlockRotatableLamp();

	@ObjectHolder("item_collision_test")
	public static final BlockItemCollisionTest ITEM_COLLISION_TEST = new BlockItemCollisionTest();

	@ObjectHolder("fluid_tank")
	public static final BlockFluidTank FLUID_TANK = new BlockFluidTank();

	@ObjectHolder("item_debugger")
	public static final BlockItemDebugger ITEM_DEBUGGER = new BlockItemDebugger();

	@ObjectHolder("end_portal_frame_full")
	public static final Block END_PORTAL_FRAME_FULL = new BlockTestMod3(Material.ROCK, "end_portal_frame_full");

	@ObjectHolder("colored_rotatable")
	public static final BlockColoredRotatable COLORED_ROTATABLE = new BlockColoredRotatable(Material.CLOTH, "colored_rotatable");

	@ObjectHolder("colored_multi_rotatable")
	public static final BlockColoredMultiRotatable COLORED_MULTI_ROTATABLE = new BlockColoredMultiRotatable(Material.CLOTH, "colored_multi_rotatable");

	@ObjectHolder("potion_effect")
	public static final BlockPotionEffect POTION_EFFECT = new BlockPotionEffect();

	@ObjectHolder("variants")
	public static final BlockVariants VARIANTS = new BlockVariants(Material.IRON);

	@ObjectHolder("client_player_rotation")
	public static final BlockClientPlayerRotation CLIENT_PLAYER_ROTATION = new BlockClientPlayerRotation();

	@ObjectHolder("pig_spawner_refiller")
	public static final BlockPigSpawnerRefiller PIG_SPAWNER_REFILLER = new BlockPigSpawnerRefiller();

	@ObjectHolder("mirror_plane")
	public static final BlockPlane MIRROR_PLANE = new BlockPlane(Material.IRON, "mirror_plane");

	@ObjectHolder("vanilla_model_test")
	public static final BlockTestMod3 VANILLA_MODEL_TEST = new BlockTestMod3(Material.IRON, "vanilla_model_test");

	@ObjectHolder("fullbright")
	public static final BlockTestMod3 FULLBRIGHT = (BlockTestMod3) new BlockTestMod3(Material.ROCK, "fullbright").setLightLevel(1);

	@ObjectHolder("normal_brightness")
	public static final BlockTestMod3 NORMAL_BRIGHTNESS = new BlockTestMod3(Material.ROCK, "normal_brightness");

	@ObjectHolder("max_health_setter")
	public static final BlockMaxHealthSetter MAX_HEALTH_SETTER = new BlockMaxHealthSetter();

	@ObjectHolder("max_health_getter")
	public static final BlockMaxHealthGetter MAX_HEALTH_GETTER = new BlockMaxHealthGetter();

	@ObjectHolder("small_collision_test")
	public static final BlockSmallCollisionTest SMALL_COLLISION_TEST = new BlockSmallCollisionTest();

	@ObjectHolder("chest")
	public static final BlockModChest CHEST = new BlockModChest();

	@ObjectHolder("hidden")
	public static final BlockHidden HIDDEN = new BlockHidden(Material.ROCK, "hidden");

	@ObjectHolder("basic_pipe")
	public static final BlockPipeBasic BASIC_PIPE = new BlockPipeBasic("basic_pipe");

	@ObjectHolder("fluid_pipe")
	public static final BlockPipeFluid FLUID_PIPE = new BlockPipeFluid();

	@ObjectHolder("survival_command_block")
	public static final BlockSurvivalCommandBlock SURVIVAL_COMMAND_BLOCK = new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.REDSTONE, "survival_command_block");

	@ObjectHolder("repeating_survival_command_block")
	public static final BlockSurvivalCommandBlock REPEATING_SURVIVAL_COMMAND_BLOCK = new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.AUTO, "repeating_survival_command_block");

	@ObjectHolder("chain_survival_command_block")
	public static final BlockSurvivalCommandBlock CHAIN_SURVIVAL_COMMAND_BLOCK = new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.SEQUENCE, "chain_survival_command_block");

	public static final BlockSaplingTestMod3 SAPLING = new BlockSaplingTestMod3();

	public static class Slabs {
		public static final BlockColouredSlab.ColouredSlabGroup STAINED_CLAY_SLABS = new BlockColouredSlab.ColouredSlabGroup("stained_clay_slab", Material.ROCK);
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler {
		public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			final Block[] blocks = {
					WATER_GRASS,
					LARGE_COLLISION_TEST,
					RIGHT_CLICK_TEST,
					CLIENT_PLAYER_RIGHT_CLICK,
					ROTATABLE_LAMP,
					ITEM_COLLISION_TEST,
					FLUID_TANK,
					ITEM_DEBUGGER,
					END_PORTAL_FRAME_FULL,
					COLORED_ROTATABLE,
					COLORED_MULTI_ROTATABLE,
					POTION_EFFECT,
					VARIANTS,
					CLIENT_PLAYER_ROTATION,
					PIG_SPAWNER_REFILLER,
					MIRROR_PLANE,
					VANILLA_MODEL_TEST,
					FULLBRIGHT,
					NORMAL_BRIGHTNESS,
					MAX_HEALTH_SETTER,
					MAX_HEALTH_GETTER,
					SMALL_COLLISION_TEST,
					CHEST,
					HIDDEN,
					BASIC_PIPE,
					FLUID_PIPE,
					SURVIVAL_COMMAND_BLOCK,
					REPEATING_SURVIVAL_COMMAND_BLOCK,
					CHAIN_SURVIVAL_COMMAND_BLOCK,
					SAPLING,
			};

			registry.registerAll(blocks);

			registerSlabGroup(registry, Slabs.STAINED_CLAY_SLABS.high);
			registerSlabGroup(registry, Slabs.STAINED_CLAY_SLABS.low);
		}

		/**
		 * Register the {@link Block}s of a {@link BlockSlabTestMod3.SlabGroup}.
		 *
		 * @param registry  The registry
		 * @param slabGroup The slab group
		 */
		private static void registerSlabGroup(IForgeRegistry<Block> registry, BlockSlabTestMod3.SlabGroup<?, ?, ?> slabGroup) {
			registry.register(slabGroup.singleSlab);
			registry.register(slabGroup.doubleSlab);
		}

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(RegistryEvent.Register<Item> event) {
			final ItemBlock[] items = {
					new ItemBlock(WATER_GRASS),
					new ItemBlock(LARGE_COLLISION_TEST),
					new ItemBlock(RIGHT_CLICK_TEST),
					new ItemBlock(CLIENT_PLAYER_RIGHT_CLICK),
					new ItemBlock(ROTATABLE_LAMP),
					new ItemBlock(ITEM_COLLISION_TEST),
					new ItemFluidTank(FLUID_TANK),
					new ItemBlock(ITEM_DEBUGGER),
					new ItemBlock(END_PORTAL_FRAME_FULL),
					new ItemCloth(COLORED_ROTATABLE),
					new ItemCloth(COLORED_MULTI_ROTATABLE),
					new ItemBlock(POTION_EFFECT),
					new ItemMultiTexture(VARIANTS, VARIANTS, VARIANTS::getName),
					new ItemBlock(CLIENT_PLAYER_ROTATION),
					new ItemBlock(PIG_SPAWNER_REFILLER),
					new ItemBlock(MIRROR_PLANE),
					new ItemBlock(VANILLA_MODEL_TEST),
					new ItemBlock(FULLBRIGHT),
					new ItemBlock(NORMAL_BRIGHTNESS),
					new ItemBlock(MAX_HEALTH_SETTER),
					new ItemBlock(MAX_HEALTH_GETTER),
					new ItemBlock(SMALL_COLLISION_TEST),
					new ItemBlock(CHEST),
					new ItemBlock(HIDDEN),
					new ItemBlock(BASIC_PIPE),
					new ItemBlock(FLUID_PIPE),
					new ItemBlock(SURVIVAL_COMMAND_BLOCK),
					new ItemBlock(REPEATING_SURVIVAL_COMMAND_BLOCK),
					new ItemBlock(CHAIN_SURVIVAL_COMMAND_BLOCK),
					new ItemMultiTexture(SAPLING, SAPLING, BlockSaplingTestMod3::getName),
					Slabs.STAINED_CLAY_SLABS.low.item,
					Slabs.STAINED_CLAY_SLABS.high.item,
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final ItemBlock item : items) {
				registry.register(item.setRegistryName(item.getBlock().getRegistryName()));
				ITEM_BLOCKS.add(item);
			}
		}
	}

	public static void registerTileEntities() {
		registerTileEntity(TileEntitySurvivalCommandBlock.class);
		registerTileEntity(TileEntityFluidTank.class);
		registerTileEntity(TileEntityColoredRotatable.class);
		registerTileEntity(TileEntityColoredMultiRotatable.class);
		registerTileEntity(TileEntityPotionEffect.class);
		registerTileEntity(TileEntityModChest.class);
		registerTileEntity(TileEntityHidden.class);
	}

	private static void registerTileEntity(Class<? extends TileEntity> tileEntityClass) {
		GameRegistry.registerTileEntity(tileEntityClass, Constants.RESOURCE_PREFIX + tileEntityClass.getSimpleName().replaceFirst("TileEntity", ""));
	}
}
