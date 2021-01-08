package choonster.testmod3.item;

import com.google.common.collect.Sets;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import javax.annotation.Nullable;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;

/**
 * @author Choonster
 */
public class ModSpawnEggItem extends SpawnEggItem {
	private static final Field EGGS = ObfuscationReflectionHelper.findField(SpawnEggItem.class, /* EGGS */ "field_195987_b");

	private static final Set<ModSpawnEggItem> MOD_EGGS = Collections.synchronizedSet(Sets.newIdentityHashSet());

	private final Supplier<? extends EntityType<?>> entityType;

	public ModSpawnEggItem(final Supplier<? extends EntityType<?>> entityType, final int primaryColor, final int secondaryColor, final Item.Properties properties) {
		//noinspection ConstantConditions
		super(null, primaryColor, secondaryColor, properties);

		this.entityType = entityType;

		MOD_EGGS.add(this);
	}

	/**
	 * Adds all mod spawn eggs to the Vanilla {@code SpawnEggItem.EGGS} map.
	 * <p>
	 * This should be called on the main thread after common setup.
	 */
	public static void addEggsToEggsMap() {
		try {
			@SuppressWarnings("unchecked")
			final Map<EntityType<?>, SpawnEggItem> eggs = (Map<EntityType<?>, SpawnEggItem>) EGGS.get(null);

			for (final ModSpawnEggItem egg : MOD_EGGS) {
				eggs.put(egg.entityType.get(), egg);
			}
		} catch (final IllegalAccessException e) {
			throw new RuntimeException("Unable to add spawn egg item to EGGS map", e);
		}
	}

	@Override
	public EntityType<?> getType(@Nullable final CompoundNBT nbt) {
		if (nbt != null && nbt.contains("EntityTag", Constants.NBT.TAG_COMPOUND)) {
			final CompoundNBT entityTag = nbt.getCompound("EntityTag");
			if (entityTag.contains("id", Constants.NBT.TAG_STRING)) {
				return EntityType.byKey(entityTag.getString("id")).orElse(entityType.get());
			}
		}

		return entityType.get();
	}
}
