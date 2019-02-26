package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BlockPipeBasic;
import choonster.testmod3.block.pipe.BlockPipeFluid;
import choonster.testmod3.block.slab.BlockColouredSlab;
import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.block.variantgroup.IBlockVariantGroup;
import choonster.testmod3.item.block.ItemFluidTank;
import choonster.testmod3.tileentity.*;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.trees.*;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

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

	public static final BlockPotionEffect POTION_EFFECT = Null();

	public static final BlockClientPlayerRotation CLIENT_PLAYER_ROTATION = Null();

	public static final BlockPigSpawnerRefiller PIG_SPAWNER_REFILLER = Null();

	public static final BlockPlane MIRROR_PLANE = Null();

	public static final Block VANILLA_MODEL_TEST = Null();

	public static final Block FULLBRIGHT = Null();

	public static final Block NORMAL_BRIGHTNESS = Null();

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

	public static final BlockSaplingTestMod3 OAK_SAPLING = Null();

	public static final BlockSaplingTestMod3 SPRUCE_SAPLING = Null();

	public static final BlockSaplingTestMod3 BIRCH_SAPLING = Null();

	public static final BlockSaplingTestMod3 JUNGLE_SAPLING = Null();

	public static final BlockSaplingTestMod3 ACACIA_SAPLING = Null();

	public static final BlockSaplingTestMod3 DARK_OAK_SAPLING = Null();

	public static final BlockInvisible INVISIBLE = Null();

	public static final BlockFluidTankRestricted FLUID_TANK_RESTRICTED = Null();

	public static final Block PLANKS = Null();

	public static class VariantGroups {
		public static final BlockVariantGroup<EnumDyeColor, BlockColoredRotatable> COLORED_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<EnumDyeColor, BlockColoredRotatable>create()
				.groupName("rotatable_block")
				.variants(EnumDyeColor.values())
				.blockPropertiesFactory(color -> Block.Properties.create(Material.CLOTH, color))
				.blockFactory(BlockColoredRotatable::new)
				.build();

		public static final BlockVariantGroup<EnumDyeColor, BlockColoredMultiRotatable> COLORED_MULTI_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<EnumDyeColor, BlockColoredMultiRotatable>create()
				.groupName("multi_rotatable_block")
				.variants(EnumDyeColor.values())
				.blockPropertiesFactory(color -> Block.Properties.create(Material.CLOTH, color))
				.blockFactory(BlockColoredMultiRotatable::new)
				.build();

		public static final BlockVariantGroup<BlockVariants.EnumType, BlockVariants> VARIANTS_BLOCKS = BlockVariantGroup.Builder.<BlockVariants.EnumType, BlockVariants>create()
				.groupName("variants_block")
				.suffix()
				.variants(BlockVariants.EnumType.values())
				.blockPropertiesFactory(type -> Block.Properties.create(Material.IRON))
				.blockFactory(BlockVariants::new)
				.build();

		public static final BlockVariantGroup<EnumDyeColor, BlockColouredSlab> TERRACOTTA_SLABS = BlockVariantGroup.Builder.<EnumDyeColor, BlockColouredSlab>create()
				.groupName("terracotta_slab")
				.variants(EnumDyeColor.values())
				.blockPropertiesFactory(color -> Block.Properties.create(Material.ROCK, color))
				.blockFactory(BlockColouredSlab::new)
				.build();
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
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
					new BlockWaterGrass(Block.Properties.create(Material.WATER)).setRegistryName("water_grass"),
					new BlockLargeCollisionTest(Block.Properties.create(Material.CLOTH)).setRegistryName("large_collision_test"),
					new BlockRightClickTest(Block.Properties.create(Material.GLASS)).setRegistryName("right_click_test"),
					new BlockClientPlayerRightClick(Block.Properties.create(Material.ROCK)).setRegistryName("client_player_right_click"),
					new BlockRotatableLamp(Block.Properties.create(Material.REDSTONE_LIGHT)).setRegistryName("rotatable_lamp"),
					new BlockItemCollisionTest(Block.Properties.create(Material.CLOTH)).setRegistryName("item_collision_test"),
					new BlockFluidTank<>(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3f)).setRegistryName("fluid_tank"),
					new BlockItemDebugger(Block.Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000)).setRegistryName("item_debugger"),
					new Block(Block.Properties.create(Material.ROCK)).setRegistryName("end_portal_frame_full"),
					new BlockPotionEffect(Block.Properties.create(Material.ROCK)).setRegistryName("potion_effect"),
					new BlockClientPlayerRotation(Block.Properties.create(Material.ROCK)).setRegistryName("client_player_rotation"),
					new BlockPigSpawnerRefiller(Block.Properties.create(Material.IRON)).setRegistryName("pig_spawner_refiller"),
					new BlockPlane(Block.Properties.create(Material.IRON)).setRegistryName("mirror_plane"),
					new Block(Block.Properties.create(Material.IRON)).setRegistryName("vanilla_model_test"),
					new Block(Block.Properties.create(Material.ROCK).lightValue(15)).setRegistryName("fullbright"),
					new Block(Block.Properties.create(Material.ROCK)).setRegistryName("normal_brightness"),
					new BlockMaxHealthSetter(Block.Properties.create(Material.IRON)).setRegistryName("max_health_setter"),
					new BlockMaxHealthGetter(Block.Properties.create(Material.IRON)).setRegistryName("max_health_getter"),
					new BlockSmallCollisionTest(Block.Properties.create(Material.IRON)).setRegistryName("small_collision_test"),
					new BlockModChest(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5f)).setRegistryName("chest"),
					new BlockHidden(Block.Properties.create(Material.ROCK)).setRegistryName("hidden"),
					new BlockPipeBasic(Block.Properties.create(Material.IRON)).setRegistryName("basic_pipe"),
					new BlockPipeFluid(Block.Properties.create(Material.IRON)).setRegistryName("fluid_pipe"),
					new BlockSurvivalCommandBlock(Block.Properties.from(Blocks.COMMAND_BLOCK), TileEntityCommandBlock.Mode.REDSTONE).setRegistryName("survival_command_block"),
					new BlockSurvivalCommandBlock(Block.Properties.from(Blocks.REPEATING_COMMAND_BLOCK), TileEntityCommandBlock.Mode.AUTO).setRegistryName("repeating_survival_command_block"),
					new BlockSurvivalCommandBlock(Block.Properties.from(Blocks.CHAIN_COMMAND_BLOCK), TileEntityCommandBlock.Mode.SEQUENCE).setRegistryName("chain_survival_command_block"),
					new BlockSaplingTestMod3(new OakTree(), Block.Properties.from(Blocks.OAK_SAPLING)).setRegistryName("oak_sapling"),
					new BlockSaplingTestMod3(new SpruceTree(), Block.Properties.from(Blocks.SPRUCE_SAPLING)).setRegistryName("spruce_sapling"),
					new BlockSaplingTestMod3(new BirchTree(), Block.Properties.from(Blocks.BIRCH_SAPLING)).setRegistryName("birch_sapling"),
					new BlockSaplingTestMod3(new JungleTree(), Block.Properties.from(Blocks.JUNGLE_SAPLING)).setRegistryName("jungle_sapling"),
					new BlockSaplingTestMod3(new AcaciaTree(), Block.Properties.from(Blocks.ACACIA_SAPLING)).setRegistryName("acacia_sapling"),
					new BlockSaplingTestMod3(new DarkOakTree(), Block.Properties.from(Blocks.DARK_OAK_SAPLING)).setRegistryName("dark_oak_sapling"),
					new BlockInvisible(Block.Properties.create(Material.ROCK)).setRegistryName("invisible"),
					new BlockFluidTankRestricted(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3f)).setRegistryName("fluid_tank_restricted"),
					new Block(Block.Properties.create(Material.WOOD)).setRegistryName("planks"),
			};

			for (final Block block : blocks) {
				registry.register(block);
			}

			VariantGroups.COLORED_ROTATABLE_BLOCKS.registerBlocks(registry);
			VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS.registerBlocks(registry);
			VariantGroups.TERRACOTTA_SLABS.registerBlocks(registry);
			VariantGroups.VARIANTS_BLOCKS.registerBlocks(registry);

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
					new ItemBlock(WATER_GRASS, defaultItemProperties()),
					new ItemBlock(LARGE_COLLISION_TEST, defaultItemProperties()),
					new ItemBlock(RIGHT_CLICK_TEST, defaultItemProperties()),
					new ItemBlock(CLIENT_PLAYER_RIGHT_CLICK, defaultItemProperties()),
					new ItemBlock(ROTATABLE_LAMP, defaultItemProperties()),
					new ItemBlock(ITEM_COLLISION_TEST, defaultItemProperties()),
					new ItemFluidTank(FLUID_TANK, defaultItemProperties().maxStackSize(1)),
					new ItemBlock(ITEM_DEBUGGER, defaultItemProperties()),
					new ItemBlock(END_PORTAL_FRAME_FULL, defaultItemProperties()),
					new ItemBlock(POTION_EFFECT, defaultItemProperties()),
					new ItemBlock(CLIENT_PLAYER_ROTATION, defaultItemProperties()),
					new ItemBlock(PIG_SPAWNER_REFILLER, defaultItemProperties()),
					new ItemBlock(MIRROR_PLANE, defaultItemProperties()),
					new ItemBlock(VANILLA_MODEL_TEST, defaultItemProperties()),
					new ItemBlock(FULLBRIGHT, defaultItemProperties()),
					new ItemBlock(NORMAL_BRIGHTNESS, defaultItemProperties()),
					new ItemBlock(MAX_HEALTH_SETTER, defaultItemProperties()),
					new ItemBlock(MAX_HEALTH_GETTER, defaultItemProperties()),
					new ItemBlock(SMALL_COLLISION_TEST, defaultItemProperties()),
					new ItemBlock(CHEST, defaultItemProperties()),
					new ItemBlock(HIDDEN, defaultItemProperties()),
					new ItemBlock(BASIC_PIPE, defaultItemProperties()),
					new ItemBlock(FLUID_PIPE, defaultItemProperties()),
					new ItemBlock(SURVIVAL_COMMAND_BLOCK, defaultItemProperties()),
					new ItemBlock(REPEATING_SURVIVAL_COMMAND_BLOCK, defaultItemProperties()),
					new ItemBlock(CHAIN_SURVIVAL_COMMAND_BLOCK, defaultItemProperties()),
					new ItemBlock(OAK_SAPLING, defaultItemProperties()),
					new ItemBlock(SPRUCE_SAPLING, defaultItemProperties()),
					new ItemBlock(BIRCH_SAPLING, defaultItemProperties()),
					new ItemBlock(JUNGLE_SAPLING, defaultItemProperties()),
					new ItemBlock(ACACIA_SAPLING, defaultItemProperties()),
					new ItemBlock(DARK_OAK_SAPLING, defaultItemProperties()),
					new ItemBlock(INVISIBLE, defaultItemProperties()),
					new ItemFluidTank(FLUID_TANK_RESTRICTED, defaultItemProperties()),
					new ItemBlock(PLANKS, defaultItemProperties()),
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final ItemBlock item : items) {
				final Block block = item.getBlock();
				final ResourceLocation registryName = Preconditions.checkNotNull(block.getRegistryName(), "Block %s has null registry name", block);
				registry.register(item.setRegistryName(registryName));
				ITEM_BLOCKS.add(item);
			}

			registerVariantGroupItems(registry, VariantGroups.COLORED_ROTATABLE_BLOCKS);
			registerVariantGroupItems(registry, VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS);
			registerVariantGroupItems(registry, VariantGroups.TERRACOTTA_SLABS);
			registerVariantGroupItems(registry, VariantGroups.VARIANTS_BLOCKS);
		}

		/**
		 * Gets an {@link Item.Properties} instance with the {@link ItemGroup} set to {@link TestMod3#ITEM_GROUP}.
		 *
		 * @return The item properties
		 */
		private static Item.Properties defaultItemProperties() {
			return new Item.Properties().group(TestMod3.ITEM_GROUP);
		}

		/**
		 * Register an {@link IBlockVariantGroup}'s {@link Item}s and add them to {@link #ITEM_BLOCKS}.
		 *
		 * @param registry The Item registry
		 * @param group    The variant group
		 */
		private static void registerVariantGroupItems(final IForgeRegistry<Item> registry, final IBlockVariantGroup<?, ?> group) {
			final List<? extends ItemBlock> items = group.registerItems(registry);
			ITEM_BLOCKS.addAll(items);
		}
	}

	private static void registerTileEntities() {
		registerTileEntity(TileEntitySurvivalCommandBlock.class, "survival_command_block");
		registerTileEntity(TileEntityFluidTank.class, "fluid_tank");
		registerTileEntity(TileEntityColoredMultiRotatable.class, "colored_multi_rotatable");
		registerTileEntity(TileEntityPotionEffect.class, "potion_effect");
		registerTileEntity(TileEntityModChest.class, "mod_chest");
		registerTileEntity(TileEntityHidden.class, "hidden");
		registerTileEntity(TileEntityFluidTankRestricted.class, "fluid_tank_restricted");
	}

	private static void registerTileEntity(final Class<? extends TileEntity> tileEntityClass, final String name) {
		GameRegistry.registerTileEntity(tileEntityClass, new ResourceLocation(TestMod3.MODID, name));
	}
}
