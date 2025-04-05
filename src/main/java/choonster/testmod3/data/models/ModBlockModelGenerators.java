package choonster.testmod3.data.models;

import choonster.testmod3.TestMod3;
import choonster.testmod3.data.models.model.ModModelTemplates;
import choonster.testmod3.data.models.model.ModTextureMappings;
import choonster.testmod3.data.models.model.ModTextureSlots;
import choonster.testmod3.fluid.BasicFluidType;
import choonster.testmod3.fluid.group.FluidGroup;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.init.ModFluids;
import choonster.testmod3.util.EnumFaceRotation;
import choonster.testmod3.util.RegistryUtil;
import choonster.testmod3.world.level.block.*;
import choonster.testmod3.world.level.block.pipe.BasePipeBlock;
import choonster.testmod3.world.level.block.slab.ColouredSlabBlock;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.client.color.item.GrassColorSource;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiPartGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.*;
import net.minecraft.client.renderer.block.model.VariantMutator;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Generates this mod's block models.
 *
 * @author Choonster
 */
public class ModBlockModelGenerators extends BlockModelGenerators {
	private static final Field SUFFIX = ObfuscationReflectionHelper.findField(ModelTemplate.class, "suffix");

	private static final String COLORED_ROTATABLE_PREFIX = "block/colored_rotatable/";

	private static final Map<DyeColor, Block> TERRACOTTA_BLOCKS = Util.make(() -> {
		var map = new EnumMap<DyeColor, Block>(DyeColor.class);
		map.put(DyeColor.WHITE, Blocks.WHITE_TERRACOTTA);
		map.put(DyeColor.ORANGE, Blocks.ORANGE_TERRACOTTA);
		map.put(DyeColor.MAGENTA, Blocks.MAGENTA_TERRACOTTA);
		map.put(DyeColor.LIGHT_BLUE, Blocks.LIGHT_BLUE_TERRACOTTA);
		map.put(DyeColor.YELLOW, Blocks.YELLOW_TERRACOTTA);
		map.put(DyeColor.LIME, Blocks.LIME_TERRACOTTA);
		map.put(DyeColor.PINK, Blocks.PINK_TERRACOTTA);
		map.put(DyeColor.GRAY, Blocks.GRAY_TERRACOTTA);
		map.put(DyeColor.LIGHT_GRAY, Blocks.LIGHT_GRAY_TERRACOTTA);
		map.put(DyeColor.CYAN, Blocks.CYAN_TERRACOTTA);
		map.put(DyeColor.PURPLE, Blocks.PURPLE_TERRACOTTA);
		map.put(DyeColor.BLUE, Blocks.BLUE_TERRACOTTA);
		map.put(DyeColor.BROWN, Blocks.BROWN_TERRACOTTA);
		map.put(DyeColor.GREEN, Blocks.GREEN_TERRACOTTA);
		map.put(DyeColor.RED, Blocks.RED_TERRACOTTA);
		map.put(DyeColor.BLACK, Blocks.BLACK_TERRACOTTA);
		return ImmutableMap.copyOf(map);
	});

	/**
	 * Equivalent to {@link BlockModelGenerators}{@code #ROTATION_HORIZONTAL_FACING}
	 */
	private static final Map<Direction, VariantMutator> HORIZONTAL_FACING = Util.make(() -> {
		var map = new EnumMap<Direction, VariantMutator>(Direction.class);
		map.put(Direction.EAST, Y_ROT_90);
		map.put(Direction.SOUTH, Y_ROT_180);
		map.put(Direction.WEST, Y_ROT_270);
		map.put(Direction.NORTH, NOP);
		return ImmutableMap.copyOf(map);
	});

	/**
	 * Equivalent to {@link BlockModelGenerators}{@code #ROTATION_HORIZONTAL_FACING_ALT}
	 */
	private static final Map<Direction, VariantMutator> HORIZONTAL_FACING_ALT = Util.make(() -> {
		var map = new EnumMap<Direction, VariantMutator>(Direction.class);
		map.put(Direction.SOUTH, NOP);
		map.put(Direction.WEST, Y_ROT_90);
		map.put(Direction.NORTH, Y_ROT_180);
		map.put(Direction.EAST, Y_ROT_270);
		return ImmutableMap.copyOf(map);
	});


	private static final PropertyDispatch<VariantMutator> ROTATION_FACING = getDispatch("ROTATION_FACING");

	private static final PropertyDispatch<VariantMutator> ROTATION_HORIZONTAL_FACING = getDispatch(
			"ROTATION_HORIZONTAL_FACING"
	);

	public ModBlockModelGenerators(
			final Consumer<BlockModelDefinitionGenerator> blockStateOutput,
			final ItemModelOutput itemModelOutput,
			final BiConsumer<ResourceLocation, ModelInstance> modelOutput
	) {
		super(blockStateOutput, itemModelOutput, modelOutput);
	}

	@Override
	public void run() {
		createWaterGrass();

		createSimpleBlockWithExistingParent(ModBlocks.LARGE_COLLISION_TEST.get(), Blocks.WHITE_WOOL);

		createRightClickTest();

		createPressurePlateDownWithTransforms(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get(), Blocks.IRON_BLOCK);

		createRotatableLamp();

		createSimpleBlockWithExistingParent(ModBlocks.ITEM_COLLISION_TEST.get(), Blocks.CYAN_WOOL);

		createFluidTank(ModBlocks.FLUID_TANK.get());
		createFluidTank(ModBlocks.FLUID_TANK_RESTRICTED.get());

		createSimpleBlockWithExistingParent(ModBlocks.ITEM_DEBUGGER.get(), Blocks.SEA_LANTERN);

		createEndPortalFrameFull();

		createSimpleBlockWithExistingParent(ModBlocks.POTION_EFFECT.get(), Blocks.COARSE_DIRT);

		createPressurePlateDownWithTransforms(ModBlocks.CLIENT_PLAYER_ROTATION.get(), Blocks.GOLD_BLOCK);

		createTrivialCube(ModBlocks.PIG_SPAWNER_REFILLER.get());

		createMirrorPlane();

		createCube(ModBlocks.VANILLA_MODEL_TEST.get(), Blocks.ACACIA_LOG, "_top");

		createFullbright();

		createCube(ModBlocks.NORMAL_BRIGHTNESS.get(), ModBlocks.FULLBRIGHT.get(), "");

		createTrivialCube(ModBlocks.MAX_HEALTH_SETTER.get());
		createTrivialCube(ModBlocks.MAX_HEALTH_GETTER.get());

		createSimpleBlockWithExistingParent(ModBlocks.SMALL_COLLISION_TEST.get(), Blocks.SEA_LANTERN);

		createChest();

		createHidden();

		createPipeBlock(ModBlocks.BASIC_PIPE.get(), Blocks.BRICKS, false);
		createPipeBlock(ModBlocks.FLUID_PIPE.get(), Blocks.GLASS, true);

		createCommandBlock(ModBlocks.SURVIVAL_COMMAND_BLOCK.get(), Blocks.COMMAND_BLOCK);
		createCommandBlock(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK.get(), Blocks.REPEATING_COMMAND_BLOCK);
		createCommandBlock(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK.get(), Blocks.CHAIN_COMMAND_BLOCK);

		createCrossCutoutBlock(ModBlocks.OAK_SAPLING.get(), Blocks.OAK_SAPLING);
		createCrossCutoutBlock(ModBlocks.SPRUCE_SAPLING.get(), Blocks.SPRUCE_SAPLING);
		createCrossCutoutBlock(ModBlocks.BIRCH_SAPLING.get(), Blocks.BIRCH_SAPLING);
		createCrossCutoutBlock(ModBlocks.JUNGLE_SAPLING.get(), Blocks.JUNGLE_SAPLING);
		createCrossCutoutBlock(ModBlocks.ACACIA_SAPLING.get(), Blocks.ACACIA_SAPLING);
		createCrossCutoutBlock(ModBlocks.DARK_OAK_SAPLING.get(), Blocks.DARK_OAK_SAPLING);

		createSimpleBlockWithExistingParent(ModBlocks.INVISIBLE.get(), Blocks.STONE);

		createTrivialCube(ModBlocks.PLANKS.get());

		ModBlocks.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.stream()
				.map(Supplier::get)
				.forEach(this::createColoredRotatableBlock);

		ModBlocks.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.stream()
				.map(Supplier::get)
				.forEach(this::createColoredMultiRotatableBlock);


		ModBlocks.VARIANTS_BLOCKS
				.getBlocks()
				.stream()
				.map(Supplier::get)
				.forEach(this::createTrivialCube);

		ModBlocks.TERRACOTTA_SLABS
				.getBlocks()
				.stream().map(Supplier::get)
				.forEach(this::createColouredSlab);

		createFluidBlock(ModFluids.STATIC);
		createFluidBlock(ModFluids.STATIC_GAS);
		createFluidBlock(ModFluids.NORMAL);
		createFluidBlock(ModFluids.NORMAL_GAS);
		createFluidBlock(ModFluids.PORTAL_DISPLACEMENT);
	}

	// Single block model generation
	private void createWaterGrass() {
		final var waterGrass = ModBlocks.WATER_GRASS.get();
		final var shortGrass = Blocks.SHORT_GRASS;

		final var textureMapping = TextureMapping.cross(shortGrass);

		final var model = plainVariant(
				ModModelTemplates.TINTED_CROSS_CUTOUT.create(waterGrass, textureMapping, modelOutput)
		);

		final var itemModel = createFlatItemModelWithBlockTexture(waterGrass.asItem(), shortGrass);

		blockStateOutput.accept(createSimpleBlock(waterGrass, model));

		registerSimpleTintedItemModel(waterGrass, itemModel, new GrassColorSource());
	}

	private void createRightClickTest() {
		final var rightClickTest = ModBlocks.RIGHT_CLICK_TEST.get();

		final var withEnderEyeLocation = existingParent(
				Blocks.WHITE_STAINED_GLASS,
				"",
				"_with_ender_eye"
		).create(rightClickTest, new TextureMapping(), modelOutput);

		final var withoutEnderEyeLocation = existingParent(
				Blocks.BLACK_STAINED_GLASS,
				"",
				"_without_ender_eye"
		).create(rightClickTest, new TextureMapping(), modelOutput);

		final var withEnderEye = plainVariant(withEnderEyeLocation);
		final var withoutEnderEye = plainVariant(withoutEnderEyeLocation);

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(rightClickTest)
						.with(
								createBooleanModelDispatch(
										RightClickTestBlock.HAS_ENDER_EYE,
										withEnderEye,
										withoutEnderEye
								)
						)
		);

		registerSimpleItemModel(rightClickTest, withoutEnderEyeLocation);
	}

	private void createRotatableLamp() {
		final var rotatableLamp = ModBlocks.ROTATABLE_LAMP.get();
		final var redstoneLamp = Blocks.REDSTONE_LAMP;

		final var off = plainVariant(
				ModelTemplates.CUBE_ORIENTABLE.create(
						rotatableLamp,
						ModTextureMappings.orientableSingle(rotatableLamp, redstoneLamp),
						modelOutput
				)
		);

		final var suffix = "_on";
		final var on = plainVariant(
				ModelTemplates.CUBE_ORIENTABLE.createWithSuffix(
						rotatableLamp,
						suffix,
						ModTextureMappings.orientableSingle(rotatableLamp, redstoneLamp, suffix),
						modelOutput
				)
		);

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(rotatableLamp)
						.with(
								createBooleanModelDispatch(
										RotatableLampBlock.LIT,
										on,
										off
								)
						)
						.with(ROTATION_FACING)
		);
	}

	private void createEndPortalFrameFull() {
		final var endPortalFrameFull = ModBlocks.END_PORTAL_FRAME_FULL.get();

		final var textureMapping = new TextureMapping()
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(endPortalFrameFull))
				.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(Blocks.END_STONE))
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(Blocks.END_PORTAL_FRAME, "_top"));

		final var model = plainVariant(
				ModelTemplates.CUBE_BOTTOM_TOP.create(endPortalFrameFull, textureMapping, modelOutput)
		);

		blockStateOutput.accept(createSimpleBlock(endPortalFrameFull, model));
	}

	private void createMirrorPlane() {
		final var block = ModBlocks.MIRROR_PLANE.get();

		final var sideSuffix = "_side";
		final var baseSuffix = "_base";
		final var planeSuffix = "_plane";
		final var tSuffix = "_t";

		final var textureMapping = new TextureMapping()
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(block, sideSuffix))
				.put(ModTextureSlots.BASE, TextureMapping.getBlockTexture(block, baseSuffix))
				.put(ModTextureSlots.PLANE, TextureMapping.getBlockTexture(block, planeSuffix));

		final var tTextureMapping = textureMapping.copyAndUpdate(
				ModTextureSlots.PLANE,
				TextureMapping.getBlockTexture(block, planeSuffix + tSuffix)
		);

		final var mirrorPlane = plainVariant(ModModelTemplates.PLANE_CUTOUT.create(block, textureMapping, modelOutput));

		final var mirrorPlaneTModel = ModModelTemplates.PLANE_CUTOUT.createWithSuffix(
				block,
				tSuffix,
				tTextureMapping,
				modelOutput
		);

		final var mirrorPlaneT = plainVariant(mirrorPlaneTModel);

		final var mirrorPlaneSide = plainVariant(ModModelTemplates.PLANE_SIDE_CUTOUT.create(block, textureMapping, modelOutput));

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(block).with(
						PropertyDispatch.initial(PlaneBlock.HORIZONTAL_ROTATION, PlaneBlock.VERTICAL_ROTATION)
								.generate((horizontalRotation, verticalRotation) -> {
									if (horizontalRotation == Direction.NORTH && verticalRotation == PlaneBlock.VerticalRotation.UP) {
										return mirrorPlaneT;
									} else if (verticalRotation == PlaneBlock.VerticalRotation.SIDE) {
										return mirrorPlaneSide.with(HORIZONTAL_FACING.get(horizontalRotation));
									} else if (verticalRotation == PlaneBlock.VerticalRotation.UP) {
										return mirrorPlane.with(HORIZONTAL_FACING.get(horizontalRotation));
									} else {
										return mirrorPlane.with(X_ROT_180).with(HORIZONTAL_FACING_ALT.get(horizontalRotation));
									}
								})
				)
		);

		registerSimpleItemModel(block, mirrorPlaneTModel);
	}

	private void createFullbright() {
		final var block = ModBlocks.FULLBRIGHT.get();

		final var textureMapping = TextureMapping.cube(block);

		final var model = plainVariant(ModModelTemplates.FULLBRIGHT.create(block, textureMapping, modelOutput));

		blockStateOutput.accept(createSimpleBlock(block, model));
	}

	private void createChest() {
		final var block = ModBlocks.CHEST.get();

		final var textureMapping = new TextureMapping()
				.put(
						ModTextureSlots.CHEST,
						ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "block/chest/wood")
				)
				.put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(Blocks.OAK_PLANKS));

		final var model = plainVariant(ModModelTemplates.CHEST.create(block, textureMapping, modelOutput));

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(block, model)
						.with(ROTATION_HORIZONTAL_FACING)
		);
	}

	private void createHidden() {
		final var block = ModBlocks.HIDDEN.get();

		final var empty = plainVariant(ResourceLocation.fromNamespaceAndPath(TestMod3.MODID, "block/empty"));
		final var hidden = plainVariant(TexturedModel.CUBE.create(block, modelOutput));

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(block)
						.with(createBooleanModelDispatch(HiddenBlock.HIDDEN, empty, hidden))
		);
	}

	// Generic block model generation
	private void createSimpleBlockWithExistingParent(final Block block, final Block parent) {
		final var template = existingParent(parent);

		final var model = plainVariant(template.create(block, new TextureMapping(), modelOutput));

		blockStateOutput.accept(createSimpleBlock(block, model));
	}

	private void createCube(final Block block, final Block textureBlock, final String textureSuffix) {
		final var textureMapping = TextureMapping.cube(
				TextureMapping.getBlockTexture(textureBlock, textureSuffix)
		);

		final var model = plainVariant(ModelTemplates.CUBE_ALL.create(block, textureMapping, modelOutput));

		blockStateOutput.accept(createSimpleBlock(block, model));
	}

	private void createPressurePlateDownWithTransforms(final Block block, final Block textureBlock) {
		final var textureMapping = TextureMapping.defaultTexture(textureBlock);

		final var model = plainVariant(
				ModModelTemplates.PRESSURE_PLATE_DOWN_WITH_TRANSFORMS.create(block, textureMapping, modelOutput)
		);

		blockStateOutput.accept(createSimpleBlock(block, model));
	}

	private void createFluidTank(final Block block) {
		final var textureMapping = ModTextureMappings.cubeBottomTop(Blocks.GLASS, Blocks.IRON_BLOCK, Blocks.GLASS);

		final var model = plainVariant(
				ModModelTemplates.CUBE_BOTTOM_TOP_CUTOUT.create(block, textureMapping, modelOutput)
		);

		blockStateOutput.accept(createSimpleBlock(block, model));
	}

	private void createPipeBlock(final BasePipeBlock block, final Block textureBlock, final boolean cutout) {
		final var textureMapping = TextureMapping.defaultTexture(textureBlock);

		final var centreModel = plainVariant(
				(cutout ? ModModelTemplates.PIPE_CENTRE_CUTOUT : ModModelTemplates.PIPE_CENTRE)
						.create(block, textureMapping, modelOutput)
		);

		final var sideModel = plainVariant(
				(cutout ? ModModelTemplates.PIPE_PART_CUTOUT : ModModelTemplates.PIPE_PART)
						.create(block, textureMapping, modelOutput)
		);

		final var itemModel = (cutout ? ModModelTemplates.PIPE_INVENTORY_CUTOUT : ModModelTemplates.PIPE_INVENTORY)
				.create(block.asItem(), textureMapping, modelOutput);

		blockStateOutput.accept(
				// createFence handles the horizontal properties
				((MultiPartGenerator) createFence(block, centreModel, sideModel))
						.with(
								condition().term(BlockStateProperties.UP, true),
								sideModel
										.with(X_ROT_270)
										.with(UV_LOCK)
						)
						.with(
								condition().term(BlockStateProperties.UP, true),
								sideModel
										.with(X_ROT_90)
										.with(UV_LOCK)
						)
		);

		registerSimpleItemModel(block, itemModel);
	}

	private void createCommandBlock(final Block block, final Block parent) {
		final var textureMapping = new TextureMapping();

		final var model = plainVariant(existingParent(parent).create(block, textureMapping, modelOutput));

		final var suffix = "_conditional";
		final var conditionalModel = plainVariant(
				existingParent(parent, suffix, suffix).create(
						block,
						textureMapping,
						modelOutput
				)
		);

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(block)
						.with(createBooleanModelDispatch(BlockStateProperties.CONDITIONAL, conditionalModel, model))
						.with(ROTATION_FACING)
		);
	}

	private void createCrossCutoutBlock(final Block block, final Block textureBlock) {
		final var textureMapping = TextureMapping.cross(textureBlock);

		final var model = plainVariant(ModModelTemplates.CROSS_CUTOUT.create(block, textureMapping, modelOutput));
		final var itemModel = createFlatItemModelWithBlockTexture(block.asItem(), textureBlock);

		blockStateOutput.accept(createSimpleBlock(block, model));

		registerSimpleItemModel(block, itemModel);
	}

	private void createColoredRotatableBlock(final ColoredRotatableBlock block) {
		final var textureMapping = ModTextureMappings.coloredRotatable(block.getColor(), "_front");

		final var modelLocation = ModelTemplates.CUBE_ORIENTABLE.create(
				RegistryUtil.getKey(block).withPrefix(COLORED_ROTATABLE_PREFIX),
				textureMapping,
				modelOutput
		);

		final var model = plainVariant(modelLocation);

		blockStateOutput.accept(
				MultiVariantGenerator
						.dispatch(
								block,
								model
						)
						.with(ROTATION_FACING)
		);

		registerSimpleItemModel(block, modelLocation);
	}

	private void createColoredMultiRotatableBlock(final ColoredMultiRotatableBlock block) {
		final var textureMapping = ModTextureMappings.coloredRotatable(
				block.getColor(),
				"_front_multi"
		);

		final var modelLocations = new EnumMap<EnumFaceRotation, ResourceLocation>(EnumFaceRotation.class);
		final var models = new EnumMap<EnumFaceRotation, MultiVariant>(EnumFaceRotation.class);

		Arrays.stream(EnumFaceRotation.values())
				.forEach(faceRotation -> {
					final var modelTemplate = ModModelTemplates.ROTATED_ORIENTABLES.get(faceRotation);

					final var modelLocation = RegistryUtil.getKey(block)
							.withPrefix(COLORED_ROTATABLE_PREFIX)
							.withSuffix(getSuffix(modelTemplate));

					final var model = modelTemplate.create(modelLocation, textureMapping, modelOutput);
					final var variant = plainVariant(model);

					modelLocations.put(faceRotation, model);
					models.put(faceRotation, variant);
				});

		blockStateOutput.accept(
				MultiVariantGenerator.dispatch(block)
						.with(
								PropertyDispatch.initial(ColoredMultiRotatableBlock.FACE_ROTATION)
										.generate(models::get)
						)
						.with(ROTATION_FACING)
		);

		registerSimpleItemModel(block, modelLocations.get(EnumFaceRotation.UP));
	}

	private void createColouredSlab(final ColouredSlabBlock block) {
		final var modelBlock = TERRACOTTA_BLOCKS.get(block.getVariant());

		final var textureMapping = TextureMapping.cube(modelBlock);

		final var bottomLocation = ModelTemplates.SLAB_BOTTOM.create(block, textureMapping, modelOutput);
		final var bottom = plainVariant(bottomLocation);
		final var top = plainVariant(ModelTemplates.SLAB_TOP.create(block, textureMapping, modelOutput));
		final var doubleSlab = plainVariant(ModelLocationUtils.getModelLocation(modelBlock));

		blockStateOutput.accept(
				BlockModelGenerators.createSlab(
						block,
						bottom,
						top,
						doubleSlab
				)
		);

		registerSimpleItemModel(block, bottomLocation);
	}

	private void createFluidBlock(final FluidGroup<?, ?, ?, ?, ?> fluidGroup) {
		// We can't use the RenderProperties for the fluid type as they're not initialised in datagen
		if (!(fluidGroup.getType().get() instanceof final BasicFluidType basicFluidType)) {
			throw new IllegalArgumentException("Fluid type must extend BasicFluidType");
		}

		final var block = fluidGroup.getBlock().get();

		final var textureMapping = TextureMapping.particle(basicFluidType.getStillTexture());

		final var model = plainVariant(ModelTemplates.PARTICLE_ONLY.create(block, textureMapping, modelOutput));

		blockStateOutput.accept(createSimpleBlock(block, model));
	}

	// ModelTemplate creation
	private static ModelTemplate existingParent(final Block parent) {
		return new ModelTemplate(
				Optional.of(ModelLocationUtils.getModelLocation(parent)),
				Optional.empty()
		);
	}

	private static ModelTemplate existingParent(final Block parent, final String parentSuffix, final String templateSuffix) {
		return new ModelTemplate(
				Optional.of(ModelLocationUtils.getModelLocation(parent, parentSuffix)),
				Optional.of(templateSuffix)
		);
	}

	@SuppressWarnings("unchecked")
	private static String getSuffix(final ModelTemplate modelTemplate) {
		try {
			return ((Optional<String>) SUFFIX.get(modelTemplate)).orElse("");
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Failed to get suffix for ModelTemplate", e);
		}
	}

	private static PropertyDispatch<VariantMutator> getDispatch(final String fieldName) {
		return Objects.requireNonNull(
				ObfuscationReflectionHelper.getPrivateValue(BlockModelGenerators.class, null, fieldName)
		);
	}
}
