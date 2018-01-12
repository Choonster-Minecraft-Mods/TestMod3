package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BlockPipeBasic;
import choonster.testmod3.block.pipe.BlockPipeFluid;
import choonster.testmod3.block.slab.BlockColouredSlab;
import choonster.testmod3.block.slab.ISlabGroupContainer;
import choonster.testmod3.item.block.ItemFluidTank;
import choonster.testmod3.tileentity.*;
import choonster.testmod3.util.Constants;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemCloth;
import net.minecraft.item.ItemMultiTexture;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static choonster.testmod3.util.InjectionUtil.Null;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModBlocks {

	public static final BlockWaterGrass WATER_GRASS = Null();

	public static final BlockLargeCollisionTest LARGE_COLLISION_TEST = Null();

	public static final BlockRightClickTest RIGHT_CLICK_TEST = Null();

	public static final BlockClientPlayerRightClick CLIENT_PLAYER_RIGHT_CLICK = Null();

	public static final BlockRotatableLamp ROTATABLE_LAMP = Null();

	public static final BlockItemCollisionTest ITEM_COLLISION_TEST = Null();

	public static final BlockFluidTank<TileEntityFluidTank> FLUID_TANK = Null();

	public static final BlockItemDebugger ITEM_DEBUGGER = Null();

	public static final Block END_PORTAL_FRAME_FULL = Null();

	public static final BlockColoredRotatable COLORED_ROTATABLE = Null();

	public static final BlockColoredMultiRotatable COLORED_MULTI_ROTATABLE = Null();

	public static final BlockPotionEffect POTION_EFFECT = Null();

	public static final BlockVariants VARIANTS = Null();

	public static final BlockClientPlayerRotation CLIENT_PLAYER_ROTATION = Null();

	public static final BlockPigSpawnerRefiller PIG_SPAWNER_REFILLER = Null();

	public static final BlockPlane MIRROR_PLANE = Null();

	public static final BlockTestMod3 VANILLA_MODEL_TEST = Null();

	public static final BlockTestMod3 FULLBRIGHT = Null();

	public static final BlockTestMod3 NORMAL_BRIGHTNESS = Null();

	public static final BlockMaxHealthSetter MAX_HEALTH_SETTER = Null();

	public static final BlockMaxHealthGetter MAX_HEALTH_GETTER = Null();

	public static final BlockSmallCollisionTest SMALL_COLLISION_TEST = Null();

	public static final BlockModChest CHEST = Null();

	public static final BlockHidden HIDDEN = Null();

	public static final BlockPipeBasic BASIC_PIPE = Null();

	public static final BlockPipeFluid FLUID_PIPE = Null();

	public static final BlockSurvivalCommandBlock SURVIVAL_COMMAND_BLOCK = Null();

	public static final BlockSurvivalCommandBlock REPEATING_SURVIVAL_COMMAND_BLOCK = Null();

	public static final BlockSurvivalCommandBlock CHAIN_SURVIVAL_COMMAND_BLOCK = Null();

	public static final BlockSaplingTestMod3 SAPLING = Null();

	public static final BlockInvisible INVISIBLE = Null();

	public static final BlockFluidTankRestricted FLUID_TANK_RESTRICTED = Null();

	public static final BlockTestMod3 PLANKS = Null();

	public static class Slabs {
		public static final BlockColouredSlab.ColouredSlabGroupContainer STAINED_CLAY_SLABS = new BlockColouredSlab.ColouredSlabGroupContainer("stained_clay_slab", Material.ROCK);
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID)
	public static class RegistrationHandler {
		public static final Set<ItemBlock> ITEM_BLOCKS = new HashSet<>();

		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			final Block[] blocks = {
					new BlockWaterGrass(),
					new BlockLargeCollisionTest(),
					new BlockRightClickTest(),
					new BlockClientPlayerRightClick(),
					new BlockRotatableLamp(),
					new BlockItemCollisionTest(),
					new BlockFluidTank<>("fluid_tank"),
					new BlockItemDebugger(),
					new BlockTestMod3(Material.ROCK, "end_portal_frame_full"),
					new BlockColoredRotatable(Material.CLOTH, "colored_rotatable"),
					new BlockColoredMultiRotatable(Material.CLOTH, "colored_multi_rotatable"),
					new BlockPotionEffect(),
					new BlockVariants(Material.IRON),
					new BlockClientPlayerRotation(),
					new BlockPigSpawnerRefiller(),
					new BlockPlane(Material.IRON, "mirror_plane"),
					new BlockTestMod3(Material.IRON, "vanilla_model_test"),
					new BlockTestMod3(Material.ROCK, "fullbright").setLightLevel(1),
					new BlockTestMod3(Material.ROCK, "normal_brightness"),
					new BlockMaxHealthSetter(),
					new BlockMaxHealthGetter(),
					new BlockSmallCollisionTest(),
					new BlockModChest(),
					new BlockHidden(Material.ROCK, "hidden"),
					new BlockPipeBasic("basic_pipe"),
					new BlockPipeFluid(),
					new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.REDSTONE, "survival_command_block"),
					new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.AUTO, "repeating_survival_command_block"),
					new BlockSurvivalCommandBlock(TileEntityCommandBlock.Mode.SEQUENCE, "chain_survival_command_block"),
					new BlockSaplingTestMod3(),
					new BlockInvisible(),
					new BlockFluidTankRestricted(),
					new BlockTestMod3(Material.WOOD, "planks"),
			};

			registry.registerAll(blocks);

			Slabs.STAINED_CLAY_SLABS.registerBlocks(registry);

			registerTileEntities();
		}

		/**
		 * Register this mod's {@link ItemBlock}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerItemBlocks(final RegistryEvent.Register<Item> event) {
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
					new ItemBlock(INVISIBLE),
					new ItemFluidTank(FLUID_TANK_RESTRICTED),
					new ItemBlock(PLANKS),
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}

			registerSlabGroupContainerItems(registry, Slabs.STAINED_CLAY_SLABS);
		}

		/**
		 * Register a {@link ISlabGroupContainer}'s {@link Item}s and add them to {@link #ITEM_BLOCKS}.
		 *
		 * @param registry  The Item registry
		 * @param container The container
		 */
		private static void registerSlabGroupContainerItems(final IForgeRegistry<Item> registry, final ISlabGroupContainer<?, ?, ?> container) {
			final List<ItemBlock> items = container.registerItems(registry);
			ITEM_BLOCKS.addAll(items);
		}
	}

	private static void registerTileEntities() {
		registerTileEntity(TileEntitySurvivalCommandBlock.class, "survival_command_block");
		registerTileEntity(TileEntityFluidTank.class, "fluid_tank");
		registerTileEntity(TileEntityColoredRotatable.class, "colored_rotatable");
		registerTileEntity(TileEntityColoredMultiRotatable.class, "colored_multi_rotatable");
		registerTileEntity(TileEntityPotionEffect.class, "potion_effect");
		registerTileEntity(TileEntityModChest.class, "mod_chest");
		registerTileEntity(TileEntityHidden.class, "hidden");
		registerTileEntity(TileEntityFluidTankRestricted.class, "fluid_tank_restricted");
	}

	private static void registerTileEntity(final Class<? extends TileEntity> tileEntityClass, final String name) {
		GameRegistry.registerTileEntity(tileEntityClass, Constants.RESOURCE_PREFIX + name);
	}
}
