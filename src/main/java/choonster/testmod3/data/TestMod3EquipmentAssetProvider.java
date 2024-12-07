package choonster.testmod3.data;

import choonster.testmod3.init.ModEquipmentAssets;
import net.minecraft.client.resources.model.EquipmentClientInfo;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentAsset;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Generates this mod's equipment assets.
 *
 * @author Choonster
 */
public class TestMod3EquipmentAssetProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;

	public TestMod3EquipmentAssetProvider(final PackOutput output) {
		pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "equipment");
	}

	@Override
	public CompletableFuture<?> run(final CachedOutput output) {
		final var map = new HashMap<ResourceKey<EquipmentAsset>, EquipmentClientInfo>();

		bootstrap((id, equipmentClientInfo) -> {
			if (map.putIfAbsent(id, equipmentClientInfo) != null) {
				throw new IllegalStateException("Tried to register equipment asset twice for id: " + id);
			}
		});

		return DataProvider.saveAll(output, EquipmentClientInfo.CODEC, pathProvider::json, map);
	}

	@Override
	public String getName() {
		return "TestMod3 Equipment Asset Definitions";
	}

	private static void bootstrap(final BiConsumer<ResourceKey<EquipmentAsset>, EquipmentClientInfo> consumer) {
		consumer.accept(ModEquipmentAssets.REPLACEMENT, onlyHumanoid("chainmail"));
	}

	private static EquipmentClientInfo onlyHumanoid(final String textureName) {
		return EquipmentClientInfo.builder()
				.addHumanoidLayers(ResourceLocation.withDefaultNamespace(textureName))
				.build();
	}
}
