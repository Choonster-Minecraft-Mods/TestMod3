package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.world.item.block.FluidTankItem;
import choonster.testmod3.world.level.block.*;
import choonster.testmod3.world.level.block.entity.BaseFluidTankBlockEntity;
import choonster.testmod3.world.level.block.pipe.BasicPipeBlock;
import choonster.testmod3.world.level.block.pipe.FluidPipeBlock;
import choonster.testmod3.world.level.block.slab.ColouredSlabBlock;
import choonster.testmod3.world.level.block.variantgroup.BlockVariantGroup;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.CommandBlockEntity;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.function.Supplier;

public class ModBlocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod3.MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod3.MODID);
	private static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, TestMod3.MODID);

	private static final Method CODEC = ObfuscationReflectionHelper.findMethod(Block.class, /* codec */ "m_304657_");

	private static boolean isInitialised = false;

	public static final RegistryObject<WaterGrassBlock> WATER_GRASS = registerBlock("water_grass",
			() -> new WaterGrassBlock(Block.Properties.ofFullCopy(Blocks.SEAGRASS))
	);

	public static final RegistryObject<LargeCollisionTestBlock> LARGE_COLLISION_TEST = registerBlock("large_collision_test",
			() -> new LargeCollisionTestBlock(wool())
	);


	public static final RegistryObject<RightClickTestBlock> RIGHT_CLICK_TEST = registerBlock("right_click_test",
			() -> new RightClickTestBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT))
	);

	public static final RegistryObject<ClientPlayerRightClickBlock> CLIENT_PLAYER_RIGHT_CLICK = registerBlock("client_player_right_click",
			() -> new ClientPlayerRightClickBlock(stone().pushReaction(PushReaction.DESTROY))
	);

	public static final RegistryObject<RotatableLampBlock> ROTATABLE_LAMP = registerBlock("rotatable_lamp",
			() -> new RotatableLampBlock(Block.Properties.of())
	);

	public static final RegistryObject<ItemCollisionTestBlock> ITEM_COLLISION_TEST = registerBlock("item_collision_test",
			() -> new ItemCollisionTestBlock(wool())
	);

	public static final RegistryObject<FluidTankBlock<BaseFluidTankBlockEntity>> FLUID_TANK = registerBlock("fluid_tank",
			() -> new FluidTankBlock<>(Block.Properties.of().instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(0.3f).noOcclusion()),
			(block) -> new FluidTankItem(block, defaultItemProperties().stacksTo(1))
	);

	public static final RegistryObject<ItemDebuggerBlock> ITEM_DEBUGGER = registerBlock("item_debugger",
			() -> new ItemDebuggerBlock(Block.Properties.of().mapColor(MapColor.METAL).strength(-1, 3600000))
	);

	public static final RegistryObject<Block> END_PORTAL_FRAME_FULL = registerBlock("end_portal_frame_full",
			() -> new Block(stone())
	);

	public static final RegistryObject<PotionEffectBlock> POTION_EFFECT = registerBlock("potion_effect",
			() -> new PotionEffectBlock(stone())
	);

	public static final RegistryObject<ClientPlayerRotationBlock> CLIENT_PLAYER_ROTATION = registerBlock("client_player_rotation",
			() -> new ClientPlayerRotationBlock(stone().pushReaction(PushReaction.DESTROY))
	);

	public static final RegistryObject<PigSpawnerRefillerBlock> PIG_SPAWNER_REFILLER = registerBlock("pig_spawner_refiller",
			() -> new PigSpawnerRefillerBlock(metal())
	);

	public static final RegistryObject<PlaneBlock> MIRROR_PLANE = registerBlock("mirror_plane",
			() -> new PlaneBlock(Block.Properties.of().mapColor(MapColor.METAL).noOcclusion().isRedstoneConductor((state, world, pos) -> false))
	);

	public static final RegistryObject<Block> VANILLA_MODEL_TEST = registerBlock("vanilla_model_test",
			() -> new Block(metal())
	);

	public static final RegistryObject<Block> FULLBRIGHT = registerBlock("fullbright",
			() -> new Block(stone().lightLevel((state) -> 15))
	);

	public static final RegistryObject<Block> NORMAL_BRIGHTNESS = registerBlock("normal_brightness",
			() -> new Block(stone())
	);

	public static final RegistryObject<MaxHealthSetterBlock> MAX_HEALTH_SETTER = registerBlock("max_health_setter",
			() -> new MaxHealthSetterBlock(metal())
	);

	public static final RegistryObject<MaxHealthGetterBlock> MAX_HEALTH_GETTER = registerBlock("max_health_getter",
			() -> new MaxHealthGetterBlock(metal())
	);

	public static final RegistryObject<SmallCollisionTestBlock> SMALL_COLLISION_TEST = registerBlock("small_collision_test",
			() -> new SmallCollisionTestBlock(metal())
	);

	public static final RegistryObject<ModChestBlock> CHEST = registerBlock("chest",
			() -> new ModChestBlock(Block.Properties.ofFullCopy(Blocks.CHEST))
	);

	public static final RegistryObject<HiddenBlock> HIDDEN = registerBlock("hidden",
			() -> new HiddenBlock(stone())
	);

	public static final RegistryObject<BasicPipeBlock> BASIC_PIPE = registerBlock("basic_pipe",
			() -> new BasicPipeBlock(metal())
	);

	public static final RegistryObject<FluidPipeBlock> FLUID_PIPE = registerBlock("fluid_pipe",
			() -> new FluidPipeBlock(metal())
	);

	public static final RegistryObject<SurvivalCommandBlock> SURVIVAL_COMMAND_BLOCK = registerBlock("survival_command_block",
			() -> new SurvivalCommandBlock(CommandBlockEntity.Mode.REDSTONE, false, Block.Properties.ofFullCopy(Blocks.COMMAND_BLOCK)),
			(block) -> new BlockItem(block, defaultItemProperties().rarity(Rarity.EPIC))
	);

	public static final RegistryObject<SurvivalCommandBlock> REPEATING_SURVIVAL_COMMAND_BLOCK = registerBlock("repeating_survival_command_block",
			() -> new SurvivalCommandBlock(CommandBlockEntity.Mode.AUTO, false, Block.Properties.ofFullCopy(Blocks.REPEATING_COMMAND_BLOCK)),
			(block) -> new BlockItem(block, defaultItemProperties().rarity(Rarity.EPIC))
	);

	public static final RegistryObject<SurvivalCommandBlock> CHAIN_SURVIVAL_COMMAND_BLOCK = registerBlock("chain_survival_command_block",
			() -> new SurvivalCommandBlock(CommandBlockEntity.Mode.SEQUENCE, true, Block.Properties.ofFullCopy(Blocks.CHAIN_COMMAND_BLOCK)),
			(block) -> new BlockItem(block, defaultItemProperties().rarity(Rarity.EPIC))
	);

	public static final RegistryObject<SaplingBlock> OAK_SAPLING = registerBlock("oak_sapling",
			() -> new SaplingBlock(TreeGrower.OAK, Block.Properties.ofFullCopy(Blocks.OAK_SAPLING))
	);

	public static final RegistryObject<SaplingBlock> SPRUCE_SAPLING = registerBlock("spruce_sapling",
			() -> new SaplingBlock(TreeGrower.SPRUCE, Block.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING))
	);

	public static final RegistryObject<SaplingBlock> BIRCH_SAPLING = registerBlock("birch_sapling",
			() -> new SaplingBlock(TreeGrower.BIRCH, Block.Properties.ofFullCopy(Blocks.BIRCH_SAPLING))
	);

	public static final RegistryObject<SaplingBlock> JUNGLE_SAPLING = registerBlock("jungle_sapling",
			() -> new SaplingBlock(TreeGrower.JUNGLE, Block.Properties.ofFullCopy(Blocks.JUNGLE_SAPLING))
	);

	public static final RegistryObject<SaplingBlock> ACACIA_SAPLING = registerBlock("acacia_sapling",
			() -> new SaplingBlock(TreeGrower.ACACIA, Block.Properties.ofFullCopy(Blocks.ACACIA_SAPLING))
	);

	public static final RegistryObject<SaplingBlock> DARK_OAK_SAPLING = registerBlock("dark_oak_sapling",
			() -> new SaplingBlock(TreeGrower.DARK_OAK, Block.Properties.ofFullCopy(Blocks.DARK_OAK_SAPLING))
	);

	public static final RegistryObject<InvisibleBlock> INVISIBLE = registerBlock("invisible",
			() -> new InvisibleBlock(stone())
	);

	public static final RegistryObject<RestrictedFluidTankBlock> FLUID_TANK_RESTRICTED = registerBlock("fluid_tank_restricted",
			() -> new RestrictedFluidTankBlock(Block.Properties.of().instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(0.3f).noOcclusion()),
			(block) -> new FluidTankItem(block, defaultItemProperties())
	);

	public static final RegistryObject<Block> PLANKS = registerBlock("planks",
			() -> new Block(Block.Properties.ofFullCopy(Blocks.OAK_PLANKS))
	);


	public static final BlockVariantGroup<DyeColor, ColoredRotatableBlock> COLORED_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredRotatableBlock>create(BLOCKS, ITEMS)
			.groupName("rotatable_block")
			.variants(DyeColor.values())
			.blockPropertiesFactory(color -> wool().mapColor(color))
			.blockFactory(ColoredRotatableBlock::new)
			.build();

	public static final BlockVariantGroup<DyeColor, ColoredMultiRotatableBlock> COLORED_MULTI_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredMultiRotatableBlock>create(BLOCKS, ITEMS)
			.groupName("multi_rotatable_block")
			.variants(DyeColor.values())
			.blockPropertiesFactory(color -> wool().mapColor(color))
			.blockFactory(ColoredMultiRotatableBlock::new)
			.build();

	public static final BlockVariantGroup<VariantsBlock.EnumType, VariantsBlock> VARIANTS_BLOCKS = BlockVariantGroup.Builder.<VariantsBlock.EnumType, VariantsBlock>create(BLOCKS, ITEMS)
			.groupName("variants_block")
			.suffix()
			.variants(VariantsBlock.EnumType.values())
			.blockPropertiesFactory(type -> metal())
			.blockFactory(VariantsBlock::new)
			.build();

	public static final BlockVariantGroup<DyeColor, ColouredSlabBlock> TERRACOTTA_SLABS = BlockVariantGroup.Builder.<DyeColor, ColouredSlabBlock>create(BLOCKS, ITEMS)
			.groupName("terracotta_slab")
			.variants(DyeColor.values())
			.blockPropertiesFactory(color -> stone().mapColor(color))
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
	 * @return A collection of this mod's block items in the order of their registration.
	 */
	static Collection<RegistryObject<Item>> orderedItems() {
		return ITEMS.getEntries();
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

		BLOCK_TYPES.register(name, () -> {
			try {
				@SuppressWarnings("unchecked") final var codec = (MapCodec<? extends Block>) CODEC.invoke(block.get());

				if (codec == Block.CODEC) {
					throw new IllegalStateException("Block " + name + " must override Block.codec()");
				}

				return codec;
			} catch (final IllegalAccessException | InvocationTargetException e) {
				throw new RuntimeException("Failed to get get codec for block " + name, e);
			}
		});

		return block;
	}

	/**
	 * Gets a {@link BlockBehaviour.Properties} instance with the default wool block properties.
	 *
	 * @return The block properties
	 */
	private static BlockBehaviour.Properties wool() {
		return Block.Properties.of().mapColor(MapColor.WOOL).ignitedByLava();
	}

	/**
	 * Gets a {@link BlockBehaviour.Properties} instance with the default stone block properties.
	 *
	 * @return The block properties
	 */
	private static BlockBehaviour.Properties stone() {
		return Block.Properties.of().mapColor(MapColor.STONE).instrument(NoteBlockInstrument.BASEDRUM);
	}

	/**
	 * Gets a {@link BlockBehaviour.Properties} instance with the default metal block properties.
	 *
	 * @return The block properties
	 */
	private static BlockBehaviour.Properties metal() {
		return Block.Properties.of().mapColor(MapColor.METAL);
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
