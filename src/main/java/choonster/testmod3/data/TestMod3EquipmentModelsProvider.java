package choonster.testmod3.data;

import choonster.testmod3.init.ModEquipmentModels;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.equipment.EquipmentModel;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

/**
 * Generates this mod's equipment models.
 *
 * @author Choonster
 */
public class TestMod3EquipmentModelsProvider implements DataProvider {
	private final PackOutput.PathProvider pathProvider;

	public TestMod3EquipmentModelsProvider(final PackOutput output) {
		pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models/equipment");
	}

	@Override
	public CompletableFuture<?> run(final CachedOutput output) {
		final var map = new HashMap<ResourceLocation, EquipmentModel>();

		bootstrap((id, equipmentModel) -> {
			if (map.putIfAbsent(id, equipmentModel) != null) {
				throw new IllegalStateException("Tried to register equipment model twice for id: " + id);
			}
		});

		return DataProvider.saveAll(output, EquipmentModel.CODEC, pathProvider, map);
	}

	@Override
	public String getName() {
		return "TestMod3 Equipment Model Definitions";
	}

	private static void bootstrap(final BiConsumer<ResourceLocation, EquipmentModel> p_360870_) {
		p_360870_.accept(ModEquipmentModels.REPLACEMENT, onlyHumanoid("chainmail"));
	}

	private static EquipmentModel onlyHumanoid(final String textureName) {
		return EquipmentModel.builder().addHumanoidLayers(ResourceLocation.withDefaultNamespace(textureName)).build();
	}
}
