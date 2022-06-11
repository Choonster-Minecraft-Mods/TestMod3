package choonster.testmod3.world.level.biome.modifier;

import choonster.testmod3.init.levelgen.ModBiomeModifierSerializers;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

/**
 * A biome modifier that adds a spawn cost for a mob.
 *
 * @author Choonster
 */
public record AddMobSpawnCostBiomeModifier(
		HolderSet<Biome> biomes,
		EntityType<?> type,
		MobSpawnSettings.MobSpawnCost spawnCost
) implements BiomeModifier {
	@Override
	public void modify(final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD && biomes.contains(biome)) {
			builder.getMobSpawnSettings().addMobCharge(type, spawnCost.getEnergyBudget(), spawnCost.getCharge());
		}
	}

	@Override
	public Codec<AddMobSpawnCostBiomeModifier> codec() {
		return ModBiomeModifierSerializers.ADD_MOB_SPAWN_COST.get();
	}
}
