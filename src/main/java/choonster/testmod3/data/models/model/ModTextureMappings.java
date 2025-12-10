package choonster.testmod3.data.models.model;

import choonster.testmod3.TestMod3;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.block.Block;

/**
 * Creates this mod's {@link TextureMapping TextureMappings}.
 *
 * @author Choonster
 */
public class ModTextureMappings {
	private static final String COLORED_ROTATABLE_PREFIX = "block/colored_rotatable/";

	public static TextureMapping orientableSingle(final Block frontBlock, final Block otherBlock) {
		return new TextureMapping()
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(otherBlock))
				.put(TextureSlot.FRONT, TextureMapping.getBlockTexture(frontBlock))
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(otherBlock));
	}

	public static TextureMapping orientableSingle(final Block frontBlock, final Block otherBlock, final String suffix) {
		return new TextureMapping()
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(otherBlock, suffix))
				.put(TextureSlot.FRONT, TextureMapping.getBlockTexture(frontBlock, suffix))
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(otherBlock, suffix));
	}

	public static TextureMapping cubeBottomTop(final Block sideBlock, final Block bottomBlock, final Block topBlock) {
		return new TextureMapping()
				.put(TextureSlot.SIDE, TextureMapping.getBlockTexture(sideBlock))
				.put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(bottomBlock))
				.put(TextureSlot.TOP, TextureMapping.getBlockTexture(topBlock));
	}

	public static TextureMapping coloredRotatable(final DyeColor color, final String frontSuffix) {
		final var side = Identifier.fromNamespaceAndPath(
				TestMod3.MODID,
				COLORED_ROTATABLE_PREFIX + color.getSerializedName()
		);

		final var front = side.withSuffix(frontSuffix);

		return new TextureMapping()
				.put(TextureSlot.SIDE, side)
				.put(TextureSlot.FRONT, front)
				.put(TextureSlot.TOP, side);
	}

}
