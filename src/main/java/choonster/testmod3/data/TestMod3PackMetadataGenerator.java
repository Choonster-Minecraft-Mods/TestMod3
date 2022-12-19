package choonster.testmod3.data;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableMap;
import com.mojang.bridge.game.PackType;
import net.minecraft.DetectedVersion;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

import static net.minecraft.server.packs.PackType.CLIENT_RESOURCES;
import static net.minecraft.server.packs.PackType.SERVER_DATA;

/**
 * Generates this mod's <b>pack.mcmeta</b> file.
 *
 * @author Choonster
 */
public class TestMod3PackMetadataGenerator {
	public static PackMetadataGenerator create(final PackOutput output) {
		return new PackMetadataGenerator(output)
				.add(
						PackMetadataSection.TYPE,
						new PackMetadataSection(Component.translatable(TestMod3Lang.PACK_DESCRIPTION_TESTMOD3.getTranslationKey()),
								DetectedVersion.BUILT_IN.getPackVersion(PackType.RESOURCE),
								ImmutableMap.<net.minecraft.server.packs.PackType, Integer>builder()
										.put(SERVER_DATA, DetectedVersion.BUILT_IN.getPackVersion(PackType.DATA))
										.put(CLIENT_RESOURCES, DetectedVersion.BUILT_IN.getPackVersion(PackType.RESOURCE))
										.build()
						)
				);
	}
}
