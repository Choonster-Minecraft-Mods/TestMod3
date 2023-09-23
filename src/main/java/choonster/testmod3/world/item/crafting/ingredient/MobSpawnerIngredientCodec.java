package choonster.testmod3.world.item.crafting.ingredient;

import choonster.testmod3.TestMod3;
import choonster.testmod3.serialization.VanillaCodecs;
import choonster.testmod3.util.RegistryUtil;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * An ingredient serializer that produces a mob spawner of the specified entity.
 * <p>
 * JSON Properties:
 * <ul>
 * <li>All properties used by {@link CraftingHelper#getItemStack} (including NBT)</li>
 * <li><code>entity</code> - The registry name of the {@link EntityType} (required string)</li>
 * </ul>
 *
 * @author Choonster
 */
public class MobSpawnerIngredientCodec {
	public static final ResourceLocation TYPE = new ResourceLocation(TestMod3.MODID, "mob_spawner");
	public static final Codec<Data> DATA_CODEC = RecordCodecBuilder.create(instance -> instance.group(

					VanillaCodecs.ITEMSTACK_NONAIR_CODEC
							.fieldOf("item")
							.forGetter(Data::item),

					ForgeRegistries.ENTITY_TYPES.getCodec()
							.fieldOf("entity")
							.forGetter(Data::entityType),

					ResourceLocation.CODEC
							.fieldOf("type")
							.forGetter(x -> TYPE)

			).apply(instance, (item, entity, _type) -> new Data(item, entity))
	);

	public static final Codec<Ingredient> CODEC = DATA_CODEC.flatComapMap(
			data -> {
				final var blockEntityData = data.item.getOrCreateTagElement("BlockEntityTag");

				final var spawnData = blockEntityData.getCompound("SpawnData");
				spawnData.putString("id", RegistryUtil.getKey(data.entityType).toString());
				blockEntityData.put("SpawnData", spawnData);
				blockEntityData.put("SpawnPotentials", blockEntityData.getList("SpawnPotentials", Tag.TAG_COMPOUND));

				// TODO: Partial NBT match
				return Ingredient.of(data.item);
			},
			ingredient -> DataResult.error(() -> "Can't convert Ingredient back to MobSpawnerIngredientCodec.Data")
	);

	public record Data(ItemStack item, EntityType<?> entityType) {
	}
}
