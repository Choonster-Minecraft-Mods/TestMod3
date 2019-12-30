package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.*;
import choonster.testmod3.block.pipe.BasePipeBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.EnumFaceRotation;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.block.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.Direction;
import net.minecraft.util.LazyLoadBase;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ValidationResults;
import net.minecraftforge.client.model.generators.*;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Generates this mod's block models and blockstate files.
 *
 * @author Choonster
 */
public class TestMod3BlockStateProvider extends BlockStateProvider {
	private static final Logger LOGGER = LogManager.getLogger();

	/**
	 * Centre cube of the pipe model.
	 */
	private final LazyLoadBase<ModelFile> pipeCentre = new LazyLoadBase<>(() ->
			withExistingParent("block/pipe/pipe_centre", mcLoc("block"))
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
	private final LazyLoadBase<ModelFile> pipePart = new LazyLoadBase<>(() ->
			withExistingParent("block/pipe/pipe_part", mcLoc("block"))
					.texture("particle", "#side")
					.element()
					.from(4, 4, 0)
					.to(12, 12, 12)
					.allFaces((direction, faceBuilder) -> faceBuilder.texture("#side"))
					.end()
	);

	/**
	 * Orientable models for each {@link EnumFaceRotation} value.
	 */
	private final LazyLoadBase<Map<EnumFaceRotation, ModelFile>> rotatedOrientables = new LazyLoadBase<>(() -> {
		Map<EnumFaceRotation, ModelFile> map = new EnumMap<>(EnumFaceRotation.class);
		map.put(EnumFaceRotation.UP, existingMcModel("orientable"));

		Arrays.stream(EnumFaceRotation.values())
				.filter(faceRotation -> faceRotation != EnumFaceRotation.UP)
				.forEach(faceRotation -> {
					final ModelFile cube = getBuilder("cube_rotated_" + faceRotation.getName())
							.parent(existingMcModel("block"))
							.element()
							.allFaces((direction, faceBuilder) ->
									faceBuilder
											.texture("#" + direction.getName())
											.cullface(direction)
							)
							.allFaces((direction, faceBuilder) -> {
								switch (faceRotation) {
									case LEFT:
										faceBuilder.rotation(ModelBuilder.FaceRotation.COUNTERCLOCKWISE_90);
									case RIGHT:
										faceBuilder.rotation(ModelBuilder.FaceRotation.CLOCKWISE_90);
									case DOWN:
										faceBuilder.rotation(ModelBuilder.FaceRotation.UPSIDE_DOWN);
								}
							})
							.end();

					final ModelFile orientableWithBottom = getBuilder("orientable_with_bottom_rotated_" + faceRotation.getName())
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

					final ModelFile orientable = getBuilder("orientable_rotated_" + faceRotation.getName())
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
	private final LazyLoadBase<ModelFile> PRESSURE_PLATE_DOWN_WITH_TRANSFORMS = new LazyLoadBase<>(() ->
			withExistingParent("block/pressure_plate_down_with_transforms", mcLoc("thin_block"))

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

	public ExistingFileHelper getExistingFileHelper() {
		return existingFileHelper;
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlockWithExistingParent(ModBlocks.WATER_GRASS, Blocks.GRASS);

		simpleBlockWithExistingParent(ModBlocks.LARGE_COLLISION_TEST, Blocks.WHITE_WOOL);


		getVariantBuilder(ModBlocks.RIGHT_CLICK_TEST)
				.partialState()
				.with(RightClickTestBlock.HAS_ENDER_EYE, true)
				.modelForState()
				.modelFile(getBuilder(name(ModBlocks.RIGHT_CLICK_TEST) + "_with_ender_eye")
						.parent(existingModel(Blocks.WHITE_STAINED_GLASS)))
				.addModel()

				.partialState()
				.with(RightClickTestBlock.HAS_ENDER_EYE, false)
				.modelForState()
				.modelFile(getBuilder(name(ModBlocks.RIGHT_CLICK_TEST) + "_without_ender_eye")
						.parent(existingModel(Blocks.BLACK_STAINED_GLASS)))
				.addModel();


		final ModelFile clientPlayerRightClick = getBuilder(name(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK))
				.parent(PRESSURE_PLATE_DOWN_WITH_TRANSFORMS.getValue())
				.texture("texture", mcLoc("block/iron_block"));

		simpleBlock(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK, clientPlayerRightClick);


		final ModelFile rotatableLampOn = orientableSingle(
				name(ModBlocks.ROTATABLE_LAMP) + "_on",
				mcLoc("block/redstone_lamp_on"),
				modLoc("block/rotatable_lamp_on")
		);

		final ModelFile rotatableLampOff = orientableSingle(
				name(ModBlocks.ROTATABLE_LAMP) + "_off",
				mcLoc("block/redstone_lamp"),
				modLoc("block/rotatable_lamp_off")
		);

		directionalBlockUvLock(
				ModBlocks.ROTATABLE_LAMP,
				state -> state.get(RotatableLampBlock.LIT) ? rotatableLampOn : rotatableLampOff
		);


		simpleBlockWithExistingParent(ModBlocks.ITEM_COLLISION_TEST, Blocks.CYAN_WOOL);


		final ModelFile fluidTank = cubeBottomTop(
				name(ModBlocks.FLUID_TANK),
				mcLoc("block/glass"),
				mcLoc("block/iron_block"),
				mcLoc("block/glass")
		);

		simpleBlock(ModBlocks.FLUID_TANK, fluidTank);

		simpleBlockWithExistingParent(ModBlocks.FLUID_TANK_RESTRICTED, fluidTank);


		simpleBlockWithExistingParent(ModBlocks.ITEM_DEBUGGER, Blocks.SEA_LANTERN);


		final ModelFile endPortalFrameFull = cubeBottomTop(
				name(ModBlocks.END_PORTAL_FRAME_FULL),
				modLoc("block/end_portal_side_full"),
				mcLoc("block/end_stone"),
				mcLoc("block/end_portal_frame_top")
		);

		simpleBlock(ModBlocks.END_PORTAL_FRAME_FULL, endPortalFrameFull);


		simpleBlockWithExistingParent(ModBlocks.POTION_EFFECT, Blocks.COARSE_DIRT);


		final ModelFile clientPlayerRotation = getBuilder(name(ModBlocks.CLIENT_PLAYER_ROTATION))
				.parent(PRESSURE_PLATE_DOWN_WITH_TRANSFORMS.getValue())
				.texture("texture", mcLoc("block/gold_block"));

		simpleBlock(ModBlocks.CLIENT_PLAYER_ROTATION, clientPlayerRotation);


		simpleBlock(ModBlocks.PIG_SPAWNER_REFILLER);


		final ModelFile plane = withExistingParent("plane", mcLoc("block"))
				// Base
				.element()

				.face(Direction.EAST)
				.texture("#side")
				.end()

				.face(Direction.NORTH)
				.texture("#base")
				.end()

				.face(Direction.WEST)
				.texture("#side")
				.end()

				.face(Direction.DOWN)
				.texture("#base")
				.end()

				.end()

				// Plane
				.element()

				.rotation(null) // TODO: Remove parameter when MinecraftForge/MinecraftForge#6321 is fixed
				.origin(0, 16, 0)
				.axis(Direction.Axis.X)
//				.angle(-45) // TODO: Angle validation is broken, see MinecraftForge/MinecraftForge#6323
				.rescale(true)
				.end()

				.face(Direction.SOUTH)
				.texture("#plane")
				.end()

				.end();

		final BlockModelBuilder mirrorPlane = getBuilder(name(ModBlocks.MIRROR_PLANE))
				.parent(plane)
				.texture("side", modLoc("block/mirror_plane_side"))
				.texture("base", modLoc("block/mirror_plane_base"))
				.texture("plane", modLoc("block/mirror_plane_plane"));

		final BlockModelBuilder mirrorPlaneT = getBuilder(name(ModBlocks.MIRROR_PLANE) + "_t")
				.parent(plane)
				.texture("side", modLoc("block/mirror_plane_side"))
				.texture("base", modLoc("block/mirror_plane_base"))
				.texture("plane", modLoc("block/mirror_plane_plane_t"));

		getVariantBuilder(ModBlocks.MIRROR_PLANE)
				// Set the rotation and default model for all states
				.forAllStates(state -> {
					if (state.get(PlaneBlock.HORIZONTAL_ROTATION) == Direction.NORTH && state.get(PlaneBlock.VERTICAL_ROTATION) == PlaneBlock.VerticalRotation.UP) {
						return ConfiguredModel.builder()
								.modelFile(mirrorPlaneT)
								.build();
					} else {
						return ConfiguredModel.builder()
								.modelFile(mirrorPlane)
								.rotationX((int) state.get(PlaneBlock.HORIZONTAL_ROTATION).getHorizontalAngle())
								.rotationY((int) state.get(PlaneBlock.VERTICAL_ROTATION).getAngleDegrees())
								.build();
					}
				})
		;


		final ModelFile vanillaModelTest = cubeAll(
				name(ModBlocks.VANILLA_MODEL_TEST),
				mcLoc("block/acacia_log_top")
		);

		simpleBlock(ModBlocks.VANILLA_MODEL_TEST, vanillaModelTest);


		final ModelFile templateFullbright = withExistingParent("template_fullbright", mcLoc("block"))
				.texture("#particle", "#all")
				.element()
				.cube("#all")
				.shade(false)
				.end();

		final ModelFile fullbright = getBuilder(name(ModBlocks.FULLBRIGHT))
				.parent(templateFullbright)
				.texture("all", blockTexture(ModBlocks.FULLBRIGHT));

		simpleBlock(ModBlocks.FULLBRIGHT, fullbright);


		final ModelFile normalBrightness = cubeAll(
				name(ModBlocks.NORMAL_BRIGHTNESS),
				blockTexture(ModBlocks.FULLBRIGHT)
		);

		simpleBlock(ModBlocks.NORMAL_BRIGHTNESS, normalBrightness);


		simpleBlock(ModBlocks.MAX_HEALTH_SETTER);

		simpleBlock(ModBlocks.MAX_HEALTH_GETTER);

		simpleBlockWithExistingParent(ModBlocks.SMALL_COLLISION_TEST, existingModel(Blocks.SEA_LANTERN));


		// I'm keeping template_chest as JSON since it's a relatively complex model
		final ModelFile chest = withExistingParent(name(ModBlocks.CHEST), modLoc("template_chest"))
				.texture("chest", modLoc("block/chest/wood"))
				.texture("particle", blockTexture(Blocks.OAK_PLANKS));

		simpleBlock(ModBlocks.CHEST, chest);


		final ModelFile empty = getBuilder("empty");
		final ModelFile hidden = cubeAll(ModBlocks.HIDDEN);

		getVariantBuilder(ModBlocks.HIDDEN)
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


		pipeBlock(ModBlocks.BASIC_PIPE, blockTexture(Blocks.BRICKS), blockTexture(Blocks.BRICKS));

		pipeBlock(ModBlocks.FLUID_PIPE, blockTexture(Blocks.GLASS), blockTexture(Blocks.GLASS));

		commandBlock(ModBlocks.SURVIVAL_COMMAND_BLOCK, Blocks.COMMAND_BLOCK);

		commandBlock(ModBlocks.REPEATING_SURVIVAL_COMMAND_BLOCK, Blocks.REPEATING_COMMAND_BLOCK);

		commandBlock(ModBlocks.CHAIN_SURVIVAL_COMMAND_BLOCK, Blocks.CHAIN_COMMAND_BLOCK);

		simpleBlockWithExistingParent(ModBlocks.OAK_SAPLING, Blocks.OAK_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.SPRUCE_SAPLING, Blocks.SPRUCE_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.BIRCH_SAPLING, Blocks.BIRCH_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.JUNGLE_SAPLING, Blocks.JUNGLE_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.ACACIA_SAPLING, Blocks.ACACIA_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.DARK_OAK_SAPLING, Blocks.DARK_OAK_SAPLING);

		simpleBlockWithExistingParent(ModBlocks.INVISIBLE, Blocks.STONE);

		simpleBlock(ModBlocks.PLANKS);


		ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> {
					final ModelFile modelFile = orientableSingle(
							"block/colored_rotatable/" + name(block),
							modLoc("block/colored_rotatable/" + block.getColor().getName()),
							modLoc("block/colored_rotatable/" + block.getColor().getName() + "_front")
					);

					directionalBlockUvLock(block, modelFile);
				});

		ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> {
					final ResourceLocation side = modLoc("block/colored_rotatable/" + block.getColor().getName());
					final ResourceLocation front = modLoc("block/colored_rotatable/" + block.getColor().getName() + "_front_multi");

					final EnumMap<EnumFaceRotation, ModelFile> models = new EnumMap<>(EnumFaceRotation.class);

					Arrays.stream(EnumFaceRotation.values())
							.forEach(faceRotation -> {
								String name = "block/colored_rotatable/" + name(block);

								if (faceRotation != EnumFaceRotation.UP) {
									name += "_rotated_" + faceRotation.getName();
								}

								final ModelFile model = getBuilder(name)
										.parent(rotatedOrientables.getValue().get(faceRotation))
										.texture("side", side)
										.texture("front", front)
										.texture("top", side);

								models.put(faceRotation, model);
							});

					directionalBlockUvLock(block, (state) -> models.get(state.get(ColoredMultiRotatableBlock.FACE_ROTATION)));
				});


		ModBlocks.VariantGroups.VARIANTS_BLOCKS
				.getBlocks()
				.forEach(this::simpleBlock);


		ModBlocks.VariantGroups.TERRACOTTA_SLABS
				.getBlocks()
				.forEach(block ->
						slabBlock(
								block,
								mcLoc("block/" + block.getVariant().getName() + "_terracotta"),
								mcLoc("block/" + block.getVariant().getName() + "_terracotta")
						)
				);


		validate();
	}

	private void validate() {
		final ValidationResults validationresults = new ValidationResults();

		for (final Block block : Sets.difference(RegistryUtil.getModRegistryEntries(ForgeRegistries.BLOCKS), registeredBlocks.keySet())) {
			// Ignore fluids, since they don't need block state files
			if (block instanceof FlowingFluidBlock) {
				continue;
			}

			validationresults.addProblem("Missing block state file for: " + block.getRegistryName());
		}

		final Multimap<String, String> problems = validationresults.getProblems();
		if (!problems.isEmpty()) {
			problems.forEach((path, message) -> {
				LOGGER.warn("Found validation problem in " + path + ": " + message);
			});

			throw new IllegalStateException("Failed to validate block states/block models, see logs");
		}
	}

	private ResourceLocation registryName(final Block block) {
		return Preconditions.checkNotNull(block.getRegistryName(), "Block %s has a null registry name", block);
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
		return getExistingFile(registryName(block));
	}

	/**
	 * Gets an existing model in the {@code minecraft} namespace with the specified name.
	 *
	 * @param name The model name
	 * @return The model
	 */
	private ModelFile existingMcModel(final String name) {
		return getExistingFile(new ResourceLocation("minecraft", name));
	}

	private ModelFile orientableSingle(final String name, final ResourceLocation side, final ResourceLocation front) {
		return orientable(name, side, front, side);
	}

	private void simpleBlockWithExistingParent(final Block block, final Block parentBlock) {
		simpleBlockWithExistingParent(block, existingModel(parentBlock));
	}

	private void simpleBlockWithExistingParent(final Block block, final ModelFile parentModel) {
		simpleBlock(
				block,
				getBuilder(name(block))
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
		final ModelFile centreModel = getBuilder(name(block) + "_centre")
				.parent(pipeCentre.getValue())
				.texture("centre", centre);

		final ModelFile sideModel = getBuilder(name(block) + "_side")
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
					.rotationX(direction == Direction.DOWN ? 90 : direction.getAxis().isHorizontal() ? 0 : -90)
					.rotationY(getRotationY(direction.getOpposite()))
					.addModel()
					.condition(BasePipeBlock.getConnectedProperty(direction), true);
		}
	}

	private void commandBlock(final CommandBlockBlock commandBlock, final Block modelCommandBlock) {
		final ModelFile normalModel = withExistingParent(name(commandBlock), name(modelCommandBlock));
		final ModelFile conditionalModel = withExistingParent(name(commandBlock) + "_conditional", name(modelCommandBlock) + "_conditional");

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
		return direction == Direction.DOWN ? 180 : direction.getAxis().isHorizontal() ? 90 : 0;
	}

	private int getRotationY(final Direction direction) {
		return direction.getAxis().isVertical() ? 0 : (int) direction.getHorizontalAngle();
	}
}
