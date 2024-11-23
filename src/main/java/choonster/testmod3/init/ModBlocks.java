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

public class ModBlocks {
	private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, TestMod3.MODID);
	private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TestMod3.MODID);
	private static final DeferredRegister<MapCodec<? extends Block>> BLOCK_TYPES = DeferredRegister.create(Registries.BLOCK_TYPE, TestMod3.MODID);

	private static final Method CODEC = ObfuscationReflectionHelper.findMethod(Block.class, /* codec */ "m_304657_");

	private static boolean isInitialised = false;

	public static final RegistryObject<WaterGrassBlock> WATER_GRASS = registerBlock("water_grass",
			WaterGrassBlock::new,
			Block.Properties.ofFullCopy(Blocks.SEAGRASS)
	);

	public static final RegistryObject<LargeCollisionTestBlock> LARGE_COLLISION_TEST = registerBlock("large_collision_test",
			LargeCollisionTestBlock::new,
			wool()
	);

	public static final RegistryObject<RightClickTestBlock> RIGHT_CLICK_TEST = registerBlock("right_click_test",
			RightClickTestBlock::new,
			Block.Properties.of().instrument(NoteBlockInstrument.HAT)
	);

	public static final RegistryObject<ClientPlayerRightClickBlock> CLIENT_PLAYER_RIGHT_CLICK = registerBlock("client_player_right_click",
			ClientPlayerRightClickBlock::new,
			stone().pushReaction(PushReaction.DESTROY)
	);

	public static final RegistryObject<RotatableLampBlock> ROTATABLE_LAMP = registerBlock("rotatable_lamp",
			RotatableLampBlock::new,
			Block.Properties.of()
	);

	public static final RegistryObject<ItemCollisionTestBlock> ITEM_COLLISION_TEST = registerBlock("item_collision_test",
			ItemCollisionTestBlock::new,
			wool()
	);

	public static final RegistryObject<FluidTankBlock<BaseFluidTankBlockEntity>> FLUID_TANK = registerBlock("fluid_tank",
			FluidTankBlock::new,
			Block.Properties.of().instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(0.3f).noOcclusion(),
			FluidTankItem::new,
			defaultItemProperties().stacksTo(1)
	);

	public static final RegistryObject<ItemDebuggerBlock> ITEM_DEBUGGER = registerBlock("item_debugger",
			ItemDebuggerBlock::new,
			Block.Properties.of().mapColor(MapColor.METAL).strength(-1, 3600000)
	);

	public static final RegistryObject<Block> END_PORTAL_FRAME_FULL = registerBlock("end_portal_frame_full",
			Block::new,
			stone()
	);

	public static final RegistryObject<PotionEffectBlock> POTION_EFFECT = registerBlock("potion_effect",
			PotionEffectBlock::new,
			stone()
	);

	public static final RegistryObject<ClientPlayerRotationBlock> CLIENT_PLAYER_ROTATION = registerBlock("client_player_rotation",
			ClientPlayerRotationBlock::new,
			stone().pushReaction(PushReaction.DESTROY)
	);

	public static final RegistryObject<PigSpawnerRefillerBlock> PIG_SPAWNER_REFILLER = registerBlock("pig_spawner_refiller",
			PigSpawnerRefillerBlock::new,
			metal()
	);

	public static final RegistryObject<PlaneBlock> MIRROR_PLANE = registerBlock("mirror_plane",
			PlaneBlock::new,
			Block.Properties.of().mapColor(MapColor.METAL).noOcclusion().isRedstoneConductor((state, world, pos) -> false)
	);

	public static final RegistryObject<Block> VANILLA_MODEL_TEST = registerBlock("vanilla_model_test",
			Block::new,
			metal()
	);

	public static final RegistryObject<Block> FULLBRIGHT = registerBlock("fullbright",
			Block::new,
			stone().lightLevel((state) -> 15)
	);

	public static final RegistryObject<Block> NORMAL_BRIGHTNESS = registerBlock("normal_brightness",
			Block::new,
			stone()
	);

	public static final RegistryObject<MaxHealthSetterBlock> MAX_HEALTH_SETTER = registerBlock("max_health_setter",
			MaxHealthSetterBlock::new,
			metal()
	);

	public static final RegistryObject<MaxHealthGetterBlock> MAX_HEALTH_GETTER = registerBlock("max_health_getter",
			MaxHealthGetterBlock::new,
			metal()
	);

	public static final RegistryObject<SmallCollisionTestBlock> SMALL_COLLISION_TEST = registerBlock("small_collision_test",
			SmallCollisionTestBlock::new,
			metal()
	);

	public static final RegistryObject<ModChestBlock> CHEST = registerBlock("chest",
			ModChestBlock::new,
			Block.Properties.ofFullCopy(Blocks.CHEST)
	);

	public static final RegistryObject<HiddenBlock> HIDDEN = registerBlock("hidden",
			HiddenBlock::new,
			stone()
	);

	public static final RegistryObject<BasicPipeBlock> BASIC_PIPE = registerBlock("basic_pipe",
			BasicPipeBlock::new,
			metal()
	);

	public static final RegistryObject<FluidPipeBlock> FLUID_PIPE = registerBlock("fluid_pipe",
			FluidPipeBlock::new,
			metal()
	);

	public static final RegistryObject<SurvivalCommandBlock> SURVIVAL_COMMAND_BLOCK = registerBlock("survival_command_block",
			(properties) -> new SurvivalCommandBlock(CommandBlockEntity.Mode.REDSTONE, false, properties),
			Block.Properties.ofFullCopy(Blocks.COMMAND_BLOCK),
			BlockItem::new,
			defaultItemProperties().rarity(Rarity.EPIC)
	);

	public static final RegistryObject<SurvivalCommandBlock> REPEATING_SURVIVAL_COMMAND_BLOCK = registerBlock("repeating_survival_command_block",
			(properties) -> new SurvivalCommandBlock(CommandBlockEntity.Mode.AUTO, false, properties),
			Block.Properties.ofFullCopy(Blocks.REPEATING_COMMAND_BLOCK),
			BlockItem::new,
			defaultItemProperties().rarity(Rarity.EPIC)
	);

	public static final RegistryObject<SurvivalCommandBlock> CHAIN_SURVIVAL_COMMAND_BLOCK = registerBlock("chain_survival_command_block",
			(properties) -> new SurvivalCommandBlock(CommandBlockEntity.Mode.SEQUENCE, true, properties),
			Block.Properties.ofFullCopy(Blocks.CHAIN_COMMAND_BLOCK),
			BlockItem::new,
			defaultItemProperties().rarity(Rarity.EPIC)
	);

	public static final RegistryObject<SaplingBlock> OAK_SAPLING = registerBlock("oak_sapling",
			(properties) -> new SaplingBlock(TreeGrower.OAK, properties),
			Block.Properties.ofFullCopy(Blocks.OAK_SAPLING)
	);

	public static final RegistryObject<SaplingBlock> SPRUCE_SAPLING = registerBlock("spruce_sapling",
			(properties) -> new SaplingBlock(TreeGrower.SPRUCE, properties),
			Block.Properties.ofFullCopy(Blocks.SPRUCE_SAPLING)
	);

	public static final RegistryObject<SaplingBlock> BIRCH_SAPLING = registerBlock("birch_sapling",
			(properties) -> new SaplingBlock(TreeGrower.BIRCH, properties),
			Block.Properties.ofFullCopy(Blocks.BIRCH_SAPLING)
	);

	public static final RegistryObject<SaplingBlock> JUNGLE_SAPLING = registerBlock("jungle_sapling",
			(properties) -> new SaplingBlock(TreeGrower.JUNGLE, properties),
			Block.Properties.ofFullCopy(Blocks.JUNGLE_SAPLING)
	);

	public static final RegistryObject<SaplingBlock> ACACIA_SAPLING = registerBlock("acacia_sapling",
			(properties) -> new SaplingBlock(TreeGrower.ACACIA, properties),
			Block.Properties.ofFullCopy(Blocks.ACACIA_SAPLING)
	);

	public static final RegistryObject<SaplingBlock> DARK_OAK_SAPLING = registerBlock("dark_oak_sapling",
			(properties) -> new SaplingBlock(TreeGrower.DARK_OAK, properties),
			Block.Properties.ofFullCopy(Blocks.DARK_OAK_SAPLING)
	);

	public static final RegistryObject<InvisibleBlock> INVISIBLE = registerBlock("invisible",
			InvisibleBlock::new,
			stone()
	);

	public static final RegistryObject<RestrictedFluidTankBlock> FLUID_TANK_RESTRICTED = registerBlock("fluid_tank_restricted",
			RestrictedFluidTankBlock::new,
			Block.Properties.of().instrument(NoteBlockInstrument.HAT).sound(SoundType.GLASS).strength(0.3f).noOcclusion(),
			FluidTankItem::new,
			defaultItemProperties().rarity(Rarity.EPIC)
	);

	public static final RegistryObject<Block> PLANKS = registerBlock("planks",
			Block::new,
			Block.Properties.ofFullCopy(Blocks.OAK_PLANKS)
	);


	public static final BlockVariantGroup<DyeColor, ColoredRotatableBlock> COLORED_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredRotatableBlock>create(BLOCKS, ITEMS)
			.groupName("rotatable_block")
			.variants(DyeColor.values())
			.variantCodec(DyeColor.CODEC)
			.blockPropertiesFactory(color -> wool().mapColor(color))
			.blockFactory(ColoredRotatableBlock::new)
			.blockCodecFactory(ColoredRotatableBlock::codec)
			.build();

	public static final BlockVariantGroup<DyeColor, ColoredMultiRotatableBlock> COLORED_MULTI_ROTATABLE_BLOCKS = BlockVariantGroup.Builder.<DyeColor, ColoredMultiRotatableBlock>create(BLOCKS, ITEMS)
			.groupName("multi_rotatable_block")
			.variants(DyeColor.values())
			.variantCodec(DyeColor.CODEC)
			.blockPropertiesFactory(color -> wool().mapColor(color))
			.blockFactory(ColoredMultiRotatableBlock::new)
			.blockCodecFactory(ColoredMultiRotatableBlock::codec)
			.build();

	public static final BlockVariantGroup<VariantsBlock.EnumType, VariantsBlock> VARIANTS_BLOCKS = BlockVariantGroup.Builder.<VariantsBlock.EnumType, VariantsBlock>create(BLOCKS, ITEMS)
			.groupName("variants_block")
			.suffix()
			.variants(VariantsBlock.EnumType.values())
			.variantCodec(VariantsBlock.EnumType.CODEC)
			.blockPropertiesFactory(type -> metal())
			.blockFactory(VariantsBlock::new)
			.blockCodecFactory(VariantsBlock::codec)
			.build();

	public static final BlockVariantGroup<DyeColor, ColouredSlabBlock> TERRACOTTA_SLABS = BlockVariantGroup.Builder.<DyeColor, ColouredSlabBlock>create(BLOCKS, ITEMS)
			.groupName("terracotta_slab")
			.variants(DyeColor.values())
			.variantCodec(DyeColor.CODEC)
			.blockPropertiesFactory(color -> stone().mapColor(color))
			.blockFactory(ColouredSlabBlock::new)
			.blockCodecFactory(ColouredSlabBlock::codec)
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
	 * @param name            The registry name of the block
	 * @param blockFactory    The factory used to create the block
	 * @param blockProperties The properties of the block
	 * @param <BLOCK>         The block type
	 * @return A RegistryObject reference to the block
	 */
	private static <BLOCK extends Block> RegistryObject<BLOCK> registerBlock(
			final String name,
			final IBlockFactory<BLOCK> blockFactory,
			final BlockBehaviour.Properties blockProperties
	) {
		return registerBlock(
				name,
				blockFactory,
				blockProperties,
				BlockItem::new,
				defaultItemProperties()
		);
	}

	/**
	 * Registers a block and its block item.
	 *
	 * @param name            The registry name of the block
	 * @param blockFactory    The factory used to create the block
	 * @param blockProperties The properties of the block
	 * @param itemFactory     The factory used to create the block item
	 * @param itemProperties  The properties of the item
	 * @param <BLOCK>         The block type
	 * @return A RegistryObject reference to the block
	 */
	private static <BLOCK extends Block> RegistryObject<BLOCK> registerBlock(
			final String name,
			final IBlockFactory<BLOCK> blockFactory,
			final BlockBehaviour.Properties blockProperties,
			final IBlockItemFactory<BLOCK> itemFactory,
			final Item.Properties itemProperties
	) {
		final var blockId = BLOCKS.key(name);
		final var itemId = ITEMS.key(name);

		final var block = BLOCKS.register(
				name,
				() -> blockFactory.create(
						blockProperties.setId(blockId)
				)
		);

		ITEMS.register(
				name,
				() -> itemFactory.create(
						block.get(),
						itemProperties.setId(itemId)
				)
		);

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
		return new Item.Properties().useBlockDescriptionPrefix();
	}

	/**
	 * A factory function used to create blocks.
	 *
	 * @param <BLOCK> The block type
	 */
	@FunctionalInterface
	private interface IBlockFactory<BLOCK extends Block> {
		BLOCK create(BlockBehaviour.Properties properties);
	}

	/**
	 * A factory function used to create block items.
	 *
	 * @param <BLOCK> The block type
	 */
	@FunctionalInterface
	private interface IBlockItemFactory<BLOCK extends Block> {
		Item create(BLOCK block, Item.Properties properties);
	}
}
