package choonster.testmod3.data.models.model;

import choonster.testmod3.TestMod3;
import choonster.testmod3.util.EnumFaceRotation;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.client.data.models.model.ModelTemplate;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Util;

import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Creates this mod's {@link ModelTemplate ModelTemplates}.
 *
 * @author Choonster
 */
public class ModModelTemplates {
	private static final Identifier CUTOUT = Identifier.withDefaultNamespace("cutout");

	/**
	 * Centre cube of the pipe model.
	 */
	public static final ModelTemplate PIPE_CENTRE = createMod(
			"pipe/pipe_centre",
			"_centre",
			TextureSlot.TEXTURE
	);

	/**
	 * North side of the pipe model. Can be rotated for other sides.
	 */
	public static final ModelTemplate PIPE_PART = createMod(
			"pipe/pipe_part",
			"_side",
			TextureSlot.TEXTURE
	);

	/**
	 * Inventory model for pipe blocks.
	 */
	public static final ModelTemplate PIPE_INVENTORY = createModItem(
			"pipe/inventory",
			TextureSlot.TEXTURE
	);

	/**
	 * Centre cube of the pipe model.
	 */
	public static final ModelTemplate PIPE_CENTRE_CUTOUT = createCutoutMod(
			"pipe/pipe_centre",
			"_centre",
			TextureSlot.TEXTURE
	);

	/**
	 * North side of the pipe model. Can be rotated for other sides.
	 */
	public static final ModelTemplate PIPE_PART_CUTOUT = createCutoutMod(
			"pipe/pipe_part",
			"_side",
			TextureSlot.TEXTURE
	);

	/**
	 * Inventory model for pipe blocks.
	 */
	public static final ModelTemplate PIPE_INVENTORY_CUTOUT = createCutoutModItem(
			"pipe/inventory",
			TextureSlot.TEXTURE
	);

	/**
	 * Orientable models for each {@link EnumFaceRotation} value.
	 */
	public static final Map<EnumFaceRotation, ModelTemplate> ROTATED_ORIENTABLES = Util.make(() -> {
		final var map = Arrays.stream(EnumFaceRotation.values())
				.map(faceRotation -> {
					if (faceRotation == EnumFaceRotation.UP) {
						return Pair.of(faceRotation, ModelTemplates.CUBE_ORIENTABLE);
					}

					var suffix = "_rotated_" + faceRotation.getSerializedName();
					return Pair.of(
							faceRotation,
							createMod(
									"orientable" + suffix,
									suffix,
									TextureSlot.TOP,
									TextureSlot.FRONT,
									TextureSlot.SIDE
							)
					);
				})
				.collect(Collectors.toMap(Pair::getFirst, Pair::getSecond));

		return ImmutableMap.copyOf(new EnumMap<>(map));
	});

	/**
	 * A copy of {@code minecraft:block/pressure_plate_down} that extends {@code minecraft:block/thin_block}
	 * so that it has the same display transformations as {@code minecraft:block/pressure_plate_up}.
	 */
	public static final ModelTemplate PRESSURE_PLATE_DOWN_WITH_TRANSFORMS = createMod(
			"pressure_plate_down_with_transforms",
			TextureSlot.TEXTURE
	);

	public static final ModelTemplate PLANE_CUTOUT = createCutoutMod(
			"plane",
			TextureSlot.SIDE,
			ModTextureSlots.BASE,
			ModTextureSlots.PLANE
	);

	public static final ModelTemplate PLANE_SIDE_CUTOUT = createCutoutMod(
			"plane_side",
			"_side",
			TextureSlot.SIDE,
			ModTextureSlots.BASE,
			ModTextureSlots.PLANE
	);

	public static final ModelTemplate FULLBRIGHT = createMod(
			"template_fullbright",
			TextureSlot.ALL
	);

	public static final ModelTemplate CHEST = createMod(
			"template_chest",
			ModTextureSlots.CHEST,
			TextureSlot.PARTICLE
	);

	/**
	 * A model that extends item/generated and uses the same transforms as the Vanilla bow.
	 */
	public static final ModelTemplate SIMPLE_ITEM = createModItem(
			"simple_model",
			TextureSlot.LAYER0
	);

	public static final ModelTemplate EMPTY = create();

	public static final ModelTemplate CUBE_BOTTOM_TOP_CUTOUT = createCutoutMc(
			"cube_bottom_top",
			TextureSlot.TOP,
			TextureSlot.BOTTOM,
			TextureSlot.SIDE
	);

	public static final ModelTemplate CROSS_CUTOUT = createCutoutMc(
			"cross",
			TextureSlot.CROSS
	);

	public static final ModelTemplate TINTED_CROSS_CUTOUT = createCutoutMc(
			"tinted_cross",
			TextureSlot.CROSS
	);

	private static ModelTemplate create(final TextureSlot... requiredSlots) {
		return new ModelTemplate(Optional.empty(), Optional.empty(), requiredSlots);
	}

	private static ModelTemplate createMc(final String model, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.withDefaultNamespace("block/" + model)),
				Optional.empty(),
				requiredSlots
		);
	}

	private static ModelTemplate createMcItem(final String model, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.withDefaultNamespace("item/" + model)),
				Optional.empty(),
				requiredSlots
		);
	}

	private static ModelTemplate createMcItem(final String model, final String suffix, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.withDefaultNamespace("item/" + model)),
				Optional.of(suffix),
				requiredSlots
		);
	}

	private static ModelTemplate createMc(final String model, final String suffix, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.withDefaultNamespace("block/" + model)),
				Optional.of(suffix),
				requiredSlots
		);
	}

	private static ModelTemplate createMod(final String model, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "block/" + model)),
				Optional.empty(),
				requiredSlots
		);
	}

	private static ModelTemplate createModItem(final String model, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "item/" + model)),
				Optional.empty(),
				requiredSlots
		);
	}

	private static ModelTemplate createModItem(final String model, final String suffix, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "item/" + model)),
				Optional.of(suffix),
				requiredSlots
		);
	}

	private static ModelTemplate createMod(final String model, final String suffix, final TextureSlot... requiredSlots) {
		return new ModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "block/" + model)),
				Optional.of(suffix),
				requiredSlots
		);
	}

	private static ModelTemplate createCutoutMc(final String model, final TextureSlot... requiredSlots) {
		return new RenderTypeModelTemplate(
				Optional.of(Identifier.withDefaultNamespace("block/" + model)),
				Optional.empty(),
				CUTOUT,
				requiredSlots
		);
	}

	private static ModelTemplate createCutoutMod(final String model, final TextureSlot... requiredSlots) {
		return new RenderTypeModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "block/" + model)),
				Optional.empty(),
				CUTOUT,
				requiredSlots
		);
	}

	private static ModelTemplate createCutoutMod(final String model, final String suffix, final TextureSlot... requiredSlots) {
		return new RenderTypeModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "block/" + model)),
				Optional.of(suffix),
				CUTOUT,
				requiredSlots
		);
	}

	private static ModelTemplate createCutoutModItem(final String model, final TextureSlot... requiredSlots) {
		return new RenderTypeModelTemplate(
				Optional.of(Identifier.fromNamespaceAndPath(TestMod3.MODID, "item/" + model)),
				Optional.empty(),
				CUTOUT,
				requiredSlots
		);
	}
}
