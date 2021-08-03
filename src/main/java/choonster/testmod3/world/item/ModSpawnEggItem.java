package choonster.testmod3.world.item;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * An extension of the Vanilla spawn egg that works with Forge's DeferredRegister system.
 *
 * @author Choonster
 */
public class ModSpawnEggItem extends SpawnEggItem {
	private static final Field BY_ID = ObfuscationReflectionHelper.findField(SpawnEggItem.class, /* BY_ID */ "f_43201_");

	private final Supplier<? extends EntityType<?>> entityType;

	public ModSpawnEggItem(final Supplier<? extends EntityType<?>> entityType, final int primaryColor, final int secondaryColor, final Item.Properties properties) {
		//noinspection ConstantConditions
		super(null, primaryColor, secondaryColor, properties);

		this.entityType = entityType;
	}

	/**
	 * Adds the specified mod spawn eggs to the Vanilla {@code SpawnEggItem.EGGS} map.
	 * <p>
	 * This should be called on the main thread after common setup.
	 *
	 * @param modSpawnEggs The spawn egg items to add to the map
	 */
	public static void addEggsToEggsMap(final Collection<? extends Supplier<ModSpawnEggItem>> modSpawnEggs) {
		final Map<EntityType<?>, SpawnEggItem> eggsByID;
		try {
			@SuppressWarnings("unchecked")
			final Map<EntityType<?>, SpawnEggItem> map = (Map<EntityType<?>, SpawnEggItem>) BY_ID.get(null);
			eggsByID = map;
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to add spawn egg item to EGGS map", e);
		}

		modSpawnEggs.stream()
				.map(Supplier::get)
				.forEach(egg -> eggsByID.put(egg.entityType.get(), egg));
	}

	@Override
	public EntityType<?> getType(@Nullable final CompoundTag nbt) {
		if (nbt != null && nbt.contains("EntityTag", Constants.NBT.TAG_COMPOUND)) {
			final CompoundTag entityTag = nbt.getCompound("EntityTag");
			if (entityTag.contains("id", Constants.NBT.TAG_STRING)) {
				return EntityType.byString(entityTag.getString("id")).orElse(entityType.get());
			}
		}

		return entityType.get();
	}
}
