package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BasicPipeBlock;
import choonster.testmod3.block.pipe.FluidPipeBlock;
import choonster.testmod3.block.slab.ColouredSlabBlock;
import choonster.testmod3.block.variantgroup.BlockVariantGroup;
import choonster.testmod3.block.variantgroup.IBlockVariantGroup;
import choonster.testmod3.item.block.FluidTankItem;
import choonster.testmod3.tileentity.FluidTankTileEntity;
import com.google.common.base.Preconditions;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.trees.*;
import net.minecraft.item.BlockItem;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.CommandBlockTileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.ObjectHolder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static choonster.testmod3.util.InjectionUtil.Null;

@SuppressWarnings("WeakerAccess")
@ObjectHolder(TestMod3.MODID)
public class ModBlocks {

	public static final WaterGrassBlock WATER_GRASS = Null();

	public static final LargeCollisionTestBlock LARGE_COLLISION_TEST = Null();

	public static final RightClickTestBlock RIGHT_CLICK_TEST = Null();

	public static final ClientPlayerRightClickBlock CLIENT_PLAYER_RIGHT_CLICK = Null();

	public static final RotatableLampBlock ROTATABLE_LAMP = Null();

	public static final ItemCollisionTestBlock ITEM_COLLISION_TEST = Null();

	public static final FluidTankBlock<FluidTankTileEntity> FLUID_TANK = Null();

	public static final ItemDebuggerBlock ITEM_DEBUGGER = Null();

	public static final Block END_PORTAL_FRAME_FULL = Null();

	public static final PotionEffectBlock POTION_EFFECT = Null();

	public static final ClientPlayerRotationBlock CLIENT_PLAYER_ROTATION = Null();

	public static final PigSpawnerRefillerBlock PIG_SPAWNER_REFILLER = Null();

	public static final PlaneBlock MIRROR_PLANE = Null();

	public static final Block VANILLA_MODEL_TEST = Null();

	public static final Block FULLBRIGHT = Null();

	public static final Block NORMAL_BRIGHTNESS = Null();

	public static final MaxHealthSetterBlock MAX_HEALTH_SETTER = Null();

	public static final MaxHealthGetterBlock MAX_HEALTH_GETTER = Null();

	public static final SmallCollisionTestBlock SMALL_COLLISION_TEST = Null();

	public static final ModChestBlock CHEST = Null();

	public static final HiddenBlock HIDDEN = Null();

	public static final BasicPipeBlock BASIC_PIPE = Null();

	public static final FluidPipeBlock FLUID_PIPE = Null();

	public static final SurvivalCommandBlockBlock SURVIVAL_COMMAND_BLOCK = Null();

	public static final SurvivalCommandBlockBlock REPEATING_SURVIVAL_COMMAND_BLOCK = Null();

	public static final SurvivalCommandBlockBlock CHAIN_SURVIVAL_COMMAND_BLOCK = Null();

	public static final TestMod3SaplingBlock OAK_SAPLING = Null();

	public static final TestMod3SaplingBlock SPRUCE_SAPLING = Null();

	public static final TestMod3SaplingBlock BIRCH_SAPLING = Null();

	public static final TestMod3SaplingBlock JUNGLE_SAPLING = Null();

	public static final TestMod3SaplingBlock ACACIA_SAPLING = Null();

	public static final TestMod3SaplingBlock DARK_OAK_SAPLING = Null();

	public static final InvisibleBlock INVISIBLE = Null();

	public static final RestrictedFluidTankBlock FLUID_TANK_RESTRICTED = Null();

	public static final Block PLANKS = Null();

	public static class VariantGroups {
		public static final BlockVariantGroup<DyeColor, ColoredRotatableBlock> COLORED_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredRotatableBlock>create()
				.groupName("rotatable_block")
				.variants(DyeColor.values())
				.blockPropertiesFactory(color -> Block.Properties.create(Material.WOOL, color))
				.blockFactory(ColoredRotatableBlock::new)
				.build();

		public static final BlockVariantGroup<DyeColor, ColoredMultiRotatableBlock> COLORED_MULTI_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredMultiRotatableBlock>create()
				.groupName("multi_rotatable_block")
				.variants(DyeColor.values())
				.blockPropertiesFactory(color -> Block.Properties.create(Material.WOOL, color))
				.blockFactory(ColoredMultiRotatableBlock::new)
				.build();

		public static final BlockVariantGroup<VariantsBlock.EnumType, VariantsBlock> VARIANTS_BLOCKS = BlockVariantGroup.Builder.<VariantsBlock.EnumType, VariantsBlock>create()
				.groupName("variants_block")
				.suffix()
				.variants(VariantsBlock.EnumType.values())
				.blockPropertiesFactory(type -> Block.Properties.create(Material.IRON))
				.blockFactory(VariantsBlock::new)
				.build();

		public static final BlockVariantGroup<DyeColor, ColouredSlabBlock> TERRACOTTA_SLABS = BlockVariantGroup.Builder.<DyeColor, ColouredSlabBlock>create()
				.groupName("terracotta_slab")
				.variants(DyeColor.values())
				.blockPropertiesFactory(color -> Block.Properties.create(Material.ROCK, color))
				.blockFactory(ColouredSlabBlock::new)
				.build();
	}

	@Mod.EventBusSubscriber(modid = TestMod3.MODID, bus = Bus.MOD)
	public static class RegistrationHandler {
		public static final Set<BlockItem> ITEM_BLOCKS = new HashSet<>();

		/**
		 * Register this mod's {@link Block}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlocks(final RegistryEvent.Register<Block> event) {
			final IForgeRegistry<Block> registry = event.getRegistry();

			final Block[] blocks = {
					new WaterGrassBlock(Block.Properties.create(Material.WATER)).setRegistryName("water_grass"),
					new LargeCollisionTestBlock(Block.Properties.create(Material.WOOL)).setRegistryName("large_collision_test"),
					new RightClickTestBlock(Block.Properties.create(Material.GLASS)).setRegistryName("right_click_test"),
					new ClientPlayerRightClickBlock(Block.Properties.create(Material.ROCK)).setRegistryName("client_player_right_click"),
					new RotatableLampBlock(Block.Properties.create(Material.REDSTONE_LIGHT)).setRegistryName("rotatable_lamp"),
					new ItemCollisionTestBlock(Block.Properties.create(Material.WOOL)).setRegistryName("item_collision_test"),
					new FluidTankBlock<>(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3f)).setRegistryName("fluid_tank"),
					new ItemDebuggerBlock(Block.Properties.create(Material.IRON).hardnessAndResistance(-1, 3600000)).setRegistryName("item_debugger"),
					new Block(Block.Properties.create(Material.ROCK)).setRegistryName("end_portal_frame_full"),
					new PotionEffectBlock(Block.Properties.create(Material.ROCK)).setRegistryName("potion_effect"),
					new ClientPlayerRotationBlock(Block.Properties.create(Material.ROCK)).setRegistryName("client_player_rotation"),
					new PigSpawnerRefillerBlock(Block.Properties.create(Material.IRON)).setRegistryName("pig_spawner_refiller"),
					new PlaneBlock(Block.Properties.create(Material.IRON)).setRegistryName("mirror_plane"),
					new Block(Block.Properties.create(Material.IRON)).setRegistryName("vanilla_model_test"),
					new Block(Block.Properties.create(Material.ROCK).lightValue(15)).setRegistryName("fullbright"),
					new Block(Block.Properties.create(Material.ROCK)).setRegistryName("normal_brightness"),
					new MaxHealthSetterBlock(Block.Properties.create(Material.IRON)).setRegistryName("max_health_setter"),
					new MaxHealthGetterBlock(Block.Properties.create(Material.IRON)).setRegistryName("max_health_getter"),
					new SmallCollisionTestBlock(Block.Properties.create(Material.IRON)).setRegistryName("small_collision_test"),
					new ModChestBlock(Block.Properties.create(Material.WOOD).sound(SoundType.WOOD).hardnessAndResistance(2.5f)).setRegistryName("chest"),
					new HiddenBlock(Block.Properties.create(Material.ROCK)).setRegistryName("hidden"),
					new BasicPipeBlock(Block.Properties.create(Material.IRON)).setRegistryName("basic_pipe"),
					new FluidPipeBlock(Block.Properties.create(Material.IRON)).setRegistryName("fluid_pipe"),
					new SurvivalCommandBlockBlock(CommandBlockTileEntity.Mode.REDSTONE, Block.Properties.from(Blocks.COMMAND_BLOCK)).setRegistryName("survival_command_block"),
					new SurvivalCommandBlockBlock(CommandBlockTileEntity.Mode.AUTO, Block.Properties.from(Blocks.REPEATING_COMMAND_BLOCK)).setRegistryName("repeating_survival_command_block"),
					new SurvivalCommandBlockBlock(CommandBlockTileEntity.Mode.SEQUENCE, Block.Properties.from(Blocks.CHAIN_COMMAND_BLOCK)).setRegistryName("chain_survival_command_block"),
					new TestMod3SaplingBlock(new OakTree(), Block.Properties.from(Blocks.OAK_SAPLING)).setRegistryName("oak_sapling"),
					new TestMod3SaplingBlock(new SpruceTree(), Block.Properties.from(Blocks.SPRUCE_SAPLING)).setRegistryName("spruce_sapling"),
					new TestMod3SaplingBlock(new BirchTree(), Block.Properties.from(Blocks.BIRCH_SAPLING)).setRegistryName("birch_sapling"),
					new TestMod3SaplingBlock(new JungleTree(), Block.Properties.from(Blocks.JUNGLE_SAPLING)).setRegistryName("jungle_sapling"),
					new TestMod3SaplingBlock(new AcaciaTree(), Block.Properties.from(Blocks.ACACIA_SAPLING)).setRegistryName("acacia_sapling"),
					new TestMod3SaplingBlock(new DarkOakTree(), Block.Properties.from(Blocks.DARK_OAK_SAPLING)).setRegistryName("dark_oak_sapling"),
					new InvisibleBlock(Block.Properties.create(Material.ROCK)).setRegistryName("invisible"),
					new RestrictedFluidTankBlock(Block.Properties.create(Material.GLASS).sound(SoundType.GLASS).hardnessAndResistance(0.3f)).setRegistryName("fluid_tank_restricted"),
					new Block(Block.Properties.create(Material.WOOD)).setRegistryName("planks"),
			};

			for (final Block block : blocks) {
				registry.register(block);
			}

			VariantGroups.COLORED_ROTATABLE_BLOCKS.registerBlocks(registry);
			VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS.registerBlocks(registry);
			VariantGroups.TERRACOTTA_SLABS.registerBlocks(registry);
			VariantGroups.VARIANTS_BLOCKS.registerBlocks(registry);
		}

		/**
		 * Register this mod's {@link BlockItem}s.
		 *
		 * @param event The event
		 */
		@SubscribeEvent
		public static void registerBlockItems(final RegistryEvent.Register<Item> event) {
			final BlockItem[] items = {
					new BlockItem(WATER_GRASS, defaultItemProperties()),
					new BlockItem(LARGE_COLLISION_TEST, defaultItemProperties()),
					new BlockItem(RIGHT_CLICK_TEST, defaultItemProperties()),
					new BlockItem(CLIENT_PLAYER_RIGHT_CLICK, defaultItemProperties()),
					new BlockItem(ROTATABLE_LAMP, defaultItemProperties()),
					new BlockItem(ITEM_COLLISION_TEST, defaultItemProperties()),
					new FluidTankItem(FLUID_TANK, defaultItemProperties().maxStackSize(1)),
					new BlockItem(ITEM_DEBUGGER, defaultItemProperties()),
					new BlockItem(END_PORTAL_FRAME_FULL, defaultItemProperties()),
					new BlockItem(POTION_EFFECT, defaultItemProperties()),
					new BlockItem(CLIENT_PLAYER_ROTATION, defaultItemProperties()),
					new BlockItem(PIG_SPAWNER_REFILLER, defaultItemProperties()),
					new BlockItem(MIRROR_PLANE, defaultItemProperties()),
					new BlockItem(VANILLA_MODEL_TEST, defaultItemProperties()),
					new BlockItem(FULLBRIGHT, defaultItemProperties()),
					new BlockItem(NORMAL_BRIGHTNESS, defaultItemProperties()),
					new BlockItem(MAX_HEALTH_SETTER, defaultItemProperties()),
					new BlockItem(MAX_HEALTH_GETTER, defaultItemProperties()),
					new BlockItem(SMALL_COLLISION_TEST, defaultItemProperties()),
					new BlockItem(CHEST, defaultItemProperties()),
					new BlockItem(HIDDEN, defaultItemProperties()),
					new BlockItem(BASIC_PIPE, defaultItemProperties()),
					new BlockItem(FLUID_PIPE, defaultItemProperties()),
					new BlockItem(SURVIVAL_COMMAND_BLOCK, defaultItemProperties()),
					new BlockItem(REPEATING_SURVIVAL_COMMAND_BLOCK, defaultItemProperties()),
					new BlockItem(CHAIN_SURVIVAL_COMMAND_BLOCK, defaultItemProperties()),
					new BlockItem(OAK_SAPLING, defaultItemProperties()),
					new BlockItem(SPRUCE_SAPLING, defaultItemProperties()),
					new BlockItem(BIRCH_SAPLING, defaultItemProperties()),
					new BlockItem(JUNGLE_SAPLING, defaultItemProperties()),
					new BlockItem(ACACIA_SAPLING, defaultItemProperties()),
					new BlockItem(DARK_OAK_SAPLING, defaultItemProperties()),
					new BlockItem(INVISIBLE, defaultItemProperties()),
					new FluidTankItem(FLUID_TANK_RESTRICTED, defaultItemProperties()),
					new BlockItem(PLANKS, defaultItemProperties()),
			};

			final IForgeRegistry<Item> registry = event.getRegistry();

			for (final BlockItem item : items) {
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
			final List<? extends BlockItem> items = group.registerItems(registry);
			ITEM_BLOCKS.addAll(items);
		}
	}
}
