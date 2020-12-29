package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BasePipeBlock;
import choonster.testmod3.block.slab.ColouredSlabBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.EnumFaceRotation;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyValue;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;
import java.util.function.Function;

/**
 * Generates this mod's block models and blockstate files.
 *
 * @author Choonster
 */
public class TestMod3BlockStateProvider extends BlockStateProvider {
	private static final Logger LOGGER = LogManager.getLogger();

	private static final int DEFAULT_ANGLE_OFFSET = 180;

	private final List<String> errors = new ArrayList<>();

	/**
	 * Centre cube of the pipe model.
	 */
	private final LazyValue<ModelFile> pipeCentre = new LazyValue<>(() ->
			models().withExistingParent("block/pipe/pipe_centre", mcLoc("block"))
					.texture("particle", "#centre")
					.element()
					.from(4, 4, 4)
					.to(12, 12, 12)
					.allFaces((direction, faceBuilder) -> faceBuilder.texture("#centre"))
					.end()
	);

	/**
	 * North side of the pipe model. Can be rotated for other sides.
	 */
	private final LazyValue<ModelFile> pipePart = new LazyValue<>(() ->
			models().withExistingParent("block/pipe/pipe_part", mcLoc("block"))
					.texture("particle", "#side")
					.element()
					.from(4, 4, 0)
					.to(12, 12, 12)
					.allFaces((direction, faceBuilder) -> faceBuilder.texture("#side"))
					.end()
	);

	/**
	 * Inventory model for pipe blocks.
	 */
	private final LazyValue<ModelFile> pipeInventory = new LazyValue<>(() ->
			itemModels().withExistingParent("item/pipe/inventory", "block/block")
					.texture("particle", "#all")
					.element()
					.from(4, 4, 0)
					.to(12, 12, 16)
					.allFaces((direction, faceBuilder) -> faceBuilder.texture("#all"))
					.end()
	);

	/**
	 * Orientable models for each {@link EnumFaceRotation} value.
	 */
	private final LazyValue<Map<EnumFaceRotation, ModelFile>> rotatedOrientables = new LazyValue<>(() -> {
		Map<EnumFaceRotation, ModelFile> map = new EnumMap<>(EnumFaceRotation.class);
		map.put(EnumFaceRotation.UP, existingMcModel("orientable"));

		Arrays.stream(EnumFaceRotation.values())
				.filter(faceRotation -> faceRotation != EnumFaceRotation.UP)
				.forEach(faceRotation -> {
					final ModelFile cube = models().getBuilder("cube_rotated_" + faceRotation.getString())
							.parent(existingMcModel("block"))
							.element()
							.allFaces((direction, faceBuilder) ->
									faceBuilder
											.texture("#" + direction.getString())
											.cullface(direction)
							)
							.allFaces((direction, faceBuilder) -> {
								switch (faceRotation) {
									case LEFT:
										faceBuilder.rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90);
										break;
									case RIGHT:
										faceBuilder.rotation(ModelBuilder.FaceRotation.CLOCKWISE_90);
										break;
									case DOWN:
										faceBuilder.rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN);
										break;
								}
							})
							.end();

					final ModelFile orientableWithBottom = models().getBuilder("orientable_with_bottom_rotated_" + faceRotation.getString())
							.parent(cube)

							.transforms()

							.transform(ModelBuilder.Perspective.FIRSTPERSON_RIGHT)
							.rotation(0, 135, 0)
							.scale(0.40f)
							.end()

							.end()

							.texture("particle", "#front")
							.texture("down", "#bottom")
							.texture("up", "#top")
							.texture("north", "#front")
							.texture("east", "#side")
							.texture("south", "#side")
							.texture("west", "#side");

					final ModelFile orientable = models().getBuilder("orientable_rotated_" + faceRotation.getString())
							.parent(orientableWithBottom)
							.texture("bottom", "#top");

					map.put(faceRotation, orientable);
				});

		return ImmutableMap.copyOf(map);
	});

	/**
	 * A copy of {@code minecraft:block/pressure_plate_down} that extends {@code minecraft:block/thin_block}
	 * so that it has the same display transformations as {@code minecraft:block/pressure_plate_up}.
	 */
	private final LazyValue<ModelFile> PRESSURE_PLATE_DOWN_WITH_TRANSFORMS = new LazyValue<>(() ->
			models().withExistingParent("block/pressure_plate_down_with_transforms", mcLoc("thin_block"))

					.element()
					.from(1, 0, 1)
					.to(15, 0.5f, 15)
					.textureAll("#texture")

					.allFaces((direction, faceBuilder) -> {
						if (direction.getAxis().isVertical()) {
							faceBuilder.uvs(1, 1, 15, 15);
						} else {
							faceBuilder.uvs(1, 15, 15, 15.5f);
						}
					})

					.face(Direction.DOWN)
					.cullface(Direction.DOWN)
					.end()

					.end()
	);

	public TestMod3BlockStateProvider(final DataGenerator gen, final ExistingFileHelper exFileHelper) {
		super(gen, TestMod3.MODID, exFileHelper);
	}

	@Override
	public String getName() {
		return "TestMod3BlockStates";
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlockWithExistingParent(ModBlocks.WATER_GRASS.get(), Blocks.GRASS);
		simpleBlockItem(ModBlocks.WATER_GRASS.get());

		simpleBlockWithExistingParent(ModBlocks.LARGE_COLLISION_TEST.get(), Blocks.WHITE_WOOL);
		simpleBlockItem(ModBlocks.LARGE_COLLISION_TEST.get());


		final BlockModelBuilder rightClickTestWithEnderEye = models()
				.getBuilder(name(ModBlocks.RIGHT_CLICK_TEST.get()) + "_with_ender_eye")
				.parent(existingModel(Blocks.WHITE_STAINED_GLASS));

		final BlockModelBuilder rightClickTestWithoutEnderEye = models()
				.getBuilder(name(ModBlocks.RIGHT_CLICK_TEST.get()) + "_without_ender_eye")
				.parent(existingModel(Blocks.BLACK_STAINED_GLASS));

		getVariantBuilder(ModBlocks.RIGHT_CLICK_TEST.get())
				.partialState()
				.with(RightClickTestBlock.HAS_ENDER_EYE, true)
				.modelForState()
				.modelFile(rightClickTestWithEnderEye)
				.addModel()

				.partialState()
				.with(RightClickTestBlock.HAS_ENDER_EYE, false)
				.modelForState()
				.modelFile(rightClickTestWithoutEnderEye)
				.addModel();

		simpleBlockItem(ModBlocks.RIGHT_CLICK_TEST.get(), rightClickTestWithoutEnderEye);


		final ModelFile clientPlayerRightClick = models().getBuilder(name(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get()))
				.parent(PRESSURE_PLATE_DOWN_WITH_TRANSFORMS.getValue())
				.texture("texture", mcLoc("block/iron_block"));

		simpleBlock(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get(), clientPlayerRightClick);
		simpleBlockItem(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK.get());

		final ModelFile rotatableLampOn = orientableSingle(
				name(ModBlocks.ROTATABLE_LAMP.get()) + "_on",
				mcLoc("block/redstone_lamp_on"),
				modLoc("block/rotatable_lamp_on")
		);

		final ModelFile rotatableLampOff = orientableSingle(
				name(ModBlocks.ROTATABLE_LAMP.get()) + "_off",
				mcLoc("block/redstone_lamp"),
				modLoc("block/rotatable_lamp_off")
		);

		directionalBlockUvLock(ModBlocks.ROTATABLE_LAMP.get(), state1 -> state1.get(RotatableLampBlock.LIT) ? rotatableLampOn : rotatableLampOff);
		simpleBlockItem(ModBlocks.ROTATABLE_LAMP.get(), rotatableLampOff);


		simpleBlockWithExistingParent(ModBlocks.ITEM_COLLISION_TEST.get(), Blocks.CYAN_WOOL);
		simpleBlockItem(ModBlocks.ITEM_COLLISION_TEST.get());


		final ModelFile fluidTank = models().cubeBottomTop(
				name(ModBlocks.FLUID_TANK.get()),
				mcLoc("block/glass"),
				mcLoc("block/iron_block"),
				mcLoc("block/glass")
		);

		simpleBlock(ModBlocks.FLUID_TANK.get(), fluidTank);
		simpleBlockItem(ModBlocks.FLUID_TANK.get());


		simpleBlockWithExistingParent(ModBlocks.FLUID_TANK_RESTRICTED.get(), fluidTank);
		simpleBlockItem(ModBlocks.FLUID_TANK_RESTRICTED.get());


		simpleBlockWithExistingParent(ModBlocks.ITEM_DEBUGGER.get(), Blocks.SEA_LANTERN);
		simpleBlockItem(ModBlocks.ITEM_DEBUGGER.get());


		final ModelFile endPortalFrameFull = models().cubeBottomTop(
				name(ModBlocks.END_PORTAL_FRAME_FULL.get()),
				modLoc("block/end_portal_side_full"),
				mcLoc("block/end_stone"),
				mcLoc("block/end_portal_frame_top")
		);

		simpleBlock(ModBlocks.END_PORTAL_FRAME_FULL.get(), endPortalFrameFull);
		simpleBlockItem(ModBlocks.END_PORTAL_FRAME_FULL.get(), endPortalFrameFull);


		simpleBlockWithExistingParent(ModBlocks.POTION_EFFECT.get(), Blocks.COARSE_DIRT);
		simpleBlockItem(ModBlocks.POTION_EFFECT.get());


		final ModelFile clientPlayerRotation = models().getBuilder(name(ModBlocks.CLIENT_PLAYER_ROTATION.get()))
				.parent(PRESSURE_PLATE_DOWN_WITH_TRANSFORMS.getValue())
				.texture("texture", mcLoc("block/gold_block"));

		simpleBlock(ModBlocks.CLIENT_PLAYER_ROTATION.get(), clientPlayerRotation);
		simpleBlockItem(ModBlocks.CLIENT_PLAYER_ROTATION.get(), clientPlayerRotation);


		simpleBlock(ModBlocks.PIG_SPAWNER_REFILLER.get());
		simpleBlockItem(ModBlocks.PIG_SPAWNER_REFILLER.get());


		final ModelFile plane = models().getExistingFile(modLoc("plane"));
		final ModelFile planeSide = models().getExistingFile(modLoc("plane_side"));

		final BlockModelBuilder mirrorPlane = models().getBuilder(name(ModBlocks.MIRROR_PLANE.get()))
				.parent(plane)
				.texture("side", modLoc("block/mirror_plane_side"))
				.texture("base", modLoc("block/mirror_plane_base"))
				.texture("plane", modLoc("block/mirror_plane_plane"));

		final BlockModelBuilder mirrorPlaneT = models().getBuilder(name(ModBlocks.MIRROR_PLANE.get()) + "_t")
				.parent(plane)
				.texture("side", modLoc("block/mirror_plane_side"))
				.texture("base", modLoc("block/mirror_plane_base"))
				.texture("plane", modLoc("block/mirror_plane_plane_t"));

		final BlockModelBuilder mirrorPlaneSide = models().getBuilder(name(ModBlocks.MIRROR_PLANE.get()) + "_side")
				.parent(planeSide)
				.texture("side", modLoc("block/mirror_plane_side"))
				.texture("base", modLoc("block/mirror_plane_base"))
				.texture("plane", modLoc("block/mirror_plane_plane"));

		getVariantBuilder(ModBlocks.MIRROR_PLANE.get())
				// Set the rotation and default model for all states
				.forAllStates(state -> {
					if (state.get(PlaneBlock.HORIZONTAL_ROTATION) == Direction.NORTH && state.get(PlaneBlock.VERTICAL_ROTATION) == PlaneBlock.VerticalRotation.UP) {
						return ConfiguredModel.builder()
								.modelFile(mirrorPlaneT)
								.build();
					} else if (state.get(PlaneBlock.VERTICAL_ROTATION) == PlaneBlock.VerticalRotation.SIDE) {
						return ConfiguredModel.builder()
								.modelFile(mirrorPlaneSide)
								.rotationY(getRotationY(state.get(PlaneBlock.HORIZONTAL_ROTATION)))
								.build();
					} else {
						return ConfiguredModel.builder()
								.modelFile(mirrorPlane)
								.rotationX((int) state.get(PlaneBlock.VERTICAL_ROTATION).getAngleDegrees())
								.rotationY(getRotationY(state.get(PlaneBlock.HORIZONTAL_ROTATION)))
								.build();
					}
				});

		simpleBlockItem(ModBlocks.MIRROR_PLANE.get(), mirrorPlaneT);


		final ModelFile vanillaModelTest = models().cubeAll(
				name(ModBlocks.VANILLA_MODEL_TEST.get()),
				mcLoc("block/acacia_log_top")
		);

		simpleBlock(ModBlocks.VANILLA_MODEL_TEST.get(), vanillaModelTest);
		simpleBlockItem(ModBlocks.VANILLA_MODEL_TEST.get(), vanillaModelTest);


		final ModelFile templateFullbright = models().withExistingParent("template_fullbright", mcLoc("block"))
				.texture("#particle", "#all")
				.element()
				.cube("#all")
				.shade(false)
				.end();

		final ModelFile fullbright = models().getBuilder(name(ModBlocks.FULLBRIGHT.get()))
				.parent(templateFullbright)
				.texture("all", blockTexture(ModBlocks.FULLBRIGHT.get()));

		simpleBlock(ModBlocks.FULLBRIGHT.get(), fullbright);
		simpleBlockItem(ModBlocks.FULLBRIGHT.get(), fullbright);


		final ModelFile normalBrightness = models().cubeAll(
				name(ModBlocks.NORMAL_BRIGHTNESS.get()),
				blockTexture(ModBlocks.FULLBRIGHT.get())
		);

		simpleBlock(ModBlocks.NORMAL_BRIGHTNESS.get(), normalBrightness);
		simpleBlockItem(ModBlocks.NORMAL_BRIGHTNESS.get(), normalBrightness);


		simpleBlock(ModBlocks.MAX_HEALTH_SETTER.get());
		simpleBlockItem(ModBlocks.MAX_HEALTH_SETTER.get());


		simpleBlock(ModBlocks.MAX_HEALTH_GETTER.get());
		simpleBlockItem(ModBlocks.MAX_HEALTH_GETTER.get());


		simpleBlockWithExistingParent(ModBlocks.SMALL_COLLISION_TEST.get(), existingModel(Blocks.SEA_LANTERN));
		simpleBlockItem(ModBlocks.SMALL_COLLISION_TEST.get());


		// I'm keeping template_chest as JSON since it's a relatively complex model
		final ModelFile chest = models().withExistingParent(name(ModBlocks.CHEST.get()), modLoc("template_chest"))
				.texture("chest", modLoc("block/chest/wood"))
				.texture("particle", blockTexture(Blocks.OAK_PLANKS));

		horizontalBlock(ModBlocks.CHEST.get(), chest);
		simpleBlockItem(ModBlocks.CHEST.get(), chest);


		final ModelFile empty = models().getBuilder("empty");
		final ModelFile hidden = cubeAll(ModBlocks.HIDDEN.get());

		getVariantBuilder(ModBlocks.HIDDEN.get())
				.partialState()
				.with(HiddenBlock.HIDDEN, true)
				.modelForState()
				.modelFile(empty)
				.addModel()

				.partialState()
				.with(HiddenBlock.HIDDEN, false)
				.modelForState()
				.modelFile(hidden)
				.addModel();

		simpleBlockItem(ModBlocks.HIDDEN.get(), hidden);


		pipeBlock(ModBlocks.BASIC_PIPE.get(), blockTexture(Blocks.BRICKS), blockTexture(Blocks.BRICKS));
		pipeBlockItem(ModBlocks.BASIC_PIPE.get(), blockTexture(Blocks.BRICKS));

		pipeBlock(ModBlocks.FLUID_PIPE.get(), blockTexture(Blocks.GLASS), blockTexture(Blocks.GLASS));
		pipeBlockItem(ModBlocks.FLUID_PIPE.get(), blockTexture(Blocks.GLASS));

		commandBlock(ModBlocks.SURVIVAL_COMMAND_BLOCK.get(), Blocks.COMMAND_BLOCK);
		simpleBlockItem(ModBlocks.SURVIVAL_COMMAND_BLOCK.get());

		commandBlock(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK.get(), Blocks.REPEATING_COMMAND_BLOCK);
		simpleBlockItem(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK.get());

		commandBlock(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK.get(), Blocks.CHAIN_COMMAND_BLOCK);
		simpleBlockItem(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK.get());

		simpleBlockWithExistingParent(ModBlocks.OAK_SAPLING.get(), Blocks.OAK_SAPLING);
		simpleBlockItemWithExistingParent(ModBlocks.OAK_SAPLING.get(), Items.OAK_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.SPRUCE_SAPLING.get(), Blocks.SPRUCE_SAPLING);
		simpleBlockItemWithExistingParent(ModBlocks.SPRUCE_SAPLING.get(), Items.SPRUCE_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.BIRCH_SAPLING.get(), Blocks.BIRCH_SAPLING);
		simpleBlockItemWithExistingParent(ModBlocks.BIRCH_SAPLING.get(), Items.BIRCH_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.JUNGLE_SAPLING.get(), Blocks.JUNGLE_SAPLING);
		simpleBlockItemWithExistingParent(ModBlocks.JUNGLE_SAPLING.get(), Items.JUNGLE_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.ACACIA_SAPLING.get(), Blocks.ACACIA_SAPLING);
		simpleBlockItemWithExistingParent(ModBlocks.ACACIA_SAPLING.get(), Items.ACACIA_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.DARK_OAK_SAPLING.get(), Blocks.DARK_OAK_SAPLING);
		simpleBlockItemWithExistingParent(ModBlocks.DARK_OAK_SAPLING.get(), Items.DARK_OAK_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.INVISIBLE.get(), Blocks.STONE);
		simpleBlockItem(ModBlocks.INVISIBLE.get());

		simpleBlock(ModBlocks.PLANKS.get());
		simpleBlockItem(ModBlocks.PLANKS.get());


		ModBlocks.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(blockRegistryObject -> {
					final ColoredRotatableBlock block = blockRegistryObject.get();

					final ModelFile modelFile = orientableSingle(
							"block/colored_rotatable/" + name(blockRegistryObject.get()),
							modLoc("block/colored_rotatable/" + block.getColor().getString()),
							modLoc("block/colored_rotatable/" + block.getColor().getString() + "_front")
					);

					directionalBlockUvLock(blockRegistryObject.get(), modelFile);
					simpleBlockItem(block, modelFile);
				});

		ModBlocks.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(blockRegistryObject -> {
					final ColoredMultiRotatableBlock block = blockRegistryObject.get();

					final ResourceLocation side = modLoc("block/colored_rotatable/" + block.getColor().getString());
					final ResourceLocation front = modLoc("block/colored_rotatable/" + block.getColor().getString() + "_front_multi");

					final EnumMap<EnumFaceRotation, ModelFile> models = new EnumMap<>(EnumFaceRotation.class);

					Arrays.stream(EnumFaceRotation.values())
							.forEach(faceRotation -> {
								String name = "block/colored_rotatable/" + name(blockRegistryObject.get());

								if (faceRotation != EnumFaceRotation.UP) {
									name += "_rotated_" + faceRotation.getString();
								}

								final ModelFile model = models().getBuilder(name)
										.parent(rotatedOrientables.getValue().get(faceRotation))
										.texture("side", side)
										.texture("front", front)
										.texture("top", side);

								models.put(faceRotation, model);
							});

					directionalBlockUvLock(blockRegistryObject.get(), (state) -> models.get(state.get(ColoredMultiRotatableBlock.FACE_ROTATION)));
					simpleBlockItem(block, models.get(EnumFaceRotation.UP));
				});


		ModBlocks.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(block -> {
					simpleBlock(block.get());
					simpleBlockItem(block.get());
				});


		ModBlocks.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(blockRegistryObject -> {
					final ColouredSlabBlock block = blockRegistryObject.get();
					slabBlock(
							block,
							mcLoc("block/" + block.getVariant().getString() + "_terracotta"),
							mcLoc("block/" + block.getVariant().getString() + "_terracotta")
					);
					simpleBlockItem(block);
				});


		validate();
	}

	private void validate() {
		for (final Block block : Sets.difference(RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS), registeredBlocks.keySet())) {
			// Ignore fluids, since they don't need block state files
			if (block instanceof FlowingFluidBlock) {
				continue;
			}

			blockstateError(block, "Missing block state file");
		}

		if (!errors.isEmpty()) {
			LOGGER.error("Found {} errors while generating blockstates/block models: ", errors.size());

			for (final String s : errors) {
				LOGGER.error("    {}", s);
			}

			throw new IllegalStateException("Failed to validate blockstates/block models, see logs");
		}
	}

	private void blockstateError(final Block block, final String fmt, final Object... args) {
		errors.add("Generated blockstate for block " + block + " " + String.format(fmt, args));
	}

	private ResourceLocation registryName(final Block block) {
		return Preconditions.checkNotNull(block.getRegistryName(), "Block %s has a null registry name", block);
	}

	private ResourceLocation registryName(final Item item) {
		return Preconditions.checkNotNull(item.getRegistryName(), "Item %s has a null registry name", item);
	}

	private String name(final Block block) {
		return registryName(block).getPath();
	}

	/**
	 * Gets an existing model with the block's registry name.
	 * <p>
	 * Note: This isn't guaranteed to be the actual model used by the block.
	 *
	 * @param block The block
	 * @return The model
	 */
	private ModelFile existingModel(final Block block) {
		return models().getExistingFile(registryName(block));
	}

	/**
	 * Gets an existing model with the item's registry name.
	 * <p>
	 * Note: This isn't guaranteed to be the actual model used by the item.
	 *
	 * @param item The item
	 * @return The model
	 */
	private ModelFile existingModel(final Item item) {
		return itemModels().getExistingFile(registryName(item));
	}

	/**
	 * Gets an existing model in the {@code minecraft} namespace with the specified name.
	 *
	 * @param name The model name
	 * @return The model
	 */
	private ModelFile existingMcModel(final String name) {
		return models().getExistingFile(new ResourceLocation("minecraft", name));
	}

	private void simpleBlockItem(final Block block) {
		simpleBlockItem(block, existingModel(block));
	}

	private void simpleBlockItemWithExistingParent(final Block block, final Item item) {
		simpleBlockItem(block, existingModel(item));
	}

	private ModelFile orientableSingle(final String name, final ResourceLocation side, final ResourceLocation front) {
		return models().orientable(name, side, front, side);
	}

	private void simpleBlockWithExistingParent(final Block block, final Block parentBlock) {
		simpleBlockWithExistingParent(block, existingModel(parentBlock));
	}

	private void simpleBlockWithExistingParent(final Block block, final ModelFile parentModel) {
		simpleBlock(
				block,
				models().getBuilder(name(block))
						.parent(parentModel)
		);
	}

	protected void directionalBlockUvLock(final Block block, final ModelFile model) {
		directionalBlockUvLock(block, $ -> model);
	}

	protected void directionalBlockUvLock(final Block block, final Function<BlockState, ModelFile> modelFunc) {
		getVariantBuilder(block)
				.forAllStates(state -> {
					final Direction direction = state.get(BlockStateProperties.FACING);
					return ConfiguredModel.builder()
							.modelFile(modelFunc.apply(state))
							.rotationX(getRotationX(direction))
							.rotationY(getRotationY(direction))
							.build();
				});
	}

	private void pipeBlock(final BasePipeBlock block, final ResourceLocation centre, final ResourceLocation side) {
		final ModelFile centreModel = models().getBuilder(name(block) + "_centre")
				.parent(pipeCentre.getValue())
				.texture("centre", centre);

		final ModelFile sideModel = models().getBuilder(name(block) + "_side")
				.parent(pipePart.getValue())
				.texture("side", side);

		final MultiPartBlockStateBuilder multiPartBuilder = getMultipartBuilder(block);

		multiPartBuilder
				.part()
				.modelFile(centreModel)
				.uvLock(true)
				.addModel()
				.end();

		for (final Direction direction : Direction.values()) {
			multiPartBuilder
					.part()
					.modelFile(sideModel)
					.uvLock(true)
					.rotationX(getRotationX(direction))
					.rotationY(getRotationY(direction))
					.addModel()
					.condition(BasePipeBlock.getConnectedProperty(direction), true);
		}
	}

	private void pipeBlockItem(final Block block, final ResourceLocation texture) {
		itemModels().getBuilder(name(block))
				.parent(pipeInventory.getValue())
				.texture("all", texture);
	}

	private void commandBlock(final CommandBlockBlock commandBlock, final Block modelCommandBlock) {
		final ModelFile normalModel = models().withExistingParent(name(commandBlock), name(modelCommandBlock));
		final ModelFile conditionalModel = models().withExistingParent(name(commandBlock) + "_conditional", name(modelCommandBlock) + "_conditional");

		getVariantBuilder(commandBlock)
				.forAllStates(state -> {
					final Direction direction = state.get(CommandBlockBlock.FACING);
					final boolean conditional = state.get(CommandBlockBlock.CONDITIONAL);

					final ModelFile modelFile = conditional ? conditionalModel : normalModel;

					return ConfiguredModel.builder()
							.modelFile(modelFile)
							.rotationX(getRotationX(direction))
							.rotationY(getRotationY(direction))
							.build();
				});
	}

	private int getRotationX(final Direction direction) {
		return direction == Direction.DOWN ? 90 : direction.getAxis().isHorizontal() ? 0 : -90;
	}

	private int getRotationY(final Direction direction) {
		return direction.getAxis().isVertical() ? 0 : (((int) direction.getHorizontalAngle()) + DEFAULT_ANGLE_OFFSET) % 360;
	}
}
