package choonster.testmod3.data;

import choonster.testmod3.TestMod3;
import choonster.testmod3.block.HiddenBlock;
import choonster.testmod3.block.PlaneBlock;
import choonster.testmod3.block.RightClickTestBlock;
import choonster.testmod3.block.RotatableLampBlock;
import choonster.testmod3.block.pipe.BasePipeBlock;
import choonster.testmod3.init.ModBlocks;
import choonster.testmod3.util.RegistryUtil;
import com.google.common.base.Preconditions;
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

	public TestMod3BlockStateProvider(final DataGenerator gen, final ExistingFileHelper exFileHelper) {
		super(gen, TestMod3.MODID, exFileHelper);
	}

	@Override
	public String getName() {
		return "TestMod3BlockStates";
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlock(ModBlocks.WATER_GRASS, existingModel(Blocks.GRASS));

		simpleBlock(ModBlocks.LARGE_COLLISION_TEST, existingModel(Blocks.WHITE_WOOL));


		getVariantBuilder(ModBlocks.RIGHT_CLICK_TEST)
				.partialState()
				.with(RightClickTestBlock.HAS_ENDER_EYE, true)
				.modelForState()
				.modelFile(existingModel(Blocks.WHITE_STAINED_GLASS))
				.addModel()

				.partialState()
				.with(RightClickTestBlock.HAS_ENDER_EYE, false)
				.modelForState()
				.modelFile(existingModel(Blocks.BLACK_STAINED_GLASS))
				.addModel();


		simpleBlock(ModBlocks.CLIENT_PLAYER_RIGHT_CLICK, existingMcModel(name(Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE) + "_down"));


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


		simpleBlock(ModBlocks.ITEM_COLLISION_TEST, existingModel(Blocks.CYAN_WOOL));


		final ModelFile fluidTank = cubeBottomTop(
				name(ModBlocks.FLUID_TANK),
				mcLoc("block/glass"),
				mcLoc("block/iron_block"),
				mcLoc("block/glass")
		);

		simpleBlock(ModBlocks.FLUID_TANK, fluidTank);

		simpleBlock(ModBlocks.FLUID_TANK_RESTRICTED, fluidTank);


		simpleBlock(ModBlocks.ITEM_DEBUGGER, existingModel(Blocks.SEA_LANTERN));


		final ModelFile endPortalFrameFull = cubeBottomTop(
				name(ModBlocks.END_PORTAL_FRAME_FULL),
				modLoc("block/end_portal_side_full"),
				mcLoc("block/end_stone"),
				mcLoc("block/end_portal_frame_top")
		);

		simpleBlock(ModBlocks.END_PORTAL_FRAME_FULL, endPortalFrameFull);


		simpleBlock(ModBlocks.POTION_EFFECT, existingModel(Blocks.COARSE_DIRT));

		simpleBlock(ModBlocks.CLIENT_PLAYER_ROTATION, existingMcModel(name(Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE) + "_down"));

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
				.forAllStates(state ->
						ConfiguredModel.builder()
								.modelFile(mirrorPlane)
								.rotationX((int) state.get(PlaneBlock.HORIZONTAL_ROTATION).getHorizontalAngle())
								.rotationY((int) state.get(PlaneBlock.VERTICAL_ROTATION).getAngleDegrees())
								.build()
				)

//				// Set the model for the horizontal_rotation=north, vertical_rotation=up state
//				.partialState() // TODO: How to special-case this state?
//				.with(PlaneBlock.HORIZONTAL_ROTATION, Direction.NORTH)
//				.with(PlaneBlock.VERTICAL_ROTATION, PlaneBlock.VerticalRotation.UP)
//				.modelForState()
//				.modelFile(mirrorPlaneT)
//				.addModel()
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

		simpleBlock(ModBlocks.SMALL_COLLISION_TEST, existingModel(Blocks.SEA_LANTERN));


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

		simpleBlock(ModBlocks.OAK_SAPLING, existingModel(Blocks.OAK_SAPLING));

		simpleBlock(ModBlocks.SPRUCE_SAPLING, existingModel(Blocks.SPRUCE_SAPLING));

		simpleBlock(ModBlocks.BIRCH_SAPLING, existingModel(Blocks.BIRCH_SAPLING));

		simpleBlock(ModBlocks.JUNGLE_SAPLING, existingModel(Blocks.JUNGLE_SAPLING));

		simpleBlock(ModBlocks.ACACIA_SAPLING, existingModel(Blocks.ACACIA_SAPLING));

		simpleBlock(ModBlocks.DARK_OAK_SAPLING, existingModel(Blocks.DARK_OAK_SAPLING));

		simpleBlock(ModBlocks.INVISIBLE, existingModel(Blocks.STONE));

		simpleBlock(ModBlocks.PLANKS);


		ModBlocks.VariantGroups.COLORED_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> {
					final ModelFile modelFile = orientableSingle(
							name(block),
							modLoc("block/colored_rotatable/" + block.getColor().getName()),
							modLoc("block/colored_rotatable/" + block.getColor().getName() + "_front")
					);

					directionalBlockUvLock(block, modelFile);
				});

		ModBlocks.VariantGroups.COLORED_MULTI_ROTATABLE_BLOCKS
				.getBlocks()
				.forEach(block -> {
					final ModelFile modelFile = orientableSingle(
							name(block),
							modLoc("block/colored_rotatable/" + block.getColor().getName()),
							modLoc("block/colored_rotatable/" + block.getColor().getName() + "_front_multi")
					);

					directionalBlockUvLock(block, modelFile);
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
		final ModelFile normalModel = existingModel(modelCommandBlock);
		final ModelFile conditionalModel = existingMcModel(name(modelCommandBlock) + "_conditional");

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
