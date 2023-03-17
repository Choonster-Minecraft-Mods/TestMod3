package choonster.testmod3.data;

import choonster.testmod3.text.TestMod3Lang;
import com.google.common.collect.ImmutableMap;
import net.minecraft.DetectedVersion;
import net.minecraft.data.PackOutput;
import net.minecraft.data.metadata.PackMetadataGenerator;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;

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
								DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES),
								ImmutableMap.<net.minecraft.server.packs.PackType, Integer>builder()
										.put(PackType.SERVER_DATA, DetectedVersion.BUILT_IN.getPackVersion(PackType.SERVER_DATA))
										.put(PackType.CLIENT_RESOURCES, DetectedVersion.BUILT_IN.getPackVersion(PackType.CLIENT_RESOURCES))
										.build()
						)
				);
	}
}
