package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.item.block.FluidTankItem;
import choonster.testmod3.world.level.block.*;
import choonster.testmod3.world.level.block.entity.BaseFluidTankBlockEntity;
import choonster.testmod3.world.level.block.pipe.BasicPipeBlock;
import choonster.testmod3.world.level.block.pipe.FluidPipeBlock;
import choonster.testmod3.world.level.block.slab.ColouredSlabBlock;
import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.grower.*;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod3.MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod3.MODID);

	private static boolean isInitialised = false;

	public static final RegistryObject<WaterGrassBlock> WATER_GRASS = registerBlock("water_grass",
			() -> new WaterGrassBlock(Block.Properties.of(Material.REPLACEABLE_WATER_PLANT).noCollission().instabreak().sound(SoundType.WET_GRASS))
	);

	public static final RegistryObject<LargeCollisionTestBlock> LARGE_COLLISION_TEST = registerBlock("large_collision_test",
			() -> new LargeCollisionTestBlock(Block.Properties.of(Material.WOOL))
	);

	public static final RegistryObject<RightClickTestBlock> RIGHT_CLICK_TEST = registerBlock("right_click_test",
			() -> new RightClickTestBlock(Block.Properties.of(Material.GLASS))
	);

	public static final RegistryObject<ClientPlayerRightClickBlock> CLIENT_PLAYER_RIGHT_CLICK = registerBlock("client_player_right_click",
			() -> new ClientPlayerRightClickBlock(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<RotatableLampBlock> ROTATABLE_LAMP = registerBlock("rotatable_lamp",
			() -> new RotatableLampBlock(Block.Properties.of(Material.BUILDABLE_GLASS))
	);

	public static final RegistryObject<ItemCollisionTestBlock> ITEM_COLLISION_TEST = registerBlock("item_collision_test",
			() -> new ItemCollisionTestBlock(Block.Properties.of(Material.WOOL))
	);

	public static final RegistryObject<FluidTankBlock<BaseFluidTankBlockEntity>> FLUID_TANK = registerBlock("fluid_tank",
			() -> new FluidTankBlock<>(Block.Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f).noOcclusion()),
			(block) -> new FluidTankItem(block, defaultItemProperties().stacksTo(1))
	);

	public static final RegistryObject<ItemDebuggerBlock> ITEM_DEBUGGER = registerBlock("item_debugger",
			() -> new ItemDebuggerBlock(Block.Properties.of(Material.METAL).strength(-1, 3600000))
	);

	public static final RegistryObject<Block> END_PORTAL_FRAME_FULL = registerBlock("end_portal_frame_full",
			() -> new Block(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<PotionEffectBlock> POTION_EFFECT = registerBlock("potion_effect",
			() -> new PotionEffectBlock(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<ClientPlayerRotationBlock> CLIENT_PLAYER_ROTATION = registerBlock("client_player_rotation",
			() -> new ClientPlayerRotationBlock(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<PigSpawnerRefillerBlock> PIG_SPAWNER_REFILLER = registerBlock("pig_spawner_refiller",
			() -> new PigSpawnerRefillerBlock(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<PlaneBlock> MIRROR_PLANE = registerBlock("mirror_plane",
			() -> new PlaneBlock(Block.Properties.of(Material.METAL).noOcclusion().isRedstoneConductor((state, world, pos) -> false))
	);

	public static final RegistryObject<Block> VANILLA_MODEL_TEST = registerBlock("vanilla_model_test",
			() -> new Block(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<Block> FULLBRIGHT = registerBlock("fullbright",
			() -> new Block(Block.Properties.of(Material.STONE).lightLevel((state) -> 15))
	);

	public static final RegistryObject<Block> NORMAL_BRIGHTNESS = registerBlock("normal_brightness",
			() -> new Block(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<MaxHealthSetterBlock> MAX_HEALTH_SETTER = registerBlock("max_health_setter",
			() -> new MaxHealthSetterBlock(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<MaxHealthGetterBlock> MAX_HEALTH_GETTER = registerBlock("max_health_getter",
			() -> new MaxHealthGetterBlock(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<SmallCollisionTestBlock> SMALL_COLLISION_TEST = registerBlock("small_collision_test",
			() -> new SmallCollisionTestBlock(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<ModChestBlock> CHEST = registerBlock("chest",
			() -> new ModChestBlock(Block.Properties.of(Material.WOOD).sound(SoundType.WOOD).strength(2.5f))
	);

	public static final RegistryObject<HiddenBlock> HIDDEN = registerBlock("hidden",
			() -> new HiddenBlock(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<BasicPipeBlock> BASIC_PIPE = registerBlock("basic_pipe",
			() -> new BasicPipeBlock(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<FluidPipeBlock> FLUID_PIPE = registerBlock("fluid_pipe",
			() -> new FluidPipeBlock(Block.Properties.of(Material.METAL))
	);

	public static final RegistryObject<SurvivalCommandBlock> SURVIVAL_COMMAND_BLOCK = registerBlock("survival_command_block",
			() -> new SurvivalCommandBlock(CommandBlockEntity.Mode.REDSTONE, Block.Properties.copy(Blocks.COMMAND_BLOCK), false),
			(block) -> new BlockItem(block, defaultItemProperties().rarity(Rarity.EPIC))
	);

	public static final RegistryObject<SurvivalCommandBlock> REPEATING_SURVIVAL_COMMAND_BLOCK = registerBlock("repeating_survival_command_block",
			() -> new SurvivalCommandBlock(CommandBlockEntity.Mode.AUTO, Block.Properties.copy(Blocks.REPEATING_COMMAND_BLOCK), false),
			(block) -> new BlockItem(block, defaultItemProperties().rarity(Rarity.EPIC))
	);

	public static final RegistryObject<SurvivalCommandBlock> CHAIN_SURVIVAL_COMMAND_BLOCK = registerBlock("chain_survival_command_block",
			() -> new SurvivalCommandBlock(CommandBlockEntity.Mode.SEQUENCE, Block.Properties.copy(Blocks.CHAIN_COMMAND_BLOCK), true),
			(block) -> new BlockItem(block, defaultItemProperties().rarity(Rarity.EPIC))
	);

	public static final RegistryObject<TestMod3SaplingBlock> OAK_SAPLING = registerBlock("oak_sapling",
			() -> new TestMod3SaplingBlock(new OakTreeGrower(), Block.Properties.copy(Blocks.OAK_SAPLING))
	);

	public static final RegistryObject<TestMod3SaplingBlock> SPRUCE_SAPLING = registerBlock("spruce_sapling",
			() -> new TestMod3SaplingBlock(new SpruceTreeGrower(), Block.Properties.copy(Blocks.SPRUCE_SAPLING))
	);

	public static final RegistryObject<TestMod3SaplingBlock> BIRCH_SAPLING = registerBlock("birch_sapling",
			() -> new TestMod3SaplingBlock(new BirchTreeGrower(), Block.Properties.copy(Blocks.BIRCH_SAPLING))
	);

	public static final RegistryObject<TestMod3SaplingBlock> JUNGLE_SAPLING = registerBlock("jungle_sapling",
			() -> new TestMod3SaplingBlock(new JungleTreeGrower(), Block.Properties.copy(Blocks.JUNGLE_SAPLING))
	);

	public static final RegistryObject<TestMod3SaplingBlock> ACACIA_SAPLING = registerBlock("acacia_sapling",
			() -> new TestMod3SaplingBlock(new AcaciaTreeGrower(), Block.Properties.copy(Blocks.ACACIA_SAPLING))
	);

	public static final RegistryObject<TestMod3SaplingBlock> DARK_OAK_SAPLING = registerBlock("dark_oak_sapling",
			() -> new TestMod3SaplingBlock(new DarkOakTreeGrower(), Block.Properties.copy(Blocks.DARK_OAK_SAPLING))
	);

	public static final RegistryObject<InvisibleBlock> INVISIBLE = registerBlock("invisible",
			() -> new InvisibleBlock(Block.Properties.of(Material.STONE))
	);

	public static final RegistryObject<RestrictedFluidTankBlock> FLUID_TANK_RESTRICTED = registerBlock("fluid_tank_restricted",
			() -> new RestrictedFluidTankBlock(Block.Properties.of(Material.GLASS).sound(SoundType.GLASS).strength(0.3f).noOcclusion()),
			(block) -> new FluidTankItem(block, defaultItemProperties())
	);

	public static final RegistryObject<Block> PLANKS = registerBlock("planks",
			() -> new Block(Block.Properties.of(Material.WOOD))
	);


	public static final BlockVariantGroup<DyeColor, ColoredRotatableBlock> COLORED_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredRotatableBlock>create(BLOCKS, ITEMS)
			.groupName("rotatable_block")
			.variants(DyeColor.values())
			.blockPropertiesFactory(color -> Block.Properties.of(Material.WOOL, color))
			.blockFactory(ColoredRotatableBlock::new)
			.build();

	public static final BlockVariantGroup<DyeColor, ColoredMultiRotatableBlock> COLORED_MULTI_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredMultiRotatableBlock>create(BLOCKS, ITEMS)
			.groupName("multi_rotatable_block")
			.variants(DyeColor.values())
			.blockPropertiesFactory(color -> Block.Properties.of(Material.WOOL, color))
			.blockFactory(ColoredMultiRotatableBlock::new)
			.build();

	public static final BlockVariantGroup<VariantsBlock.EnumType, VariantsBlock> VARIANTS_BLOCKS = BlockVariantGroup.Builder.<VariantsBlock.EnumType, VariantsBlock>create(BLOCKS, ITEMS)
			.groupName("variants_block")
			.suffix()
			.variants(VariantsBlock.EnumType.values())
			.blockPropertiesFactory(type -> Block.Properties.of(Material.METAL))
			.blockFactory(VariantsBlock::new)
			.build();

	public static final BlockVariantGroup<DyeColor, ColouredSlabBlock> TERRACOTTA_SLABS = BlockVariantGroup.Builder.<DyeColor, ColouredSlabBlock>create(BLOCKS, ITEMS)
			.groupName("terracotta_slab")
			.variants(DyeColor.values())
			.blockPropertiesFactory(color -> Block.Properties.of(Material.STONE, color))
			.blockFactory(ColouredSlabBlock::new)
			.build();

	/**
	 * Registers the {@link DeferredRegister} instances with the mod event bus.
	 * <p>
	 * This should be called during mod construction.
	 *
	 * @param modEventBus The mod event bus
	 */
	public static void initialise(final IEventBus modEventBus) {
		if (isInitialised) {
			throw new IllegalStateException("Already initialised");
		}

		BLOCKS.register(modEventBus);
		ITEMS.register(modEventBus);

		isInitialised = true;
	}

	/**
	 * Registers a block with a standard {@link BlockItem} as its block item.
	 *
	 * @param name         The registry name of the block
	 * @param blockFactory The factory used to create the block
	 * @param <BLOCK>      The block type
	 * @return A RegistryObject reference to the block
	 */
	private static <BLOCK extends Block> RegistryObject<BLOCK> registerBlock(final String name, final Supplier<BLOCK> blockFactory) {
		return registerBlock(name, blockFactory, block -> new BlockItem(block, defaultItemProperties()));
	}

	/**
	 * Registers a block and its block item.
	 *
	 * @param name         The registry name of the block
	 * @param blockFactory The factory used to create the block
	 * @param itemFactory  The factory used to create the block item
	 * @param <BLOCK>      The block type
	 * @return A RegistryObject reference to the block
	 */
	private static <BLOCK extends Block> RegistryObject<BLOCK> registerBlock(final String name, final Supplier<BLOCK> blockFactory, final IBlockItemFactory<BLOCK> itemFactory) {
		final RegistryObject<BLOCK> block = BLOCKS.register(name, blockFactory);

		ITEMS.register(name, () -> itemFactory.create(block.get()));

		return block;
	}

	/**
	 * Gets an {@link Item.Properties} instance with the default item properties.
	 *
	 * @return The item properties
	 */
	private static Item.Properties defaultItemProperties() {
		return new Item.Properties();
	}

	/**
	 * A factory function used to create block items.
	 *
	 * @param <BLOCK> The block type
	 */
	@FunctionalInterface
	private interface IBlockItemFactory<BLOCK extends Block> {
		Item create(BLOCK block);
	}
}
