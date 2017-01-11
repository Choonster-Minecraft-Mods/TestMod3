package choonster.testmod3.init;

import choonster.testmod3.TestMod3;
import choonster.testmod3.entity.EntityBlockDetectionArrow;
import choonster.testmod3.entity.EntityModArrow;
import choonster.testmod3.entity.EntityPlayerAvoidingCreeper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ModEntities {
	public static void registerEntities() {
		registerEntity(EntityModArrow.class, "mod_arrow", 64, 20, false);
		registerEntity(EntityBlockDetectionArrow.class, "block_detection_arrow", 64, 20, false);
		registerEntity(EntityPlayerAvoidingCreeper.class, "player_avoiding_creeper", 80, 3, true, 0x0DA70B, 0x101010);
	}

	public static void addSpawns() {
		EntityRegistry.addSpawn(EntityGuardian.class, 100, 5, 20, EnumCreatureType.WATER_CREATURE, getBiomes(BiomeDictionary.Type.OCEAN));
		copySpawns(EntityPlayerAvoidingCreeper.class, EnumCreatureType.MONSTER, EntityCreeper.class, EnumCreatureType.MONSTER);
	}

	/**
	 * Get an array of {@link Biome}s with the specified {@link BiomeDictionary.Type}.
	 *
	 * @param type The Type
	 * @return An array of Biomes
	 */
	private static Biome[] getBiomes(BiomeDictionary.Type type) {
		return BiomeDictionary.getBiomes(type).stream().toArray(Biome[]::new);
	}


	/**
	 * Add a spawn list entry for {@code classToAdd} in each {@link Biome} with an entry for {@code classToCopy} using the same weight and group count.
	 *
	 * @param classToAdd         The class to add spawn entries for
	 * @param creatureTypeToAdd  The EnumCreatureType to add spawn entries for
	 * @param classToCopy        The class to copy spawn entries from
	 * @param creatureTypeToCopy The EnumCreatureType to copy spawn entries from
	 */
	private static void copySpawns(Class<? extends EntityLiving> classToAdd, EnumCreatureType creatureTypeToAdd, Class<? extends EntityLiving> classToCopy, EnumCreatureType creatureTypeToCopy) {
		for (final Biome biome : ForgeRegistries.BIOMES) {
			biome.getSpawnableList(creatureTypeToCopy).stream()
					.filter(entry -> entry.entityClass == classToCopy)
					.findFirst()
					.ifPresent(spawnListEntry ->
							biome.getSpawnableList(creatureTypeToAdd).add(new Biome.SpawnListEntry(classToAdd, spawnListEntry.itemWeight, spawnListEntry.minGroupCount, spawnListEntry.maxGroupCount))
					);
		}
	}

	private static int entityID = 0;

	/**
	 * Register an entity with the specified tracking values.
	 *
	 * @param entityClass          The entity's class
	 * @param entityName           The entity's unique name
	 * @param trackingRange        The range at which MC will send tracking updates
	 * @param updateFrequency      The frequency of tracking updates
	 * @param sendsVelocityUpdates Whether to send velocity information packets as well
	 */
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates) {
		final ResourceLocation registryName = new ResourceLocation(TestMod3.MODID, entityName);
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.toString(), entityID++, TestMod3.instance, trackingRange, updateFrequency, sendsVelocityUpdates);
	}

	/**
	 * Register an entity with the specified tracking values and spawn egg colours.
	 *
	 * @param entityClass          The entity's class
	 * @param entityName           The entity's unique name
	 * @param trackingRange        The range at which MC will send tracking updates
	 * @param updateFrequency      The frequency of tracking updates
	 * @param sendsVelocityUpdates Whether to send velocity information packets as well
	 * @param eggPrimary           The spawn egg's primary (background) colour
	 * @param eggSecondary         The spawn egg's secondary (foreground) colour
	 */
	private static void registerEntity(Class<? extends Entity> entityClass, String entityName, int trackingRange, int updateFrequency, boolean sendsVelocityUpdates, int eggPrimary, int eggSecondary) {
		final ResourceLocation registryName = new ResourceLocation(TestMod3.MODID, entityName);
		EntityRegistry.registerModEntity(registryName, entityClass, registryName.toString(), entityID++, TestMod3.instance, trackingRange, updateFrequency, sendsVelocityUpdates, eggPrimary, eggSecondary);
	}
}
